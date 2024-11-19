package com.kike.events.bookings.service.client;

import com.kike.events.bookings.config.FeignClientConfiguration;
import com.kike.events.bookings.dto.ResponseDto;
import com.kike.events.bookings.dto.client.EventResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "event", configuration = FeignClientConfiguration.class)
public interface EventsFeignClient {

    @PutMapping(value = "/api/update/currentBookingsCount")
    public ResponseEntity<ResponseDto> updateCurrentBookingsCount(@RequestParam Long eventId);

    @GetMapping("/api/fetch")
    public ResponseEntity<EventResponseDto> fetchEvent(@Valid @RequestParam Long id);

}
