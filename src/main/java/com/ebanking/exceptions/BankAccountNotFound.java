package com.ebanking.exceptions;

public class BankAccountNotFound extends CustomNotFoundException {

    public BankAccountNotFound(String bankAccountNumber) {

        super("Bank account with number: " + bankAccountNumber + " not found");
    }
}
