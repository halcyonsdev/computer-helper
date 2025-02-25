package com.halcyon.computer.helper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class BotExecutionsService {
    private final TelegramClient telegramClient;

    private final String botToken;

    public BotExecutionsService(@Value("${bot.token}") String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMessage(EditMessageText editMessageText) {
        try {
            telegramClient.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDefaultMessage(long chatId, String text) {
        SendMessage defaultMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        defaultMessage.enableHtml(true);

        sendMessage(defaultMessage);
    }
}
