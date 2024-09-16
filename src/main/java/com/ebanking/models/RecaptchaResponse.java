package com.ebanking.models;

import lombok.Data;

@Data
public class RecaptchaResponse {
    private boolean success;
    private String challenge_ta;
    private String hostname;

}
