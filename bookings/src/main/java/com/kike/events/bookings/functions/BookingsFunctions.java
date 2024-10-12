package com.kike.events.bookings.functions;

import com.kike.events.bookings.service.IBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class BookingsFunctions {

    private static final Logger log = LoggerFactory.getLogger(BookingsFunctions.class);

    @Bean
    public Consumer<Long> deleteBookingsOfEvent(IBookingService iBookingService) {
        return eventId -> {
            log.info("Entering deleteBookingsOfEvent, the enventId is {}", eventId);
            iBookingService.deleteAllbookingsWithEventId(eventId);
        };
    }
}
