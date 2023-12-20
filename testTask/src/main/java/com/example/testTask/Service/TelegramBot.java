package com.example.testTask.Service;

import com.example.testTask.Config.BotConfig;
import com.example.testTask.Entity.Category;
import com.example.testTask.Repositories.CategoryRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
@AllArgsConstructor

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botconfig;
    @Autowired
    private final CategoryRepo categoryRepo;

    static final String HELP_TEXT =
            """
                    You can execute commands by typing a command:

                    Type /start to see a welcome message

                    Type /viewTree to see tree category
                    
                    Type /addElement <parent category> to create category
                    
                    Type /addElement <parent category> <child category> to add child category
                    
                    Type /removeElement <categories id> to remove category

                    Type /help to see this message again""";



    public List<Category> list(){
        return categoryRepo.findAll();
    }


//реализация функционала бота и отработка входящих собщений
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
        long chatId = update.getMessage().getChatId();

        var name = update.getMessage().getChat().getFirstName();
        String message = update.getMessage().getText();
            String[] text = message.split(" ");
            String command = text[0];
            String body = " ";
            if(text.length>1) {
                body = text[1];
            }

            switch (command) {
                case "/start" -> startCommandReceived(chatId, name);
                case "/help" -> sendMessage(chatId, HELP_TEXT);
                case "/viewTree" -> sendMessage(chatId, viewTree());
                case "/addElement" -> {
                    addElement(message);
                    sendMessage(chatId, "Ok, element /" + body + "/ added");

                }
                case "/removeElement" -> {
                    removeElement(chatId ,message);
                    sendMessage(chatId, "Category /" + body + "/ removed");
                }
                default -> sendMessage(chatId, "Sorry, command was not recognized");
            }



        }


    }

    //Стартовый вывод текста при вводе команды /start
    private void startCommandReceived(long chatId, String name) {
        String answer = ("Hi, " + name + ", nice to meet you!");
        sendMessage(chatId, answer);
    }




// Просмотр всего дерева категорий
    public String viewTree() {

        String text ;
        if(!categoryRepo.findAll().isEmpty()){
            text = categoryRepo.findAll().toString();
        }else{
            text = "Tree is empty."+"\n"+
            "Please, use command /addElement to create category";
        }

        return text;
    }

    //Добавление нового элемента осуществляется путем написания название родительского и дочернего классов сразу после команды
    public void addElement(String message) {
        String[] text = message.split(" ");
        String parent = text[1];

        if(!parent.isEmpty()) {
            Category tree = new Category();

            //List<Category> findByCategoryParentLikeIgnoreCase(String parent);

                tree.setCategoryParent(parent);
                if (text.length > 2) {
                    String child = text[2];
                    tree.setCategoryChild(child);
                }


                categoryRepo.save(tree);

        }
    }




//Удаление категорий осуществляется путем поиска соответствующего ID
    public void removeElement(Long chatId, String message){

        String[] text = message.split(" ");
        Long parentId = Long.valueOf(text[1]);


//        categoryRepo.deleteByCategoryParentLikeIgnoreCase(parent);
        try{
        categoryRepo.deleteById(parentId);}
        catch (Exception e){
            sendMessage(chatId, "This ID is not exist");
        }

    }


//Стандартный метод для отправки сообщений ботом
    private void sendMessage(Long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("SendMessage error");
        }



    }

    @Override
    public String getBotUsername() {
        return botconfig.getBotToken();
    }

    public String getBotName() {
        return botconfig.getBotName();
    }

    public String getBotToken() {
        return botconfig.getBotToken();
    }


}
