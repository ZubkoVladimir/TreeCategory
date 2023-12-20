# TEST TASK FOR PANDEV
## Суть бота
Согласно тестовому заданию, надо было создать бота, который выводит из БД данные о дереве категорий. 
Бот должен уметь:
* Выводить всё дерево категорий (команда /viewTree)
* Добавлять родительские и дочерние элементы (команда /addElement [parent] [optional: child])
* Удалять элементы (/removeElement [element])
* Выводить команду /help
___
## Команды

### /start и /help 

Стандартные команды для работы с телеграм-ботом

#### /start

При вводе данной команды пользователь получает приветственное сообщение

```java
private void startCommandReceived(long chatId, String name) {
        String answer = ("Hi, " + name + ", nice to meet you!");
        sendMessage(chatId, answer);
    }
```
*Команду можно улучшить добавив коллекцию, содержащую chatId пользователей, чтобы при повторном вводе команды выводила другой текст*

#### /help 

Выводит все команды с описанием и правилами ввода данных.

```java
 static final String HELP_TEXT =
            """
                    You can execute commands from the main menu on the left or by typing a command:

                    Type /start to see a welcome message

                    Type /viewTree to see tree category
                    
                    Type /addElement <parent category> to create category
                    
                    Type /addElement <parent category> <child category> to add child category
                    
                    Type /removeElement <categories id> to remove category

                    Type /help to see this message again""";

```

____
### /viewTree

Команда для вывода дерева была реализована c помощью стандартного переопределения метода toString().

```java
public String viewTree() {

        String text ;
        if(!categoryRepo.findAll().toString().isEmpty()){
            text = categoryRepo.findAll().toString();
        }else{
            text = "Tree is empty."+"\n"+
            "Please, use command /addElement to create category";
        }


        return text;
    }
```
``` java
 public String toString() {
        return
                "id= " + id + "\n" +
                "Category " + CategoryParent + "\n" +
                        "|" +"\n" +
                        "|"+
                        "--"+
                "CategoryChild= " + CategoryChild + "\n";
}
```
Я выбрал такой подход для наглядности и создании простой структуризации, где родительский класс, указывается под ID, а дочерний класс - под родительским(при отсутствии значения выводится *null*)

### /addElement

Команда для добавления элемента *(ов)* вносит в БД данные, которые следовали за ней.

```java
public void addElement(String message) {
        String[] text = message.split(" ");
        String parent = text[1];

        if(!parent.isEmpty()) {
            Category tree = new Category();

                tree.setCategoryParent(parent);
                if (text.length > 2) {
                    String child = text[2];
                    tree.setCategoryChild(child);
                }


                categoryRepo.save(tree);

        }
    }
```
*Реализация неидеальна, поскольку отсутствует проверка на наличие эквивалентных элементов, поэтому при добавлении одного и того же родительского класса, будут создаваться два отдельных поля.*

### /removeElement

Команда для удаления категории по ID

```java 
public void removeElement(Long chatId, String message){

        String[] text = message.split(" ");
        Long parentId = Long.valueOf(text[1]);

        try{
        categoryRepo.deleteById(parentId);}
        catch (Exception e){
            sendMessage(chatId, "This ID is not exist");
        }

    }
```
*Изначально планировалось удалять элементы по названию категории, но при добавлении в репозиторий команды deleteByCategoryParent() программа полностью переставала работать, выводя в stack trace ошибки с невозможностью создать bean-компоненты.*


## Тесты
Для меня unit-тестирование является новым направлением, поэтому за выделенный срок я попытался сделать пару тестов, которые не сильно увенчались успехом. 

```java
@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock

    CategoryRepo categoryRepo;

   @Mock
    BotConfig botconfig;

    public String getBotUsername() {
        return botconfig.getBotToken();
    }

    public String getBotName() {
        return botconfig.getBotName();
    }

    public String getBotToken() {
        return botconfig.getBotToken();
    }
    @InjectMocks
    TelegramBot tgBot = new TelegramBot(botconfig, categoryRepo);


    @BeforeEach
    void setUp(){
        Category tree = new Category();
        tree.setCategoryParent("film");
    }

    @ParameterizedTest
    void addElement(){
        Category tree = new Category();
        tree.setCategoryParent("film");
        String message = "/addElement film";

        tgBot.addElement(message);

    }

    @Test
    void viewTree(){
        Mockito.when(categoryRepo.findAll());
        var a = tgBot.viewTree();
        Assertions.assertEquals(a , null);
    }

}
```