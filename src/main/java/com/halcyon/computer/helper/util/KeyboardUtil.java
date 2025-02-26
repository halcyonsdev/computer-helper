package com.halcyon.computer.helper.util;

import com.halcyon.computer.helper.entity.Problem;
import com.halcyon.computer.helper.entity.Specialist;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {
    private KeyboardUtil() {}

    public static InlineKeyboardMarkup getRegistrationKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDC64 Я клиент")
                                .callbackData("client_register")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCBB Я специалист")
                                .callbackData("specialist_register")
                                .build())
                )).build();
    }

    public static InlineKeyboardMarkup getClientProfileKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCD1 Изменить")
                                .callbackData("update_client")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("❌ Удалить")
                                .callbackData("delete_client")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardRow getClientStartMenuButtonRow() {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("\uD83D\uDDC2️ Меню")
                .callbackData("client_start")
                .build());
    }

    public static InlineKeyboardMarkup getClientToStartKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDC2️ Меню")
                                .callbackData("start_client_start")
                                .build())
                )).build();
    }

    public static InlineKeyboardMarkup getUpdateClientKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83E\uDEAA  Изменить ФИО")
                                .callbackData("update_client_name")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCF1 Изменить телефон")
                                .callbackData("update_client_phone")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCE7 Изменить email")
                                .callbackData("update_client_email")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCEB Изменить адрес")
                                .callbackData("update_client_address")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getClientStartMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83C\uDD98 Проблема")
                                .callbackData("client_problem")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDC64 Профиль")
                                .callbackData("client_profile")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("❓ Мои проблемы")
                                .callbackData("my_problems_0")
                                .build())
                )).build();
    }

    public static InlineKeyboardMarkup getSpecialistStartMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(getSpecialistStartMenuButtonRow()))
                .build();
    }

    private static InlineKeyboardRow getSpecialistStartMenuButtonRow() {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("\uD83D\uDDC2️ Меню")
                .callbackData("specialist_start")
                .build());
    }

    public static InlineKeyboardMarkup getAdminMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCC4 Заявки")
                                .callbackData("requests")
                                .build())
                )).build();
    }

    public static InlineKeyboardMarkup getSpecialistsRequestsKeyboard(List<Specialist> specialistRequests) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();

        for (var specialistRequest : specialistRequests) {
            var specialistRequestButton = InlineKeyboardButton.builder()
                    .text(specialistRequest.getFullName())
                    .callbackData("request_" + specialistRequest.getChatId())
                    .build();

            keyboard.add(new InlineKeyboardRow(specialistRequestButton));
        }

        keyboard.add(getAdminMenuButtonRow());

        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardRow getAdminMenuButtonRow() {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("\uD83D\uDDC2️ Меню")
                .callbackData("admin_start")
                .build());
    }

    public static InlineKeyboardMarkup getSpecialistRequestMenu(long specialistChatId) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("✅ Принять")
                                .callbackData("accept_" + specialistChatId)
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("❌ Отказать")
                                .callbackData("decline_" + specialistChatId)
                                .build()),
                        getAdminMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getProblemCategoriesKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCBB Компьютер и ноутбук")
                                .callbackData("computer")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCBE Программное обеспечение")
                                .callbackData("software")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("⌨️ Переферийные устройства")
                                .callbackData("peripheral")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDE3️ Необходима консультация")
                                .callbackData("consultation")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getComputerSubcategoriesKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDEAB Не включается")
                                .callbackData("pc_turn")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCC9 Медленно работает")
                                .callbackData("pc_slow")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDD04 Зависает")
                                .callbackData("pc_hangs")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getSoftwareSubcategoriesKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDCBD Помощь с установкой программ")
                                .callbackData("software_download")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83E\uDDA0 Проверить/почистить от вирусов")
                                .callbackData("software_virus")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDA5️ Не запускается/вылетает программа")
                                .callbackData("software_crashes")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83E\uDE9F Установка/переустановка ОС")
                                .callbackData("software_oc")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getPeripheralSubcategoriesKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDA8️ Подключить/настроить принтер")
                                .callbackData("peripheral_printer")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDB1️ Клавиатура/мышь не работает")
                                .callbackData("peripheral_keyboard")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDA5️ Проблема с монитором")
                                .callbackData("peripheral_screen")
                                .build()),
                        getClientStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getProblemMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("\uD83D\uDDBC Прикрепить фото")
                                .callbackData("set_image")
                                .build()),
                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                .text("⬆️ Отправить")
                                .callbackData("send")
                                .build())
                )).build();
    }

    public static InlineKeyboardMarkup getMyProblemsKeyboard(List<Problem> problems, int el) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();

        for (int i = el; i < Math.min(problems.size(), el + 5); i++) {
            Problem problem = problems.get(i);

            var problemButton = InlineKeyboardButton.builder()
                    .text(problem.getCategory())
                    .callbackData("my_problem_" + problem.getId())
                    .build();

            keyboard.add(new InlineKeyboardRow(problemButton));
        }

        var leftButton = InlineKeyboardButton.builder()
                .text("◀️")
                .callbackData("my_problems_" + (Math.max(el - 5, 0)))
                .build();

        var rightButton = InlineKeyboardButton.builder()
                .text("▶️")
                .callbackData("my_problems_" + (el + 5 >= problems.size() ? 0 : el + 5))
                .build();

        keyboard.add(new InlineKeyboardRow(leftButton, rightButton));
        keyboard.add(getClientStartMenuButtonRow());

        return new InlineKeyboardMarkup(keyboard);
    }
}
