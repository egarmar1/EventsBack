package com.kike.events.mapper;

import com.kike.events.dto.EventDto;
import com.kike.events.entity.*;

public class EventMapper {

    public static EventDto mapToEventsDto(Event event, EventDto eventDto) {
        eventDto.setAvailability(event.getAvailability());
        eventDto.setDescription(event.getDescription());
        eventDto.setPrice(event.getPrice());
        eventDto.setTitle(event.getTitle());
        eventDto.setVendorId(event.getVendorId());
        return eventDto;
    }

    public static Event mapToEvents(EventDto eventDto, Event event) {

        event.setAvailability(eventDto.getAvailability());
        event.setDescription(eventDto.getDescription());
        event.setTitle(eventDto.getTitle());
        event.setVendorId(eventDto.getVendorId());
        event.setPrice(eventDto.getPrice());

        return event;
    }
}
