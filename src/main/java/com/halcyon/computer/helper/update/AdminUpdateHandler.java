package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.entity.Specialist;
import com.halcyon.computer.helper.service.BotExecutionsService;
import com.halcyon.computer.helper.service.SpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static com.halcyon.computer.helper.bot.BotResponse.*;
import static com.halcyon.computer.helper.util.KeyboardUtil.*;

@Component
@RequiredArgsConstructor
public class AdminUpdateHandler {
    private final BotExecutionsService botExecutionsService;
    private final SpecialistService specialistService;

    public void sendStartMessage(long chatId) {
        var adminMenuMessage = SendMessage.builder()
                .chatId(chatId)
                .text(ADMIN_START_MESSAGE)
                .replyMarkup(getAdminMenuKeyboard())
                .build();
        adminMenuMessage.enableMarkdown(true);

        botExecutionsService.sendMessage(adminMenuMessage);
    }

    public void editToStartMessage(CallbackQuery callbackQuery) {
        var adminMenuMessage = EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(ADMIN_START_MESSAGE)
                .replyMarkup(getAdminMenuKeyboard())
                .build();
        adminMenuMessage.enableMarkdown(true);

        botExecutionsService.editMessage(adminMenuMessage);
    }

    public void editToSpecialistsRequests(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        List<Specialist> specialistsRequests = specialistService.getActiveSpecialistsRequests();

        var specialistsRequestsMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(SPECIALISTS_REQUESTS_MESSAGE)
                .replyMarkup(getSpecialistsRequestsKeyboard(specialistsRequests))
                .build();
        specialistsRequestsMessage.enableMarkdown(true);

        botExecutionsService.editMessage(specialistsRequestsMessage);
    }

    public void editToSpecialistRequestMenu(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        long specialistChatId = Long.parseLong(callbackQuery.getData().split("_")[1]);
        Specialist specialist = specialistService.findByChatId(specialistChatId);

        var specialistRequestMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(getSpecialistRequestText(specialist))
                .replyMarkup(getSpecialistRequestMenu(specialistChatId))
                .build();
        specialistRequestMessage.enableHtml(true);

        botExecutionsService.editMessage(specialistRequestMessage);
    }

    private String getSpecialistRequestText(Specialist specialist) {
        return String.format(
                ADMIN_SPECIALIST_REQUEST_MESSAGE,
                specialist.getFullName(),
                specialist.getPhone(),
                specialist.getEmail()
        );
    }

    public void acceptSpecialistRequest(CallbackQuery callbackQuery) {
        long specialistChatId = Long.parseLong(callbackQuery.getData().split("_")[1]);
        specialistService.acceptRequest(specialistChatId);

        editToStartMessage(callbackQuery);
        botExecutionsService.sendDefaultMessage(specialistChatId, ACCEPTED_REQUEST_MESSAGE);
    }

    public void declineSpecialistRequest(CallbackQuery callbackQuery) {
        long specialistChatId = Long.parseLong(callbackQuery.getData().split("_")[1]);
        specialistService.declineRequest(specialistChatId);

        editToStartMessage(callbackQuery);
        botExecutionsService.sendDefaultMessage(specialistChatId, DECLINED_REQUEST_MESSAGE);
    }
}
