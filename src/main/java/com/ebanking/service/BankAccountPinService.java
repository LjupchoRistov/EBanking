package com.ebanking.service;

public interface BankAccountPinService {

    boolean doesPinMatch(String pin, String repeatPin);

    boolean isPinValid(String pin, String bankAccountId);
}
