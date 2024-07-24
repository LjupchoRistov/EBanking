package com.ebanking.service;

import com.ebanking.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> findAllByBankAccountNum(String bankAccountNum);

    String createTransaction(TransactionDto transactionDto);
}
