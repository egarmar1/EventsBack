package com.kike.events.service.impl;

import com.kike.events.dto.EventDto;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.IEventService;
import lombok.AllArgsConstructor;
import com.kike.events.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.kike.events.mapper.EventMapper.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;
    @Override
    public void createEvent(EventDto eventDto) {

        Event event = mapToEvents(eventDto, new Event());
        log.info("The event contains: {}", event);
        eventRepository.save(event);
    }
}
