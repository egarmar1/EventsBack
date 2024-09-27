package com.kike.events.service.impl;

import com.kike.events.dto.EventDto;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.IEventService;
import lombok.AllArgsConstructor;
import com.kike.events.entity.*;
import org.springframework.stereotype.Service;

import static com.kike.events.mapper.EventMapper.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    EventRepository eventRepository;
    @Override
    public void createEvent(EventDto eventDto) {

        Event event = mapToEvents(eventDto, new Event());
        eventRepository.save(event);
    }
}
