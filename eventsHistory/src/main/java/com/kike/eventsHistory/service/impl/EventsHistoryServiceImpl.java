package com.kike.eventsHistory.service.impl;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.entity.EventsHistory;
import com.kike.eventsHistory.exception.EventsHistoryAlreadyExistsException;
import com.kike.eventsHistory.exception.MissingUserIdFromAdminException;
import com.kike.eventsHistory.exception.ResourceNotFoundException;
import com.kike.eventsHistory.exception.UnauthorizedException;
import com.kike.eventsHistory.mapper.EventsHistoryMapper;
import com.kike.eventsHistory.repository.EventsHistoryRepository;
import com.kike.eventsHistory.service.IEventsHistoryService;
import com.kike.eventsHistory.service.auth.JwtService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EventsHistoryServiceImpl implements IEventsHistoryService {

    private EventsHistoryRepository eventsHistoryRepository;
    private JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(EventsHistoryServiceImpl.class);

    @Override
    public void createEventHistory(EventsHistoryDto eventsHistoryDto) {
        log.info("Executing createEventHistory service method");

        Long eventId = eventsHistoryDto.getEventId();
        String userId = eventsHistoryDto.getUserId();


        Optional<EventsHistory> eventHistory = eventsHistoryRepository.findByUserIdAndEventId(userId, eventId);

        if (eventHistory.isPresent())
            throw new EventsHistoryAlreadyExistsException(eventId.toString(), userId);


        EventsHistory eventHistoryToSave = EventsHistoryMapper.mapToEventsHistory(eventsHistoryDto, new EventsHistory());

        eventsHistoryRepository.save(eventHistoryToSave);

    }

    @Override
    public void updateEventHistory(EventsHistoryDto eventsHistoryDto) {

        EventsHistory eventsHistory = eventsHistoryRepository.findByUserIdAndEventId(eventsHistoryDto.getUserId(), eventsHistoryDto.getEventId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("eventHistory", "UserId and eventId ",
                                eventsHistoryDto.getUserId() + " and " + eventsHistoryDto.getEventId() + " respectively."));

        EventsHistoryMapper.mapToEventsHistory(eventsHistoryDto, eventsHistory);

        eventsHistoryRepository.save(eventsHistory);
    }

    @Override
    public EventsHistoryDto fetchByUserId(String userId, Jwt jwt) {
        log.info("Starting fetchByUserId method...");
        String targetUserId = getTargetUserId(userId, jwt);

        EventsHistory eventsHistory = eventsHistoryRepository.findByUserId(targetUserId).orElseThrow(
                () -> new ResourceNotFoundException("EventHistory", "userId", targetUserId)
        );

        return EventsHistoryMapper.mapToEventsHistoryDto(eventsHistory,new EventsHistoryDto());
    }


    @NotNull
    private String getTargetUserId(String userId, Jwt jwt) {
        if (jwt == null)
            throw new UnauthorizedException("Oauth2 access token is required");

        String actualUserId = jwtService.getRealmRoles(jwt).contains("admin") ?
                userId : jwtService.getUserId(jwt);

        if (actualUserId.isEmpty())
            throw new MissingUserIdFromAdminException();

        return actualUserId;
    }
}
