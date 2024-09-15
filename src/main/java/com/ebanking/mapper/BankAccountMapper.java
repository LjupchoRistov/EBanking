package com.ebanking.mapper;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BankAccountMapper {

    public static BankAccountDto mapToBankAccountDto(BankAccount bankAccount) {

        String accountNum = bankAccount.getAccountNum();
        Boolean isDebit = bankAccount.getIsDebit();
        Double balance = bankAccount.getBalance();
        LocalDate dateCreatedOn = bankAccount.getDateCreatedOn();
        CurrencyType currencyType = bankAccount.getCurrencyType();
        UserEntity user = bankAccount.getUser();
        String username = user.getUsername();

        return new BankAccountDto(accountNum, isDebit, balance, dateCreatedOn, currencyType, username);
    }
}
