package com.ebanking.service;

public interface RecaptchaService {

    boolean validateCaptcha(String captchaResponse);
}
