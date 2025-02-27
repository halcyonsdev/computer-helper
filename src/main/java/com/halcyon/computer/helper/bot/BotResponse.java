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

    public static final String PROBLEM_MESSAGE = "Какая у Вас <u>проблема?</u>";
    public static final String SUBCATEGORY_MESSAGE = "Выберите вашу <u>проблему</u>:";

    public static final String CLIENT_PROFILE_MESSAGE = """
            👤 Ваш профиль:
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            <u>Адрес для выезда</u>: %s
            """;

    public static final String SPECIALIST_REQUEST_MESSAGE = """
            Ваша заявка отправлена на обработку:
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            """;

    public static final String WAITING_REQUEST_MESSAGE = "✅ *Ваша заявка передана администратору, ожидайте подтверждения*";

    public static final String ADMIN_START_MESSAGE = "Здравствуйте! Вы являетесь администратором. *Выберите ниже нужную Вам функцию.*";
    public static final String SPECIALISTS_REQUESTS_MESSAGE = "*Активные заявки на специалиста*";

    public static final String ADMIN_SPECIALIST_REQUEST_MESSAGE = """
            Заявка на специалиста:
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            """;

    public static final String ACCEPTED_REQUEST_MESSAGE = "✅ <b>Ваша заявка была одобрена администратором!</b>";
    public static final String DECLINED_REQUEST_MESSAGE = "❌ <b>Ваша заявка была отклонена администратором!</b>";

    public static final String DESCRIBE_PROBLEM = "Максимально подробно опишите Вашу проблему.";

    public static final String PROBLEM_INFO = """
            Информация о проблеме:
            
            <u>Категория</u>: %s
            <u>Подкатегория</u>: %s
            <u>Описание</u>: %s
            <u>Статус</u>: %s
            """;

    public static final String SEND_IMAGE_MESSAGE = "Отправьте фотографию с демонстрацией вашей проблемы";
    public static final String MY_PROBLEMS_MESSAGE = "*Мои проблемы*";
    public static final String CLIENTS_PROBLEMS_MESSAGE = "*Проблемы клиентов*";
    public static final String PROCESSING_PROBLEMS_MESSAGE = "*Проблемы, ожидающие обработки*";
    public static final String WORK_PROBLEMS_MESSAGE = "*Проблемы, которые решаются на данный момент*";
    public static final String FINISHED_PROBLEMS_MESSAGE = "*Проблемы, которые были решены*";
    public static final String CHOOSE_SPECIALIST_MESSAGE = "*Какого специалиста вы хотите назначить на эту проблему?*";
    public static final String CLIENT_SPECIALIST_MESSAGE = """
            <b>К вам приедет специалист:</b>
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            """;

    public static final String SPECIALIST_PROBLEM_MESSAGE = """
            <b>Вам назначена проблема от:</b>
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            <u>Адрес для выезда</u>: %s
            """;

    public static final String IN_WORK_FINISHED_PROBLEM_MESSAGE = """
            Информация о проблеме:
            
            <u>Категория</u>: %s
            <u>Подкатегория</u>: %s
            <u>Описание</u>: %s
            <u>Статус</u>: %s
            
            <b>Специалист:</b>
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            """;

    public static final String SPECIALIST_CLOSED_PROBLEM_MESSAGE = "*Проблема закрыта.*";
    public static final String CLIENT_CLOSED_PROBLEM_MESSAGE = """
            <b>Проблема закрыта:</b>
            
            <u>Категория</u>: %s
            <u>Подкатегория</u>: %s
            <u>Описание</u>: %s
            <u>Статус</u>: %s
            
            <b>Специалист:</b>
            
            <u>ФИО</u>: %s
            <u>Телефон</u>: %s
            <u>Email</u>: %s
            """;

    public static final String ADD_COUNTDOWN_MESSAGE = "Напишите <u>отсчет</u> по выполненной работе:";
    public static final String ADD_REVIEW_MESSAGE = "Поделитесь вашими впечатлениями от работы специалиста:";
    public static final String CREATE_COUNTDOWN_MESSAGE = """
            <u>Отсчет</u>:
            
            %s
            """;
}
