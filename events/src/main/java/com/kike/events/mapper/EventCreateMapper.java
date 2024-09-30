package com.kike.events.mapper;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.entity.*;

public class EventCreateMapper {

    public static EventCreateDto mapToEventCreateDto(Event event, EventCreateDto eventCreateDto) {
        eventCreateDto.setAvailability(event.getAvailability());
        eventCreateDto.setDescription(event.getDescription());
        eventCreateDto.setPrice(event.getPrice());
        eventCreateDto.setTitle(event.getTitle());
        eventCreateDto.setVendorId(event.getVendorId());
        return eventCreateDto;
    }
    public static EventResponseDto mapToEventResponseDto(Event event, EventResponseDto eventResponseDto) {
        eventResponseDto.setAvailability(event.getAvailability());
        eventResponseDto.setDescription(event.getDescription());
        eventResponseDto.setPrice(event.getPrice());
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setVendorId(event.getVendorId());
        eventResponseDto.setId(event.getId());
        return eventResponseDto;
    }

    public static Event mapToEvents(EventCreateDto eventCreateDto, Event event) {

        event.setAvailability(eventCreateDto.getAvailability());
        event.setDescription(eventCreateDto.getDescription());
        event.setTitle(eventCreateDto.getTitle());
        event.setVendorId(eventCreateDto.getVendorId());
        event.setPrice(eventCreateDto.getPrice());

        return event;
    }
}
