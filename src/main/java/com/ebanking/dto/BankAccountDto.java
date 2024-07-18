package com.ebanking.dto;

import com.ebanking.models.CurrencyType;

import java.time.LocalDate;

public record BankAccountDto(

        Long id,

        String accountNum,

        Boolean isDebit,

        Double balance,

        LocalDate dateCreatedOn,

        CurrencyType currencyType,

        String username
) {
}