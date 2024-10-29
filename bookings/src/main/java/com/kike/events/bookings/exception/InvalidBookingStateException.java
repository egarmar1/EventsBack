package com.kike.events.bookings.exception;

public class InvalidBookingStateException extends RuntimeException{

    public InvalidBookingStateException(String message) {
        super(message);
    }
}
