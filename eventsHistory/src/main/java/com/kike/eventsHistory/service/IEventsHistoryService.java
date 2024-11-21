package com.kike.eventsHistory.service;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;


public interface IEventsHistoryService {

    void createEventHistory(EventsHistoryDto eventsHistoryDto);
    void updateEventHistory(EventsHistoryDto eventsHistoryDto);

    List<EventsHistoryDto> fetchAllByUserId(String userId, Jwt jwt);

    EventsHistoryDto fetchByUserIdAndEventId(String userId, Long eventId, Jwt jwt);
}
