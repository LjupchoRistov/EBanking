package com.ebanking.mapper;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BankAccountMapper {
    public static BankAccount mapToBankAccount(BankAccountDto bankAccountDto){

        Long id = bankAccountDto.id();
        String accountNum = bankAccountDto.accountNum();
        Boolean isDebit = bankAccountDto.isDebit();
        Double balance = bankAccountDto.balance();
        LocalDate dateCreatedOn = bankAccountDto.dateCreatedOn();
        CurrencyType currencyType = bankAccountDto.currencyType();

        return new BankAccount(id, isDebit, balance, accountNum, dateCreatedOn, currencyType);
    }

    public static BankAccountDto mapToBankAccountDto(BankAccount bankAccount){

        Long id = bankAccount.getId();
        String accountNum = bankAccount.getAccountNum();
        Boolean isDebit = bankAccount.getIsDebit();
        Double balance = bankAccount.getBalance();
        LocalDate dateCreatedOn = bankAccount.getDateCreatedOn();
        CurrencyType currencyType = bankAccount.getCurrencyType();
        UserEntity user = bankAccount.getUser();
        String username = user.getUsername();

        return new BankAccountDto(id, accountNum, isDebit, balance, dateCreatedOn, currencyType, username);
    }
}
