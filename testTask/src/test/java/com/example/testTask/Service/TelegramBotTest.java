package com.example.testTask.Service;

import com.example.testTask.Config.BotConfig;
import com.example.testTask.Entity.Category;
import com.example.testTask.Repositories.CategoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


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
