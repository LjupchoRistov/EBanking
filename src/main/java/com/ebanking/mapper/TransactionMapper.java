package com.ebanking.mapper;

import com.ebanking.dto.TransactionDto;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionMapper {

    public TransactionDto toTransactionDTO(Transaction transaction) {

        Long id = transaction.getId();

        String description = transaction.getDescription();

        BankAccount bankAccountNumberSender = transaction.getSender();

        String sender = bankAccountNumberSender.getAccountNum();

        BankAccount bankAccountNumberReceiver = transaction.getReceiver();

        String receiver = bankAccountNumberReceiver.getAccountNum();

        Double amount = transaction.getAmount();

        CurrencyType currency = transaction.getCurrencyTypeSender();

        String currencyType = currency.getName();

        LocalDate transactionDate = transaction.getTransactionDate();

        return new TransactionDto(id, description, sender, receiver, amount.toString(), currencyType, transactionDate);
    }
}
