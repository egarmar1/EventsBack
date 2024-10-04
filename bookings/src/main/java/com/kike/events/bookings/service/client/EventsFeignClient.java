package com.kike.events.bookings.service.client;

import com.kike.events.bookings.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "event")
public interface EventsFeignClient {

    @PutMapping(value = "/api/update/currentBookingsCount")
    public ResponseEntity<ResponseDto> updateCurrentBookingsCount(@RequestParam Long eventId);
}
