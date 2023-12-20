package com.example.testTask.Config;


import com.example.testTask.Service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Component
public class BotInit {


    private final TelegramBot telegramBot;

    @Autowired
    public BotInit(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException{
        TelegramBotsApi telegramBot1 = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBot1.registerBot(telegramBot);
        }
        catch (TelegramApiException e){
            throw new TelegramApiException(e);
        }
    }

}
