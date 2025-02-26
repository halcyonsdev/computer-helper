package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.cache.CacheManager;
import com.halcyon.computer.helper.cache.ChatStatus;
import com.halcyon.computer.helper.cache.ChatStatusType;
import com.halcyon.computer.helper.entity.Client;
import com.halcyon.computer.helper.entity.Problem;
import com.halcyon.computer.helper.service.BotExecutionsService;
import com.halcyon.computer.helper.service.ClientService;
import com.halcyon.computer.helper.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final ProblemService problemService;

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
                .replyMarkup(getClientStartMenuKeyboard())
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
                .replyMarkup(getClientStartMenuKeyboard())
                .build();
        clientMenu.enableMarkdown(true);

        botExecutionsService.editMessage(clientMenu);
    }

    public void handleRegistration(long chatId) {
        botExecutionsService.sendDefaultMessage(chatId, ENTER_NAME_MESSAGE);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.CLIENT_FULL_NAME));
    }

    public void handleCreateFullName(Message message) {
        processCreateStep(message, ENTER_PHONE_MESSAGE, ChatStatusType.CLIENT_PHONE, List.of(message.getText()));
    }

    private void processCreateStep(Message message, String nextMessage, ChatStatusType nextStatus, List<String> data) {
        long chatId = message.getChatId();

        botExecutionsService.sendDefaultMessage(chatId, nextMessage);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(nextStatus, data));
    }

    public void handleCreatePhone(Message message, ChatStatus chatStatus) {
        if (!isValidPhone(message.getText())) {
            botExecutionsService.sendDefaultMessage(message.getChatId(), ENTER_CORRECT_PHONE_MESSAGE);
            return;
        }

        processCreateStep(message, ENTER_EMAIL_MESSAGE, ChatStatusType.CLIENT_EMAIL, List.of(chatStatus.getData().get(0), message.getText()));
    }

    public void handleCreateEmail(Message message, ChatStatus chatStatus) {
        if (!isValidEmail(message.getText())) {
            botExecutionsService.sendDefaultMessage(message.getChatId(), ENTER_CORRECT_EMAIL_MESSAGE);
            return;
        }

        processCreateStep(
                message,
                ENTER_CLIENT_ADDRESS_MESSAGE,
                ChatStatusType.CLIENT_ADDRESS,
                List.of(chatStatus.getData().get(0), chatStatus.getData().get(1), message.getText())
        );
    }

    public void handleCreateAddress(Message message, ChatStatus chatStatus) {
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
        var problemCreateMessage = SendMessage.builder()
                .chatId(chatId)
                .text(PROBLEM_MESSAGE)
                .replyMarkup(getProblemCategoriesKeyboard())
                .build();
        problemCreateMessage.enableHtml(true);

        botExecutionsService.sendMessage(problemCreateMessage);
    }

    public void editToProblemQuestion(CallbackQuery callbackQuery) {
        var problemCreateMessage = EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(PROBLEM_MESSAGE)
                .replyMarkup(getProblemCategoriesKeyboard())
                .build();
        problemCreateMessage.enableHtml(true);

        botExecutionsService.editMessage(problemCreateMessage);
    }

    public void editToSubcategories(CallbackQuery callbackQuery, InlineKeyboardMarkup keyboardMarkup) {
        var subcategories = EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(SUBCATEGORY_MESSAGE)
                .replyMarkup(keyboardMarkup)
                .build();
        subcategories.enableHtml(true);

        botExecutionsService.editMessage(subcategories);
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

    public void handleIssue(long chatId, String category, String subcategory) {
        botExecutionsService.sendDefaultMessage(chatId, DESCRIBE_PROBLEM);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.PROBLEM_DESCRIPTION, List.of(category, subcategory)));
    }

    public void handleDescription(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();

        Problem problem = Problem.builder()
                .category(chatStatus.getData().get(0))
                .subcategory(chatStatus.getData().get(1))
                .description(message.getText())
                .status("Создается")
                .build();

        cacheManager.save("problem:" + chatId, problem);
        sendProblemMessage(chatId, problem);

        cacheManager.remove(String.valueOf(chatId));
    }

    private void sendProblemMessage(long chatId, Problem problem) {
        if (problem.getFileId() != null) {
            var problemMessage = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(problem.getFileId()))
                    .caption(getProblemInfo(problem))
                    .replyMarkup(getProblemMenuKeyboard())
                    .parseMode("HTML")
                    .build();

            botExecutionsService.sendPhoto(problemMessage);
            return;
        }

        var problemMessage = SendMessage.builder()
                .chatId(chatId)
                .text(getProblemInfo(problem))
                .replyMarkup(getProblemMenuKeyboard())
                .build();
        problemMessage.enableHtml(true);

        botExecutionsService.sendMessage(problemMessage);
    }

    private void editToProblemMessage(CallbackQuery callbackQuery, Problem problem) {
        long chatId = callbackQuery.getMessage().getChatId();

        if (problem.getFileId() != null) {
            var problemMessage = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(problem.getFileId()))
                    .caption(getProblemInfo(problem))
                    .replyMarkup(getProblemMenuKeyboard())
                    .parseMode("HTML")
                    .build();

            botExecutionsService.sendPhoto(problemMessage);
            return;
        }

        var problemMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(getProblemInfo(problem))
                .replyMarkup(getProblemMenuKeyboard())
                .build();
        problemMessage.enableHtml(true);

        botExecutionsService.editMessage(problemMessage);
    }

    private String getProblemInfo(Problem problem) {
        return String.format(
                PROBLEM_INFO,
                problem.getCategory(),
                problem.getSubcategory(),
                problem.getDescription(),
                problem.getStatus()
        );
    }

    public void handleSettingImage(long chatId) {
        botExecutionsService.sendDefaultMessage(chatId, SEND_IMAGE_MESSAGE);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.PROBLEM_IMAGE));
    }

    public void handleImage(Message message) {
        long chatId = message.getChatId();

        String fileId = message.getPhoto().stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .map(PhotoSize::getFileId)
                .orElse(null);

        Optional<Problem> problemOptional = cacheManager.fetch("problem:" + chatId, Problem.class);

        if (fileId == null || problemOptional.isEmpty()) {
            botExecutionsService.sendDefaultMessage(chatId, "Не удалось получить фото.");
            return;
        }

        Problem problem = problemOptional.get();
        problem.setFileId(fileId);

        cacheManager.save("problem:" + chatId, problem);
        sendProblemMessage(chatId, problem);

        cacheManager.remove(String.valueOf(chatId));
    }

    public void handleSendingProblem(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        Optional<Problem> problemOptional = cacheManager.fetch("problem:" + chatId, Problem.class);

        if (problemOptional.isEmpty()) {
            botExecutionsService.sendDefaultErrorMessage(chatId);
            return;
        }

        Problem problem = problemOptional.get();
        problem.setStatus("В обработке");
        problem = problemService.save(problem, chatId);

        cacheManager.remove("problem:" + chatId);
        editToProblemMessage(callbackQuery, problem);
        sendStartMessage(chatId);
    }

    public void editToMyProblems(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        List<Problem> clientProblems = problemService.getClientProblems(chatId);
        int el = Integer.parseInt(callbackQuery.getData().split("_")[2]);

        var problems = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(MY_PROBLEMS_MESSAGE)
                .replyMarkup(getMyProblemsKeyboard(clientProblems, el))
                .build();
        problems.enableMarkdown(true);

        botExecutionsService.editMessage(problems);
    }

    public void sendProblem(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        long problemId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        Problem problem = problemService.findById(problemId);

        if (problem.getFileId() != null) {
            var problemMessage = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(problem.getFileId()))
                    .caption(getProblemInfo(problem))
                    .replyMarkup(getClientToStartKeyboard())
                    .parseMode("HTML")
                    .build();

            botExecutionsService.sendPhoto(problemMessage);
            return;
        }

        var problemMessage = SendMessage.builder()
                .chatId(chatId)
                .text(getProblemInfo(problem))
                .replyMarkup(getClientToStartKeyboard())
                .build();
        problemMessage.enableHtml(true);

        botExecutionsService.sendMessage(problemMessage);
    }
}
