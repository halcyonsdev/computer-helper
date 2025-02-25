package com.halcyon.computer.helper.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

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
                        getStartMenuButtonRow()
                )).build();
    }

    private static InlineKeyboardRow getStartMenuButtonRow() {
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
                        getStartMenuButtonRow()
                )).build();
    }

    public static InlineKeyboardMarkup getClientStartMenu() {
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
                ))
                .build();
    }
}
