package com.kike.suscription.exception;

public class SuscriptionAlreadyExistsException extends RuntimeException{
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public SuscriptionAlreadyExistsException(String userId, String eventId) {
        super(String.format("The suscription with userId %s and eventId %s already exists", userId, eventId));
    }
}
