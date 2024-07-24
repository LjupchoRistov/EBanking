package com.ebanking.service.impl;

import com.ebanking.exceptions.BankAccountNotFound;
import com.ebanking.models.BankAccount;
import com.ebanking.repository.BankAccountRepository;
import com.ebanking.service.BankAccountPinService;
import com.ebanking.service.BankAccountService;
import com.ebanking.service.EncryptDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BankAccountPinServiceImpl implements BankAccountPinService {

    private final BankAccountRepository bankAccountRepository;

    private final EncryptDataService encryptDataService;

    @Override
    public boolean doesPinMatch(String pin, String repeatPin) {

        return pin.equals(repeatPin);
    }

    @Override
    public boolean isPinValid(String pin, String bankAccountNum) {

        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccountNumEquals(bankAccountNum);

        if (bankAccountOptional.isEmpty()) {
            throw new BankAccountNotFound(bankAccountNum);
        }

        BankAccount bankAccount = bankAccountOptional.get();

        String salt = bankAccount.getSalt();

        byte[] saltInBytes = salt.getBytes();

        String combinedPinAndSalt = encryptDataService.combinePasswordAndSalt(String.valueOf(pin), saltInBytes);

        String hashedPin = encryptDataService.hashPassword(combinedPinAndSalt);

        return hashedPin.equals(bankAccount.getHashedPin());
    }
}
