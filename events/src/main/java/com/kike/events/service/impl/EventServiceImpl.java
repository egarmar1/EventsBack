package com.kike.events.service.impl;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.entity.Event;
import com.kike.events.exception.CurrentBookingsGreaterThanMaxException;
import com.kike.events.exception.ResourceNotFoundException;
import com.kike.events.mapper.EventCreateMapper;
import com.kike.events.mapper.EventResponseMapper;
import com.kike.events.mapper.EventUpdateMapper;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.IEventService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;

    @Override
    public EventResponseDto createEvent(EventCreateDto eventCreateDto) {

        Event event = EventCreateMapper.mapToEvents(eventCreateDto, new Event());
        event.setCurrentNumBookings(0L);
        Event savedEvent = eventRepository.save(event);

        return EventResponseMapper.mapToEventResponseDto(savedEvent, new EventResponseDto());
    }

    @Override
    public EventResponseDto fetchEvent(Long id) {

        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", "id", id.toString()));

        return EventResponseMapper.mapToEventResponseDto(event, new EventResponseDto());
    }

    @Override
    public boolean updateEvent(EventUpdateDto eventUpdateDto) {

        boolean isUpdated = false;

        Long eventId = eventUpdateDto.getId();

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", eventId.toString()));


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

    @Override
    public void deleteEvent(Long id) {

        eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", id.toString())
        );

        eventRepository.deleteById(id);

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
