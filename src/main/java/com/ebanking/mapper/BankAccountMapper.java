package com.ebanking.mapper;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.models.BankAccount;

import static com.ebanking.mapper.CurrencyTypeMapper.*;

public class BankAccountMapper {
    public static BankAccount mapToBankAccount(BankAccountDto bankAccountDto){
        return BankAccount.builder()
                .id(bankAccountDto.getId())
                .accountNum(bankAccountDto.getAccountNum())
                .isDebit(bankAccountDto.getIsDebit())
                .balance(bankAccountDto.getBalance())
                .currencyType(mapToCurrencyType(bankAccountDto.getCurrencyTypeDto()))
                .dateCreatedOn(bankAccountDto.getDateCreatedOn())
                .user(bankAccountDto.getUser())
                .build();
    }

    public static BankAccountDto mapToBankAccountDto(BankAccount bankAccount){
        return BankAccountDto.builder()
                .id(bankAccount.getId())
                .accountNum(bankAccount.getAccountNum())
                .isDebit(bankAccount.getIsDebit())
                .balance(bankAccount.getBalance())
                .currencyTypeDto(mapToCurrencyTypeDto(bankAccount.getCurrencyType()))
                .dateCreatedOn(bankAccount.getDateCreatedOn())
                .user(bankAccount.getUser())
                .build();
    }
}
