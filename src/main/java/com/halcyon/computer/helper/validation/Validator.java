package com.halcyon.computer.helper.validation;

import java.util.regex.Pattern;


public class Validator {
    private Validator() {}

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+?7|8)\\d{10}$"
    );

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
