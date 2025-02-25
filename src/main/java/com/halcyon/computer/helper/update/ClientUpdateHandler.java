package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.cache.CacheManager;
import com.halcyon.computer.helper.cache.ChatStatus;
import com.halcyon.computer.helper.cache.ChatStatusType;
import com.halcyon.computer.helper.entity.Client;
import com.halcyon.computer.helper.service.BotExecutionsService;
import com.halcyon.computer.helper.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.function.BiConsumer;

import static com.halcyon.computer.helper.bot.BotResponse.*;
import static com.halcyon.computer.helper.util.KeyboardUtil.*;
import static com.halcyon.computer.helper.validation.Validator.*;

@Component
@RequiredArgsConstructor
public class ClientUpdateHandler {
    private final BotExecutionsService botExecutionsService;
    private final CacheManager cacheManager;
    private final ClientService clientService;

    public void sendStartMessage(long chatId) {
        if (clientService.existsByChatId(chatId)) {
            sendClientStartMenu(chatId);
        } else {
            var registrationMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(REGISTRATION_START_MESSAGE)
                    .replyMarkup(getRegistrationKeyboard())
                    .build();
            registrationMessage.enableMarkdown(true);

            botExecutionsService.sendMessage(registrationMessage);
        }
    }

    private void editToRegistration(CallbackQuery callbackQuery) {
        var registrationMessage = EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(REGISTRATION_START_MESSAGE)
                .replyMarkup(getRegistrationKeyboard())
                .build();
        registrationMessage.enableMarkdown(true);

        botExecutionsService.editMessage(registrationMessage);
    }

    private void sendClientStartMenu(long chatId) {
        Client client = clientService.findByChatId(chatId);

        var clientMenu = SendMessage.builder()
                .chatId(chatId)
                .text(String.format(CLIENT_START_MESSAGE, client.getFullName()))
                .replyMarkup(getClientStartMenu())
                .build();
        clientMenu.enableMarkdown(true);

        botExecutionsService.sendMessage(clientMenu);
    }

    public void editToClientStartMenu(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        Client client = clientService.findByChatId(chatId);

        var clientMenu = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(String.format(CLIENT_START_MESSAGE, client.getFullName()))
                .replyMarkup(getClientStartMenu())
                .build();
        clientMenu.enableMarkdown(true);

        botExecutionsService.editMessage(clientMenu);
    }

