package com.ebanking.exceptions;

public class UserNotFoundException extends CustomNotFoundException{

    public UserNotFoundException(String input) {
        super("User with username / email: " + input + " not found");
    }

    public UserNotFoundException(Long id) {
        super("User with id: " + id + " not found");
    }
}
