package com.kike.events.bookings.scheduler;

import com.kike.events.bookings.service.IBookingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class QrExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(QrExpirationScheduler.class);
    private final IBookingService bookingService;


    @Scheduled(cron = "*/60 * * * * *")
    public void deleteExpiredQRCodes(){
        log.info("Executing deleteExpiredQRCodes in Schedule...");
        bookingService.deleteExpiredQRCodes();
    }
}
