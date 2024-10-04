package com.kike.events.service;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;

public interface IEventService {

    EventResponseDto createEvent(EventCreateDto eventCreateDto);

    EventResponseDto fetchEvent(Long id);

    boolean updateEvent(EventUpdateDto eventUpdateDto);

    void deleteEvent(Long id);

    boolean increaseCurrentBookings(Long eventId);
}
