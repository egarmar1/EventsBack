package com.kike.events.mapper;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.entity.Event;

public class EventResponseMapper {

    public static EventResponseDto mapToEventResponseDto(Event event, EventResponseDto eventResponseDto) {
        eventResponseDto.setAvailability(event.getAvailability());
        eventResponseDto.setDescription(event.getDescription());
        eventResponseDto.setPrice(event.getPrice());
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setVendorId(event.getVendorId());
        eventResponseDto.setId(event.getId());
        eventResponseDto.setMaxNumBookings(event.getMaxNumBookings());
        eventResponseDto.setCurrentNumBookings(event.getCurrentNumBookings());
        return eventResponseDto;
    }

    public static Event mapToEvents(EventResponseDto eventResponseDto, Event event) {

        event.setId(eventResponseDto.getId());
        event.setAvailability(eventResponseDto.getAvailability());
        event.setDescription(eventResponseDto.getDescription());
        event.setTitle(eventResponseDto.getTitle());
        event.setVendorId(eventResponseDto.getVendorId());
        event.setPrice(eventResponseDto.getPrice());
        event.setMaxNumBookings(eventResponseDto.getMaxNumBookings());
        event.setCurrentNumBookings(eventResponseDto.getCurrentNumBookings());

        return event;
    }
}
