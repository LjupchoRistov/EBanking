package com.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountRegistrationForm {

    private String currencyType;

    private String pin;

    private String repeatPin;
}
