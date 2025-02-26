package com.halcyon.computer.helper.util;

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

    private static InlineKeyboardRow getClientStartMenuButtonRow() {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("\uD83D\uDDC2️ Меню")
                .callbackData("client_start")
                .build());
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
}
