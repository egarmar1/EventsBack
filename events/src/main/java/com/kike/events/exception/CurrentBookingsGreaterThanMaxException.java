package com.kike.events.exception;

public class CurrentBookingsGreaterThanMaxException extends RuntimeException{

    public CurrentBookingsGreaterThanMaxException(Long currentBookings, Long maxBookings) {
        super(String.format("Current bookings number is %s and max bookings number is %s," +
                "current bookings cannot be greater than maximum bookings", currentBookings, maxBookings));
    }
}
