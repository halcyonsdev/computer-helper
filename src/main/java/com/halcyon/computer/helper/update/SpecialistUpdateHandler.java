package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.cache.CacheManager;
import com.halcyon.computer.helper.cache.ChatStatus;
import com.halcyon.computer.helper.cache.ChatStatusType;
import com.halcyon.computer.helper.entity.Client;
import com.halcyon.computer.helper.entity.Countdown;
import com.halcyon.computer.helper.entity.Problem;
import com.halcyon.computer.helper.entity.Specialist;
import com.halcyon.computer.helper.service.BotExecutionsService;
import com.halcyon.computer.helper.service.ProblemService;
import com.halcyon.computer.helper.service.SpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static com.halcyon.computer.helper.bot.BotResponse.*;
import static com.halcyon.computer.helper.util.KeyboardUtil.*;
import static com.halcyon.computer.helper.validation.Validator.isValidEmail;
import static com.halcyon.computer.helper.validation.Validator.isValidPhone;

@Component
@RequiredArgsConstructor
public class SpecialistUpdateHandler {
    private final BotExecutionsService botExecutionsService;
    private final CacheManager cacheManager;
    private final SpecialistService specialistService;
    private final ProblemService problemService;

    public void editToStartMenu(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();

        if (specialistService.isRequest(chatId)) {
            var waitingRequestMessage = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(WAITING_REQUEST_MESSAGE)
                    .build();
            waitingRequestMessage.enableMarkdown(true);

            botExecutionsService.editMessage(waitingRequestMessage);
        }
    }

    public void handleRegistration(long chatId) {
        botExecutionsService.sendDefaultMessage(chatId, ENTER_NAME_MESSAGE);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.SPECIALIST_FULL_NAME));
    }

    public void handleCreateFullName(Message message) {
        processCreateStep(message, ENTER_PHONE_MESSAGE, ChatStatusType.SPECIALIST_PHONE, List.of(message.getText()));
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

        processCreateStep(message, ENTER_EMAIL_MESSAGE, ChatStatusType.SPECIALIST_EMAIL, List.of(chatStatus.getData().get(0), message.getText()));
    }

    public void handleCreateEmail(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();

        if (!isValidEmail(message.getText())) {
            botExecutionsService.sendDefaultMessage(chatId, ENTER_CORRECT_EMAIL_MESSAGE);
            return;
        }

        String fullName = chatStatus.getData().get(0);
        String phone = chatStatus.getData().get(1);
        String email = message.getText();

        var specialistRequest = Specialist.builder()
                .chatId(chatId)
                .fullName(fullName)
                .phone(phone)
                .email(email)
                .isRequest(true)
                .build();

        specialistService.save(specialistRequest);
        cacheManager.remove(String.valueOf(chatId));

        var specialistRequestMessage = SendMessage.builder()
                .chatId(chatId)
                .text(getSpecialistText(specialistRequest))
                .replyMarkup(getSpecialistStartMenuKeyboard())
                .build();
        specialistRequestMessage.enableHtml(true);

        botExecutionsService.sendMessage(specialistRequestMessage);
    }

    private String getSpecialistText(Specialist specialist) {
        return String.format(
                SPECIALIST_REQUEST_MESSAGE,
                specialist.getFullName(),
                specialist.getPhone(),
                specialist.getEmail()
        );
    }

    public void handleAddingCountdown(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();

        botExecutionsService.sendDefaultMessage(chatId, ADD_COUNTDOWN_MESSAGE);
        cacheManager.save(String.valueOf(chatId), new ChatStatus(ChatStatusType.COUNTDOWN_CONTENT));
    }

    public void handleCountdownContent(Message message, ChatStatus chatStatus) {
        long chatId = message.getChatId();
        long problemId = Long.parseLong(chatStatus.getData().get(0));

        Problem problem = problemService.findById(problemId);

        var countdown = Countdown.builder()
                .content(message.getText())
                .problem(problem)
                .build();

        cacheManager.save("countdown:" + problem, countdown);
        sendCreateCountdown(countdown, chatId);
    }

    private void sendCreateCountdown(Countdown countdown, long chatId) {
        long problemId = countdown.getProblem().getId();

        if (countdown.getFileId() != null) {
            var countdownMessage = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(countdown.getFileId()))
                    .caption(String.format(CREATE_COUNTDOWN_MESSAGE, countdown.getContent()))
                    .parseMode("HTML")
                    .replyMarkup(getSpecialistCountdownKeyboard(problemId))
                    .build();

            botExecutionsService.sendPhoto(countdownMessage);
            return;
        }

        var countdownMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.format(CREATE_COUNTDOWN_MESSAGE, countdown.getContent()))
                .replyMarkup(getSpecialistCountdownKeyboard(problemId))
                .build();
        countdownMessage.enableHtml(true);

        botExecutionsService.sendMessage(countdownMessage);
    }

    public void handleUpdatingCountdownContent() {

    }
}
