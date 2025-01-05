package com.ebanking.service.impl;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.dto.TransactionDto;
import com.ebanking.mapper.TransactionMappper;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.Transaction;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.repository.CurrencyTypeRepository;
import com.ebanking.repository.TransactionRepository;
import com.ebanking.service.TransactionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ebanking.mapper.BankAccountMapper.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyTypeRepository currencyTypeRepository;

    public TransactionServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Override
    public List<TransactionDto> findAllByBankAccount(BankAccountDto bankAccountDto) {
        List<Transaction> sender = this.transactionRepository.findAllBySenderEquals(mapToBankAccount(bankAccountDto));
        List<Transaction> receiver = this.transactionRepository.findAllByReceiverEquals(mapToBankAccount(bankAccountDto));

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(sender);
        allTransactions.addAll(receiver);

        return allTransactions.stream().map(TransactionMappper::mapToTransactionDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createTransaction(TransactionDto transactionDto) {
        BankAccount senderAcc = this.bankAccountRepository.findByAccountNumEquals((transactionDto.getSender()));
        BankAccount receiverAcc = this.bankAccountRepository.findByAccountNumEquals((transactionDto.getReceiver()));

        CurrencyType currencyTypeSender = senderAcc.getCurrencyType();
        CurrencyType currencyTypeReceiver = receiverAcc.getCurrencyType();

        // Calculate conversion rate
        double amountDouble = Double.parseDouble(transactionDto.getAmount());
        double rate = currencyTypeSender.getValue() / currencyTypeReceiver.getValue();
        Double convertedAmount = amountDouble * rate;

        // Deduct amount from sender's balance
        if (!senderAcc.canSubstractAmount(convertedAmount)) {
            //Dont allow transaction if balance below amount
            return "Not enough balance to send!";
        }
        senderAcc.substractAmount(convertedAmount);

        // Add amount to receiver's balance
        receiverAcc.addAmount(convertedAmount);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setSender(senderAcc);
        transaction.setReceiver(receiverAcc);
        transaction.setCurrencyTypeSender(senderAcc.getCurrencyType());
        transaction.setAmount(amountDouble);
        transaction.setDescription(transactionDto.getDescription());

        //Save transaction to database
        this.transactionRepository.save(transaction);

        return "Success";
    }
}
