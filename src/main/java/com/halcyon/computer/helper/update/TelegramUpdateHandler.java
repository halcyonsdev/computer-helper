package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.cache.CacheManager;
import com.halcyon.computer.helper.cache.ChatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TelegramUpdateHandler {
    private final ClientUpdateHandler clientUpdateHandler;
    private final CacheManager cacheManager;

    public void handleMessage(Message message) {
        long chatId = message.getChatId();

        if (message.getText().equals("/start")) {
            clientUpdateHandler.sendStartMessage(chatId);
        } else if (message.getText().equals("/profile")) {
          clientUpdateHandler.sendProfile(chatId, null);
        } else {
            processStatus(message);
        }
    }

    public void processStatus(Message message) {
        Optional<ChatStatus> chatStatusOptional = cacheManager.fetch(String.valueOf(message.getChatId()), ChatStatus.class);

        if (chatStatusOptional.isEmpty()) {
            return;
        }

        ChatStatus chatStatus = chatStatusOptional.get();

        switch (chatStatus.getType()) {
            case CLIENT_FULL_NAME -> clientUpdateHandler.handleClientFullName(message);
            case CLIENT_PHONE -> clientUpdateHandler.handleClientPhone(message, chatStatus);
            case CLIENT_EMAIL -> clientUpdateHandler.handleClientEmail(message, chatStatus);
            case CLIENT_ADDRESS -> clientUpdateHandler.handleClientAddress(message, chatStatus);
            case UPDATE_CLIENT_FULL_NAME -> clientUpdateHandler.handleUpdateClientFullName(message);
            case UPDATE_CLIENT_PHONE -> clientUpdateHandler.handleUpdateClientPhone(message);
            case UPDATE_CLIENT_EMAIL -> clientUpdateHandler.handleUpdateClientEmail(message);
            case UPDATE_CLIENT_ADDRESS -> clientUpdateHandler.handleUpdateClientAddress(message);
        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "client_register" -> clientUpdateHandler.handleRegistration(chatId);
            case "update_client" -> clientUpdateHandler.update(callbackQuery);
            case "client_profile" -> clientUpdateHandler.editProfile(callbackQuery);
            case "client_problem" -> clientUpdateHandler.sendProblemQuestion(chatId);
            case "client_start" -> clientUpdateHandler.editToClientStartMenu(callbackQuery);
            case "update_client_name" -> clientUpdateHandler.sendUpdateClientFullName(chatId);
            case "update_client_phone" -> clientUpdateHandler.sendUpdateClientPhone(chatId);
            case "update_client_email" -> clientUpdateHandler.sendUpdateClientEmail(chatId);
            case "update_client_address" -> clientUpdateHandler.sendUpdateClientAddress(chatId);
            case "delete_client" -> clientUpdateHandler.delete(callbackQuery);
        }
    }
}
