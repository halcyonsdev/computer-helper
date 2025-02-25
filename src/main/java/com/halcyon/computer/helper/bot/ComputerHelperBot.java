package com.halcyon.computer.helper.bot;

import com.halcyon.computer.helper.update.TelegramUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ComputerHelperBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @Value("${bot.token}")
    private String botToken;

    private final TelegramUpdateHandler telegramUpdateHandler;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            telegramUpdateHandler.handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            telegramUpdateHandler.handleCallbackQuery(update.getCallbackQuery());
        }
    }
}
