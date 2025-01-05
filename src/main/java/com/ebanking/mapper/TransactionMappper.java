package com.ebanking.mapper;

import com.ebanking.dto.TransactionDto;
import com.ebanking.models.Transaction;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.repository.CurrencyTypeRepository;

import static com.ebanking.mapper.CurrencyTypeMapper.*;
import static com.ebanking.mapper.BankAccountMapper.*;

public class TransactionMappper {
    public static Transaction mapToTransaction(TransactionDto transactionDto, BankAccountRepository bankAccountRepository, CurrencyTypeRepository currencyTypeRepository){
        return  Transaction.builder()
                .id(transactionDto.getId())
                .sender(bankAccountRepository.findByAccountNumEquals((transactionDto.getSender())))
                .amount(Double.valueOf(transactionDto.getAmount()))
                .currencyTypeSender(currencyTypeRepository.findByNameEquals(transactionDto.getCurrencyTypeSender()))
                .receiver(bankAccountRepository.findByAccountNumEquals((transactionDto.getReceiver())))
                .transactionDate(transactionDto.getTransactionDate())
                .build();
    }

    public static TransactionDto mapToTransactionDto(Transaction transaction){
        return  TransactionDto.builder()
                .id(transaction.getId())
                .sender(transaction.getSender().getAccountNum().toString())
                .amount(String.valueOf(transaction.getAmount()))
                .currencyTypeSender(transaction.getCurrencyTypeSender().getName())
                .receiver(transaction.getReceiver().getAccountNum().toString())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
