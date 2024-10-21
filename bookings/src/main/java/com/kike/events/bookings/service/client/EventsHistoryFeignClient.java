package com.kike.events.bookings.service.client;

import com.kike.events.bookings.dto.ResponseDto;
import com.kike.events.bookings.dto.client.EventsHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "eventsHistory")
public interface EventsHistoryFeignClient {

    @PutMapping("/api/update")
    public ResponseEntity<ResponseDto> updateEventHistory(EventsHistoryDto eventsHistoryDto);
}
