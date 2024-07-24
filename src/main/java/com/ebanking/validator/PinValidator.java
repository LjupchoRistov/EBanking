package com.ebanking.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PinValidator {

    private static final String PIN_REGEX = "^(?!(\\d)\\1{5})(?!012|123|234|345|456|567|678|789|890)(?!.*(.{3})\\1)[0-9]{6}$";

    public boolean validatePin(String pin) {

        Pattern pattern = Pattern.compile(PIN_REGEX);

        Matcher matcher = pattern.matcher(pin);

        return matcher.matches();
    }
}
