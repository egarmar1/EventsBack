package com.kike.events.bookings.exception;

public class BookingAlreadyExistsException extends RuntimeException{

    public BookingAlreadyExistsException(String message) {
        super(message);
    }
}
