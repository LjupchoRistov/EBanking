package com.ebanking.service.impl;

import com.ebanking.dto.TransactionDto;
import com.ebanking.exceptions.BankAccountNotFound;
import com.ebanking.mapper.TransactionMapper;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.Transaction;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.repository.TransactionRepository;
import com.ebanking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> findAllByBankAccountNum(String bankAccountNum) {

        List<Transaction> transactions = transactionRepository.findAllBySender_AccountNumOrReceiver_AccountNum(bankAccountNum, bankAccountNum);

        return transactions.stream().map(transactionMapper::toTransactionDTO).toList();
    }

    @Override
    @Transactional
    public String createTransaction(TransactionDto transactionDto) {

        BankAccount senderAcc = getBankAccount(transactionDto.getSender());

        BankAccount receiverAcc = getBankAccount(transactionDto.getReceiver());

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

    private BankAccount getBankAccount(String accountNum) {

        Optional<BankAccount> bankAccountOptional = this.bankAccountRepository.findByAccountNumEquals(accountNum);

        if (bankAccountOptional.isEmpty()) {
            throw new BankAccountNotFound(accountNum);
        }

        return bankAccountOptional.get();
    }
}
