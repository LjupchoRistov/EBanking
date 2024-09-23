package com.ebanking.service.impl;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.exceptions.BankAccountNotFound;
import com.ebanking.exceptions.CurrencyTypeNotFoundException;
import com.ebanking.mapper.BankAccountMapper;
import com.ebanking.models.BankAccount;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.repository.CurrencyTypeRepository;
import com.ebanking.repository.TransactionRepository;
import com.ebanking.repository.UserRepository;
import com.ebanking.service.BankAccountService;
import com.ebanking.service.EncryptDataService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ebanking.mapper.BankAccountMapper.mapToBankAccountDto;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final UserRepository userRepository;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final EncryptDataService encryptDataService;

    private final TransactionRepository transactionRepository;

    @Override
    public List<BankAccountDto> findBankAccountsByUsername(String username) {

        UserEntity user = getUser(username);

        List<BankAccount> bankAccounts = this.bankAccountRepository.findAllByUserUsername(user.getUsername());

        return bankAccounts.stream().map(BankAccountMapper::mapToBankAccountDto).toList();
    }

    @Override
    public void createBankAccount(CurrencyType currency, Integer pin, String username) {

        String currencyName = currency.getName();

        Optional<CurrencyType> currencyTypeOptional = currencyTypeRepository.findByNameEquals(currencyName);

        if (currencyTypeOptional.isEmpty()) {
            throw new CurrencyTypeNotFoundException(currencyName);
        }

        CurrencyType currencyType = currencyTypeOptional.get();

        String accountNum = generateAccountNumber(currencyType.getName());

        boolean isDebit = currencyName.equals("Macedonian Denar");

        Double balance = 0.0;

        LocalDate dateCreatedOn = LocalDate.now();

        UserEntity user = getUser(username);

        String salt = encryptDataService.createSalt();

        byte[] saltInBytes = salt.getBytes();

        String combinedPinAndSalt = encryptDataService.combinePasswordAndSalt(String.valueOf(pin), saltInBytes);

        String hashedPin = encryptDataService.hashPassword(combinedPinAndSalt);

        BankAccount bankAccount = new BankAccount(accountNum, isDebit, balance, dateCreatedOn, currencyType, user,
                hashedPin, salt);

        bankAccountRepository.save(bankAccount);
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
        String prefix = switch (currency) {
            case "Macedonian Denar" -> "MK";
            case "Euro" -> "EU";
            case "United States Dollar" -> "US";
            default -> throw new IllegalArgumentException("Unsupported currency: " + currency);
        };

        StringBuilder accountNum = new StringBuilder(prefix);
        for (int i = 0; i < 10; i++) {
            int digit = (int) Math.floor(Math.random() * 10);
            accountNum.append(digit);
        }

        return accountNum.toString();
    }

    @Override
    public Integer activeBankAccounts(String username) {

        UserEntity user = getUser(username);

        return bankAccountRepository.findAllByUserUsername(user.getUsername()).size();
    }

    @Override
    public Integer availableBankAccounts(UserEntity user) {

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
}
