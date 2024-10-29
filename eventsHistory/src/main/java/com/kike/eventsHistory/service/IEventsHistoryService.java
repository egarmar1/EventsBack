package com.kike.eventsHistory.service;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


public interface IEventsHistoryService {

    void createEventHistory(EventsHistoryDto eventsHistoryDto);
    void updateEventHistory(EventsHistoryDto eventsHistoryDto);

    EventsHistoryDto fetchByUserId(String userId, Jwt jwt);
}
