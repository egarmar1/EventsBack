package com.kike.eventsHistory.functions;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.service.impl.EventsHistoryServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class EventsHistoryFunctions {

    private EventsHistoryServiceImpl eventsHistoryService;
    private static final Logger log = LoggerFactory.getLogger(EventsHistoryFunctions.class);

    @Bean
    public Consumer<EventsHistoryDto> createEventHistory(){
        log.info("Executing createEventHistory function... ");
        return eventsHistoryService::createEventHistory;
    }

}
