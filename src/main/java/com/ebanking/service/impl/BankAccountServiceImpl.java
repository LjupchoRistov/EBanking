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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ebanking.mapper.BankAccountMapper.*;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final CurrencyTypeRepository currencyTypeRepository;
    private static final AtomicLong COUNTER = new AtomicLong(0);

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, UserRepository userRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    private final TransactionRepository transactionRepository;

    @Override
    public List<BankAccountDto> findBankAccountsByUsername(String username) {

        UserEntity user = getUser(username);

        List<BankAccount> bankAccounts = this.bankAccountRepository.findAllByUserUsername(user.getUsername());

        return bankAccounts.stream().map(BankAccountMapper::mapToBankAccountDto).toList();
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

    @Transactional
    @Override
    public void deleteBankAccount(String bankAccountNumber) {
        bankAccountRepository.existsById(bankAccountNumber);

        transactionRepository.deleteAllByReceiver_AccountNum(bankAccountNumber);
        transactionRepository.deleteAllBySender_AccountNum(bankAccountNumber);
        bankAccountRepository.deleteById(bankAccountNumber);
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
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccountNumEquals(sender);

        if (bankAccountOptional.isEmpty()) {
            throw new BankAccountNotFound(sender);
        }

        BankAccount bankAccount = bankAccountOptional.get();

        return mapToBankAccountDto(bankAccount);
    }

    private UserEntity getUser(String username) {

        Optional<UserEntity> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return userOptional.get();
    }
  public BankAccount deleteBankAccount(Long id){
    BankAccount bankAccount= bankAccountRepository.findById(id).orElseThrow();
    bankAccountRepository.deleteById(id);
    return bankAccount;
  }
}
