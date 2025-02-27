package com.halcyon.computer.helper.update;

import com.halcyon.computer.helper.entity.Client;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.function.Function;

import static com.halcyon.computer.helper.bot.BotResponse.*;
import static com.halcyon.computer.helper.util.KeyboardUtil.*;

@Component
@RequiredArgsConstructor
public class AdminUpdateHandler {
    private final BotExecutionsService botExecutionsService;
    private final SpecialistService specialistService;
    private final ProblemService problemService;

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
                .replyMarkup(getSpecialistsRequestsKeyboard(specialistsRequests, "request_"))
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

    public void editToClientsProblems(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();

        var clientsProblems = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(CLIENTS_PROBLEMS_MESSAGE)
                .replyMarkup(getClientProblemsKeyboard())
                .build();
        clientsProblems.enableMarkdown(true);

        botExecutionsService.editMessage(clientsProblems);
    }

    public void editToProcessingProblems(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        List<Problem> processingProblems = problemService.getProcessingClientProblems();
        int el = Integer.parseInt(callbackQuery.getData().split("_")[2]);

        var processingProblemsMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(PROCESSING_PROBLEMS_MESSAGE)
                .replyMarkup(getProblemsKeyboard(processingProblems, el, "processing_problems_", "processing_problem_"))
                .build();
        processingProblemsMessage.enableMarkdown(true);

        botExecutionsService.editMessage(processingProblemsMessage);
    }

    public void editToWorkProblems(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        List<Problem> workProblems = problemService.getWorkClientProblems();
        int el = Integer.parseInt(callbackQuery.getData().split("_")[2]);

        var workProblemsMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(WORK_PROBLEMS_MESSAGE)
                .replyMarkup(getProblemsKeyboard(workProblems, el, "work_problems_", "work_problem_"))
                .build();
        workProblemsMessage.enableMarkdown(true);

        botExecutionsService.editMessage(workProblemsMessage);
    }

    public void editToFinishedProblems(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        List<Problem> finishedProblems = problemService.getFinishedClientProblems();
        int el = Integer.parseInt(callbackQuery.getData().split("_")[2]);

        var finishedProblemsMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(FINISHED_PROBLEMS_MESSAGE)
                .replyMarkup(getProblemsKeyboard(finishedProblems, el, "finished_problems_", "finished_problem_"))
                .build();
        finishedProblemsMessage.enableMarkdown(true);

        botExecutionsService.editMessage(finishedProblemsMessage);
    }

    public void sendProblemDetails(CallbackQuery callbackQuery, Function<Problem, String> infoProvider, InlineKeyboardMarkup keyboard) {
        long chatId = callbackQuery.getMessage().getChatId();
        long problemId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        Problem problem = problemService.findById(problemId);

        sendProblemMessage(problem, chatId, infoProvider, keyboard);
    }

    private void sendProblemMessage(Problem problem, long chatId, Function<Problem, String> infoProvider, InlineKeyboardMarkup keyboard) {
        if (problem.getFileId() != null) {
            var message = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(problem.getFileId()))
                    .caption(infoProvider.apply(problem))
                    .replyMarkup(keyboard)
                    .parseMode("HTML")
                    .build();

            botExecutionsService.sendPhoto(message);
        } else {
            var message = SendMessage.builder()
                    .chatId(chatId)
                    .text(infoProvider.apply(problem))
                    .replyMarkup(keyboard)
                    .build();
            message.enableHtml(true);

            botExecutionsService.sendMessage(message);
        }
    }

    public void sendProcessingProblem(CallbackQuery callbackQuery) {
        sendProblemDetails(callbackQuery, this::getProcessingProblemInfo, getProcessingProblemKeyboard(extractProblemId(callbackQuery)));
    }

    private String getProcessingProblemInfo(Problem problem) {
        return String.format(
                PROBLEM_INFO,
                problem.getCategory(),
                problem.getSubcategory(),
                problem.getDescription(),
                problem.getStatus()
        );
    }

    private long extractProblemId(CallbackQuery callbackQuery) {
        return Long.parseLong(callbackQuery.getData().split("_")[2]);
    }

    public void sendInWorkProblem(CallbackQuery callbackQuery) {
        sendProblemDetails(callbackQuery, this::getInWorkOrFinishedProblemInfo, getAdminMenuKeyboard());
    }

    private String getInWorkOrFinishedProblemInfo(Problem problem) {
        Specialist specialist = problem.getSpecialist();

        return String.format(
                IN_WORK_FINISHED_PROBLEM_MESSAGE,
                problem.getCategory(),
                problem.getSubcategory(),
                problem.getDescription(),
                problem.getStatus(),
                specialist.getFullName(),
                specialist.getPhone(),
                specialist.getEmail()
        );
    }

    public void sendFinishedProblem(CallbackQuery callbackQuery) {
        sendProblemDetails(callbackQuery, this::getInWorkOrFinishedProblemInfo, getFinishedProblemKeyboard(extractProblemId(callbackQuery)));
    }

    public void sendChoosingSpecialist(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        long problemId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        List<Specialist> specialists = specialistService.getSpecialists();

        var specialistsMessage = SendMessage.builder()
                .chatId(chatId)
                .text(CHOOSE_SPECIALIST_MESSAGE)
                .replyMarkup(getSpecialistsRequestsKeyboard(specialists, "problem_specialist_" + problemId + "_"))
                .build();
        specialistsMessage.enableMarkdown(true);

        botExecutionsService.sendMessage(specialistsMessage);
    }

    public void handleChoosingSpecialist(CallbackQuery callbackQuery) {
        long problemId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        long specialistChatId = Long.parseLong(callbackQuery.getData().split("_")[3]);

        Problem problem = problemService.setSpecialist(problemId, specialistChatId);
        Specialist specialist = problem.getSpecialist();
        Client client = problem.getClient();

        var toClientMessage = SendMessage.builder()
                .chatId(client.getChatId())
                .text(getSpecialistToClientText(specialist))
                .replyMarkup(getProblemClientNotificationKeyboard(problemId))
                .build();
        toClientMessage.enableHtml(true);

        var toSpecialistMessage = SendMessage.builder()
                .chatId(specialistChatId)
                .text(getProblemToSpecialistText(client))
                .replyMarkup(getProblemSpecialistNotificationKeyboard(problemId))
                .build();
        toSpecialistMessage.enableHtml(true);

        botExecutionsService.sendMessage(toClientMessage);
        botExecutionsService.sendMessage(toSpecialistMessage);
        editToStartMessage(callbackQuery);
    }

    private String getSpecialistToClientText(Specialist specialist) {
        return String.format(
                CLIENT_SPECIALIST_MESSAGE,
                specialist.getFullName(),
                specialist.getPhone(),
                specialist.getEmail()
        );
    }

    private String getProblemToSpecialistText(Client client) {
        return String.format(
                SPECIALIST_PROBLEM_MESSAGE,
                client.getFullName(),
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );
    }
}
