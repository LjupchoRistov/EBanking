package com.ebanking.service.impl;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.mapper.BankAccountMapper;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.repository.CurrencyTypeRepository;
import com.ebanking.repository.TransactionRepository;
import com.ebanking.repository.UserRepository;
import com.ebanking.service.BankAccountService;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributeIdentifierException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.ebanking.mapper.BankAccountMapper.*;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
private final CurrencyTypeRepository currencyTypeRepository;
    private static final AtomicLong COUNTER = new AtomicLong(0);

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, UserRepository userRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Override
    public List<BankAccountDto> findBankAccountsByUser(UserEntity user) {
        List<BankAccount> bankAccounts = this.bankAccountRepository.findAllByUserEquals(user);

        return bankAccounts.stream().map(BankAccountMapper::mapToBankAccountDto).collect(Collectors.toList());
    }

    @Override
    public BankAccountDto findBankAccountById(Long id) {
        return this.bankAccountRepository.findById(id).map(BankAccountMapper::mapToBankAccountDto).get();
    }

    @Override
    public BankAccount createBankAccount(String currency, UserEntity user) {
        // todo: generate account number
        String accountNum = generateAccountNumber(currency);
        CurrencyType currencyType = currencyTypeRepository.findByNameEquals(currency);
        BankAccount bankAccount = new BankAccount();
        Boolean isDebit = true;
        if (!currencyType.getName().equals("Macedonian Denar")) {
            isDebit = false;
        }

        Double balance=0.0;

        LocalDateTime dateCreatedOn=LocalDateTime.now();

        bankAccount.setDateCreatedOn(dateCreatedOn);
        bankAccount.setBalance(balance);
        bankAccount.setIsDebit(isDebit);
        bankAccount.setAccountNum(accountNum);
        bankAccount.setCurrencyType(currencyType);
        bankAccount.setUser(user);
        // Set other fields as necessary

        // Save the bank account (assuming you have a repository or DAO)
        // bankAccountRepository.save(bankAccountDto);

        return bankAccountRepository.save(bankAccount);

    }
    private String generateAccountNumber(String currency) {
        String prefix;
        switch (currency) {
            case "Macedonian Denar":
                prefix = "MK";
                break;
            case "Euro":
                prefix = "EU";
                break;
            case "United States Dollar":
                prefix = "US";
                break;
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currency);
        }

        StringBuilder accountNum = new StringBuilder(prefix);
        for (int i = 0; i < 10; i++) {
            int digit = (int) Math.floor(Math.random() * 10);
            accountNum.append(digit);
        }

        return accountNum.toString();
    }
    @Override
    public Integer activeBankAccounts(UserEntity user){
        //todo: change search with (EMBG)
        return bankAccountRepository.findAllByUserEquals(user).size();
    }

    @Override
    public Integer availableBankAccounts(UserEntity user){
        //todo: change search with (EMBG)
        return MAX_BANK_ACCOUNTS - bankAccountRepository.findAllByUserEquals(user).size();
    }

    @Override
    public BankAccountDto findBankAccountByNumber(String sender) {
        return mapToBankAccountDto(this.bankAccountRepository.findByAccountNumEquals(sender));
    }
  public BankAccount deleteBankAccount(Long id){
    BankAccount bankAccount= bankAccountRepository.findById(id).orElseThrow();
    bankAccountRepository.deleteById(id);
    return bankAccount;
  }
}
