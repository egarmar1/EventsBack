package com.kike.notifications.service.client;

import com.kike.notifications.dto.EventResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "event")
public interface EventsFeignClient {

    @GetMapping("/api/fetch")
    public ResponseEntity<EventResponseDto> fetchEvent(@Valid @RequestParam Long id);
}
