package com.ebanking.service;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findAllByBankAccount(BankAccountDto bankAccountDto);
    String createTransaction(TransactionDto transactionDto);
}
