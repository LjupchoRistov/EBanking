package com.ebanking.dto;

import com.ebanking.models.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto{

    String accountNum;

    Boolean isDebit;

    Double balance;

    LocalDate dateCreatedOn;

    CurrencyType currencyType;

    String username;
}