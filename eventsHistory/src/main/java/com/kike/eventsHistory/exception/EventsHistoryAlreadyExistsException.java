package com.kike.eventsHistory.exception;


public class EventsHistoryAlreadyExistsException extends RuntimeException {

    public EventsHistoryAlreadyExistsException(String eventId, String userId) {
        super(String.format("EventsHistory with eventId %s and userId %s already exists", eventId, userId));
    }
}
