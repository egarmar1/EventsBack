package com.kike.events.mapper;

import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.entity.Event;

public class EventUpdateMapper {

    public static EventUpdateDto mapToEventResponseDto(Event event, EventUpdateDto eventUpdateDto) {
        eventUpdateDto.setAvailability(event.getAvailability());
        eventUpdateDto.setDescription(event.getDescription());
        eventUpdateDto.setPrice(event.getPrice());
        eventUpdateDto.setTitle(event.getTitle());
        eventUpdateDto.setVendorId(event.getVendorId());
        eventUpdateDto.setId(event.getId());
        eventUpdateDto.setMaxNumBookings(event.getMaxNumBookings());

        return eventUpdateDto;
    }

    public static Event mapToEvents(EventUpdateDto eventUpdateDto, Event event) {

        event.setId(eventUpdateDto.getId());
        event.setAvailability(eventUpdateDto.getAvailability());
        event.setDescription(eventUpdateDto.getDescription());
        event.setTitle(eventUpdateDto.getTitle());
        event.setVendorId(eventUpdateDto.getVendorId());
        event.setPrice(eventUpdateDto.getPrice());
        event.setMaxNumBookings(eventUpdateDto.getMaxNumBookings());

        return event;
    }
}
