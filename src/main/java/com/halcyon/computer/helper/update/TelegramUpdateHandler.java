package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.cache.CacheManager;
import com.halcyon.computer.helper.cache.ChatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.Optional;

import static com.halcyon.computer.helper.util.KeyboardUtil.*;

@Component
@RequiredArgsConstructor
public class TelegramUpdateHandler {
    private final ClientUpdateHandler clientUpdateHandler;
    private final SpecialistUpdateHandler specialistUpdateHandler;
    private final AdminUpdateHandler adminUpdateHandler;
    private final CacheManager cacheManager;

    @Value("${bot.admin.ids}")
    private List<Long> adminIds;

    public void handleMessage(Message message) {
        long chatId = message.getChatId();

        if (message.getText().equals("/start")) {
            if (adminIds.contains(message.getChatId())) {
                adminUpdateHandler.sendStartMessage(chatId);
            } else {
                clientUpdateHandler.sendStartMessage(chatId);
            }
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
            case CLIENT_FULL_NAME -> clientUpdateHandler.handleCreateFullName(message);
            case CLIENT_PHONE -> clientUpdateHandler.handleCreatePhone(message, chatStatus);
            case CLIENT_EMAIL -> clientUpdateHandler.handleCreateEmail(message, chatStatus);
            case CLIENT_ADDRESS -> clientUpdateHandler.handleCreateAddress(message, chatStatus);
            case UPDATE_CLIENT_FULL_NAME -> clientUpdateHandler.handleUpdateClientFullName(message);
            case UPDATE_CLIENT_PHONE -> clientUpdateHandler.handleUpdateClientPhone(message);
            case UPDATE_CLIENT_EMAIL -> clientUpdateHandler.handleUpdateClientEmail(message);
            case UPDATE_CLIENT_ADDRESS -> clientUpdateHandler.handleUpdateClientAddress(message);

            case SPECIALIST_FULL_NAME -> specialistUpdateHandler.handleCreateFullName(message);
            case SPECIALIST_PHONE -> specialistUpdateHandler.handleCreatePhone(message, chatStatus);
            case SPECIALIST_EMAIL -> specialistUpdateHandler.handleCreateEmail(message, chatStatus);

            case PROBLEM_DESCRIPTION -> clientUpdateHandler.handleDescription(message, chatStatus);
            case PROBLEM_IMAGE -> clientUpdateHandler.handleImage(message);

            case COUNTDOWN_CONTENT -> specialistUpdateHandler.handleCountdownContent(message, chatStatus);
        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "client_register" -> clientUpdateHandler.handleRegistration(chatId);
            case "update_client" -> clientUpdateHandler.update(callbackQuery);
            case "client_profile" -> clientUpdateHandler.editProfile(callbackQuery);
            case "client_problem" -> clientUpdateHandler.editToProblemQuestion(callbackQuery);
            case "client_start" -> clientUpdateHandler.editToClientStartMenu(callbackQuery);
            case "start_client_start" -> clientUpdateHandler.sendStartMessage(chatId);
            case "update_client_name" -> clientUpdateHandler.sendUpdateClientFullName(chatId);
            case "update_client_phone" -> clientUpdateHandler.sendUpdateClientPhone(chatId);
            case "update_client_email" -> clientUpdateHandler.sendUpdateClientEmail(chatId);
            case "update_client_address" -> clientUpdateHandler.sendUpdateClientAddress(chatId);
            case "delete_client" -> clientUpdateHandler.delete(callbackQuery);

            case "specialist_start" -> specialistUpdateHandler.editToStartMenu(callbackQuery);
            case "specialist_register" -> specialistUpdateHandler.handleRegistration(chatId);

            case "admin_start" -> adminUpdateHandler.editToStartMessage(callbackQuery);
            case "requests" -> adminUpdateHandler.editToSpecialistsRequests(callbackQuery);

            case "computer" -> clientUpdateHandler.editToSubcategories(callbackQuery, getComputerSubcategoriesKeyboard());
            case "pc_turn" -> clientUpdateHandler.handleIssue(chatId, "Компьютер/Ноутбук", "Не включается");
            case "pc_slow" -> clientUpdateHandler.handleIssue(chatId, "Компьютер/Ноутбук", "Медленно работает");
            case "pc_hangs" -> clientUpdateHandler.handleIssue(chatId, "Компьютер/Ноутбук", "Зависает");

            case "software" -> clientUpdateHandler.editToSubcategories(callbackQuery, getSoftwareSubcategoriesKeyboard());
            case "software_download" -> clientUpdateHandler.handleIssue(chatId, "Программное обеспечение", "Помощь с установкой программ");
            case "software_virus" -> clientUpdateHandler.handleIssue(chatId, "Программное обеспечение",  "Проверить/почистить от вирусов");
            case "software_crashes" -> clientUpdateHandler.handleIssue(chatId, "Программное обеспечение",  "Не запускается/вылетает программа");
            case "software_oc" -> clientUpdateHandler.handleIssue(chatId, "Программное обеспечение", "Установка/переустановка ОС");

            case "peripheral" -> clientUpdateHandler.editToSubcategories(callbackQuery, getPeripheralSubcategoriesKeyboard());
            case "peripheral_printer" -> clientUpdateHandler.handleIssue(chatId, "Переферийные устройства", "Подключить/настроить принтер");
            case "peripheral_keyboard" -> clientUpdateHandler.handleIssue(chatId, "Переферийные устройства", "Клавиатура/мышь не работает");
            case "peripheral_screen" -> clientUpdateHandler.handleIssue(chatId, "Переферийные устройства", "Проблема с монитором");

            case "consultation" -> clientUpdateHandler.handleIssue(chatId, "Необходима консультация", "Консультация");

            case "set_image" -> clientUpdateHandler.handleSettingImage(chatId);
            case "send" -> clientUpdateHandler.handleSendingProblem(callbackQuery);

            case "clients_problems" -> adminUpdateHandler.editToClientsProblems(callbackQuery);

            default -> {
                if (callbackData.startsWith("request_")) {
                    adminUpdateHandler.editToSpecialistRequestMenu(callbackQuery);
                } else if (callbackData.startsWith("accept_")) {
                    adminUpdateHandler.acceptSpecialistRequest(callbackQuery);
                } else if (callbackData.startsWith("decline_")) {
                    adminUpdateHandler.declineSpecialistRequest(callbackQuery);
                } else if (callbackData.startsWith("my_problems_")) {
                    clientUpdateHandler.editToMyProblems(callbackQuery);
                } else if (callbackData.startsWith("my_problem_")) {
                    clientUpdateHandler.sendProblem(callbackQuery);
                } else if (callbackData.startsWith("processing_problems_")) {
                    adminUpdateHandler.editToProcessingProblems(callbackQuery);
                } else if (callbackData.startsWith("work_problems_")) {
                    adminUpdateHandler.editToWorkProblems(callbackQuery);
                } else if (callbackData.startsWith("finished_problems_")) {
                    adminUpdateHandler.editToFinishedProblems(callbackQuery);
                } else if (callbackData.startsWith("processing_problem_")) {
                    adminUpdateHandler.sendProcessingProblem(callbackQuery);
                } else if (callbackData.startsWith("set_specialist_")) {
                    adminUpdateHandler.sendChoosingSpecialist(callbackQuery);
                } else if (callbackData.startsWith("problem_specialist_")) {
                    adminUpdateHandler.handleChoosingSpecialist(callbackQuery);
                } else if (callbackData.startsWith("get_problem_")) {
                    clientUpdateHandler.sendProblem(callbackQuery);
                } else if (callbackData.startsWith("work_problem_")) {
                    adminUpdateHandler.sendInWorkProblem(callbackQuery);
                } else if (callbackData.startsWith("finished_problem_")) {
                    adminUpdateHandler.sendFinishedProblem(callbackQuery);
                } else if (callbackData.startsWith("countdown_")) {
                    specialistUpdateHandler.handleAddingCountdown(callbackQuery);
                }
            }
        }
    }
}
