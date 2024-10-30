package com.kike.events.service.impl;

import com.kike.events.constants.HistoryType;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.dto.client.EventsHistoryDto;
import com.kike.events.dto.client.UserTypeDto;
import com.kike.events.entity.Event;
import com.kike.events.exception.CurrentBookingsGreaterThanMaxException;
import com.kike.events.exception.ForbiddenException;
import com.kike.events.exception.IncorrectTypeOfUserException;
import com.kike.events.exception.ResourceNotFoundException;
import com.kike.events.mapper.EventCreateMapper;
import com.kike.events.mapper.EventResponseMapper;
import com.kike.events.mapper.EventUpdateMapper;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.IEventService;
import com.kike.events.service.auth.JwtService;
import com.kike.events.service.client.UserFeignClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;
    private StreamBridge streamBridge;
    private UserFeignClient userFeignClient;
    private JwtService jwtService;

    @Override
    public EventResponseDto createEvent(EventCreateDto eventCreateDto, Jwt jwt) {

        String vendorId = eventCreateDto.getVendorId();
        String vendorIdJwt = jwt.getClaim("sub");


        if (!Objects.equals(vendorId, vendorIdJwt) && !isAdmin(jwt)) {
            throw new ForbiddenException("You are not allowed to create the event " +
                    " for vendor with id: " + vendorId);
        }

        checkTypeVendor(vendorId, jwt);


        Event event = EventCreateMapper.mapToEvents(eventCreateDto, new Event());
        event.setCurrentNumBookings(0L);
        Event savedEvent = eventRepository.save(event);


        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(vendorId,
                savedEvent.getId(),
                HistoryType.EVENT_ORGANIZED);

        streamBridge.send("create-event-history", eventsHistoryDto);

        return EventResponseMapper.mapToEventResponseDto(savedEvent, new EventResponseDto());
    }

    private void checkTypeVendor(String vendorId, Jwt jwt) {
        try {
            ResponseEntity<UserTypeDto> typeResponse = userFeignClient.getType(vendorId);


            String type = typeResponse.getBody().getType();
            if (!type.equals("vendor") && !isAdmin(jwt))
                throw new IncorrectTypeOfUserException(vendorId, "vendor", type);

        } catch (FeignException feignException) {
            if (feignException.status() == HttpStatus.NOT_FOUND.value())
                throw new ResourceNotFoundException("UserType", "userId", vendorId);
        }

    }

    @Override
    public EventResponseDto fetchEvent(Long id, Jwt jwt) {

        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", "id", id.toString()));

        if (!isAdmin(jwt) && !Objects.equals(event.getVendorId(), jwt.getClaim("sub")))
            throw new ForbiddenException("You don't have permissions for info about event with id  " + id);

        return EventResponseMapper.mapToEventResponseDto(event, new EventResponseDto());
    }

    @Override
    public boolean updateEvent(EventUpdateDto eventUpdateDto, Jwt jwt) {

        String vendorIdDto = eventUpdateDto.getVendorId();
        String vendorIdJwt = jwt.getClaim("sub");

        if (!Objects.equals(vendorIdDto, vendorIdJwt) && !isAdmin(jwt)) {
            throw new ForbiddenException("You are not allowed to change the vendor" +
                    " of the event with id: " + eventUpdateDto.getId());
        }

        boolean isUpdated = false;

        Long eventId = eventUpdateDto.getId();

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", eventId.toString()));


        if (!Objects.equals(event.getVendorId(), vendorIdJwt) && !isAdmin(jwt)) {
            throw new ForbiddenException("You are not allowed to change" +
                    " the event with id: " + eventUpdateDto.getId());
        }

        if (event.getCurrentNumBookings() > eventUpdateDto.getMaxNumBookings())
            throw new CurrentBookingsGreaterThanMaxException(event.getCurrentNumBookings(),
                    eventUpdateDto.getMaxNumBookings());


        Event eventUpdated = EventUpdateMapper.mapToEvents(eventUpdateDto, new Event());


        // We set the currentNumBookings with the current value
        eventUpdated.setCurrentNumBookings(event.getCurrentNumBookings());
        eventRepository.save(eventUpdated);

        isUpdated = true;

        return isUpdated;
    }

    private boolean isAdmin(Jwt jwt) {
        return jwtService.getRealmRoles(jwt).contains("admin");
    }

    @Override
    public void deleteEvent(Long id, Jwt jwt) {


        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", id.toString())
        );

        Object vendorIdJwt = jwt.getClaim("sub");

        if (!Objects.equals(event.getVendorId(), vendorIdJwt) && !isAdmin(jwt)) {
            throw new ForbiddenException("You are not allowed to change" +
                    " the event with id: " + id);
        }

        eventRepository.deleteById(id);
        streamBridge.send("deleteBookingsOfEvent-out-0", id);

    }

    @Override
    public boolean increaseCurrentBookings(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", eventId.toString())
        );

        Long currentNumBookings = event.getCurrentNumBookings();
        Long maxNumBookings = event.getMaxNumBookings();


        if (currentNumBookings < maxNumBookings)
            event.setCurrentNumBookings(currentNumBookings + 1);
        else
            throw new CurrentBookingsGreaterThanMaxException(currentNumBookings, maxNumBookings);

        eventRepository.save(event);
        return true;
    }


}
