package com.halcyon.computer.helper.bot;

public class BotResponse {
    private BotResponse() {}

    public static final String REGISTRATION_START_MESSAGE = "*Здравствуйте! Кем Вы являетесь?*";
    public static final String CLIENT_START_MESSAGE = "*Здравствуйте, %s!*";
    public static final String ENTER_NAME_MESSAGE = "Введите Ваше <u>ФИО</u>:";
    public static final String ENTER_PHONE_MESSAGE = "Ваш <u>телефон</u>:";
    public static final String ENTER_EMAIL_MESSAGE = "Ваш <u>email</u>:";
    public static final String ENTER_CLIENT_ADDRESS_MESSAGE = "Ваш <u>адрес для выезда:</u>";
    public static final String ENTER_CORRECT_PHONE_MESSAGE = "Введите корректный <u>номер телефона</u>:";
    public static final String ENTER_CORRECT_EMAIL_MESSAGE = "Введите корректный <u>email</u>:";

    public static final String PROBLEM_MESSAGE = "Здравствуйте! Какая у Вас <u>проблема?</u>";

    public static final String CLIENT_PROFILE_MESSAGE = """
            👤 Ваш профиль:
            
            <u>ФИО</u>: %s
            <u>телефон</u>: %s
            <u>email</u>: %s
            <u>адрес для выезда</u>: %s
            """;
}
