package com.ebanking.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$";

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}