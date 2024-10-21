package com.kike.events.exception;

public class IncorrectTypeOfUserException extends RuntimeException{

    public IncorrectTypeOfUserException(String userId, String expectedType, String actualType) {
        super(String.format("The user with id %s does not have the type %s, it actually contains the type %s", userId, expectedType, actualType));
    }
}