    public void handleRegistration(long chatId) {
        botExecutionsService.sendDefaultMessage(chatId, ENTER_NAME_MESSAGE);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.CLIENT_FULL_NAME));
    }

    public void handleClientFullName(Message message) {
        long chatId = message.getChatId();

        botExecutionsService.sendDefaultMessage(chatId, ENTER_PHONE_MESSAGE);

        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.CLIENT_PHONE, List.of(message.getText())));
    }

    public void handleClientPhone(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();

        if (!isValidPhone(message.getText())) {
            botExecutionsService.sendDefaultMessage(chatId, ENTER_CORRECT_PHONE_MESSAGE);
            return;
        }

        botExecutionsService.sendDefaultMessage(chatId, ENTER_EMAIL_MESSAGE);
        ChatStatus phoneStatus = new ChatStatus(ChatStatusType.CLIENT_EMAIL , List.of(chatStatus.getData().get(0), message.getText()));

        cacheManager.save(String.valueOf(chatId), phoneStatus);
    }

    public void handleClientEmail(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();

        if (!isValidEmail(message.getText())) {
            botExecutionsService.sendDefaultMessage(chatId, ENTER_CORRECT_EMAIL_MESSAGE);
            return;
        }

        botExecutionsService.sendDefaultMessage(chatId, ENTER_CLIENT_ADDRESS_MESSAGE);
        ChatStatus addressStatus = new ChatStatus(ChatStatusType.CLIENT_ADDRESS, List.of(chatStatus.getData().get(0), chatStatus.getData().get(1), message.getText()));

        cacheManager.save(String.valueOf(chatId), addressStatus);
    }

    public void handleClientAddress(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();

        String fullName = chatStatus.getData().get(0);
        String phone = chatStatus.getData().get(1);
        String email = chatStatus.getData().get(2);
        String address = message.getText();

        Client client = Client.builder()
                .fullName(fullName)
                .chatId(chatId)
                .phone(phone)
                .email(email)
                .address(address)
                .build();

        clientService.save(client);

        sendClientStartMenu(chatId);
        cacheManager.remove(String.valueOf(chatId));
    }

    public void sendProblemQuestion(long chatId) {
        botExecutionsService.sendDefaultMessage(chatId, PROBLEM_MESSAGE);
    }

    public void sendProfile(long chatId, Client client) {
        if (client == null) {
            client = clientService.findByChatId(chatId);
        }

        var profile = SendMessage.builder()
                .chatId(chatId)
                .text(getProfileText(client))
                .replyMarkup(getClientProfileKeyboard())
                .build();
        profile.enableHtml(true);

        botExecutionsService.sendMessage(profile);
    }

    private String getProfileText(Client client) {
        return String.format(
                CLIENT_PROFILE_MESSAGE,
                client.getFullName(),
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );
    }

    public void editProfile(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        Client client = clientService.findByChatId(chatId);

        var profile = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(getProfileText(client))
                .replyMarkup(getClientProfileKeyboard())
                .build();
        profile.enableHtml(true);

        botExecutionsService.editMessage(profile);
    }

    public void delete(CallbackQuery callbackQuery) {
        clientService.deleteByChatId(callbackQuery.getMessage().getChatId());
        editToRegistration(callbackQuery);
    }

    public void update(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        Client client = clientService.findByChatId(chatId);

        var updateProfile = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(getProfileText(client))
                .replyMarkup(getUpdateClientKeyboard())
                .build();
        updateProfile.enableHtml(true);

        botExecutionsService.editMessage(updateProfile);
    }

    public void sendUpdateClientFullName(long chatId) {
        sendUpdateRequest(chatId, ENTER_NAME_MESSAGE, ChatStatusType.UPDATE_CLIENT_FULL_NAME);
    }

    private void sendUpdateRequest(long chatId, String message, ChatStatusType chatStatusType) {
        botExecutionsService.sendDefaultMessage(chatId, message);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(chatStatusType));
    }

    public void handleUpdateClientFullName(Message message) {
        handleClientUpdate(message, Client::setFullName);
    }

    private void handleClientUpdate(Message message, BiConsumer<Client, String> updateAction) {
        long chatId = message.getChatId();
        Client client = clientService.findByChatId(chatId);

        updateAction.accept(client, message.getText());

        client = clientService.save(client);
        sendProfile(chatId, client);
        cacheManager.remove(String.valueOf(chatId));
    }

    public void sendUpdateClientPhone(long chatId) {
        sendUpdateRequest(chatId, ENTER_PHONE_MESSAGE, ChatStatusType.UPDATE_CLIENT_PHONE);
    }

    public void handleUpdateClientPhone(Message message) {
        handleClientUpdate(message, (client, value) -> {
            if (!isValidPhone(value)) {
                botExecutionsService.sendDefaultMessage(message.getChatId(), ENTER_CORRECT_PHONE_MESSAGE);
                return;
            }
            client.setPhone(value);
        });
    }

    public void sendUpdateClientEmail(long chatId) {
        sendUpdateRequest(chatId, ENTER_EMAIL_MESSAGE, ChatStatusType.UPDATE_CLIENT_EMAIL);
    }

    public void handleUpdateClientEmail(Message message) {
        handleClientUpdate(message, (client, value) -> {
            if (!isValidEmail(value)) {
                botExecutionsService.sendDefaultMessage(message.getChatId(), ENTER_CORRECT_EMAIL_MESSAGE);
                return;
            }

            client.setEmail(value);
        });
    }

    public void sendUpdateClientAddress(long chatId) {
        sendUpdateRequest(chatId, ENTER_CLIENT_ADDRESS_MESSAGE, ChatStatusType.UPDATE_CLIENT_ADDRESS);
    }

    public void handleUpdateClientAddress(Message message) {
        handleClientUpdate(message, Client::setAddress);
    }
}
