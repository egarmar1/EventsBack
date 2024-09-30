package com.kike.events.service.impl;

import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.exception.ResourceNotFoundException;
import com.kike.events.mapper.EventCreateMapper;
import com.kike.events.mapper.EventResponseMapper;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.IEventService;
import lombok.AllArgsConstructor;
import com.kike.events.entity.*;
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
        Event savedEvent = eventRepository.save(event);

        return EventResponseMapper.mapToEventResponseDto(savedEvent, new EventResponseDto());
    }

    @Override
    public EventResponseDto fetchEvent(Long id) {

        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", "id", id.toString()));

        return EventResponseMapper.mapToEventResponseDto(event, new EventResponseDto());
    }

    @Override
    public boolean updateEvent(EventResponseDto eventResponseDto) {

        boolean isUpdated = false;

        Long eventId = eventResponseDto.getId();

        eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", eventId.toString()));

        Event event = EventResponseMapper.mapToEvents(eventResponseDto, new Event());

        eventRepository.save(event);

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
}
