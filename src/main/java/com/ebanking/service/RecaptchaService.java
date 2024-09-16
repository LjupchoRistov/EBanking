package com.ebanking.service;

public interface RecaptchaService {
    public boolean validateCaptcha(String captchaResponse);
}
