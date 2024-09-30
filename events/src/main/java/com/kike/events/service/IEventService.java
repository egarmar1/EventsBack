package com.kike.events.service;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;

public interface IEventService {

    EventResponseDto createEvent(EventCreateDto eventCreateDto);

    EventResponseDto fetchEvent(Long id);

    boolean updateEvent(EventResponseDto eventResponseDto);

    void deleteEvent(Long id);
}
