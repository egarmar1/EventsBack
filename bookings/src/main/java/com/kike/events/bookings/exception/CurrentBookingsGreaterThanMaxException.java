package com.kike.events.bookings.exception;

public class CurrentBookingsGreaterThanMaxException extends RuntimeException {

    public CurrentBookingsGreaterThanMaxException(String message) {
        super(message);
    }
}
