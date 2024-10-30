package com.kike.events.service;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IEventService {

    EventResponseDto createEvent(EventCreateDto eventCreateDto, Jwt jwt);

    EventResponseDto fetchEvent(Long id, Jwt jwt);

    boolean updateEvent(EventUpdateDto eventUpdateDto, Jwt jwt);

    void deleteEvent(Long id, Jwt jwt);

    boolean increaseCurrentBookings(Long eventId);
}
