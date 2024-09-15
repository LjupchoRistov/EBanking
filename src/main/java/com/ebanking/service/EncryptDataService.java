package com.ebanking.service;

public interface EncryptDataService {

    String createSalt();

    String combinePasswordAndSalt(String password, byte[] salt);

    String hashPassword(String input);
}
