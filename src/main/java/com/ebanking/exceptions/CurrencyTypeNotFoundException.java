package com.ebanking.exceptions;

public class CurrencyTypeNotFoundException extends CustomNotFoundException{

    public CurrencyTypeNotFoundException(String name) {

        super("Currency with name " + name + " not found");
    }
}
