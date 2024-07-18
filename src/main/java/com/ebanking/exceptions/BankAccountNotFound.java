package com.ebanking.exceptions;

public class BankAccountNotFound extends CustomNotFoundException{

    public BankAccountNotFound(Long id) {

        super("Bank account with id: " + id + " not found");
    }
}
