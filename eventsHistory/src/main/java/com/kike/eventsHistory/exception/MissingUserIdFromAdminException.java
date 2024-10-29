package com.kike.eventsHistory.exception;

public class MissingUserIdFromAdminException extends RuntimeException{
    public MissingUserIdFromAdminException() {
        super("Admins need to specify as a url parameter the userId");
    }
}
