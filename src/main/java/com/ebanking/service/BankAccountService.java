package com.ebanking.service;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;

import java.util.List;

public interface BankAccountService {

    Integer MAX_BANK_ACCOUNTS = 5;

    List<BankAccountDto> findBankAccountsByUser(UserEntity user);

    List<BankAccountDto> findBankAccountsByUsername(String username);

    void createBankAccount(CurrencyType currencyType, Integer pin, String username);

    BankAccount deleteBankAccount(Long id);

    BankAccountDto findBankAccountByNumber(String sender);

    Integer activeBankAccounts(String username);

    Integer availableBankAccounts(UserEntity user);
}
