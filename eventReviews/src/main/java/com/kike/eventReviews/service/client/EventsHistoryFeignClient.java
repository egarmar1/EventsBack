package com.kike.eventReviews.service.client;

import com.kike.eventReviews.dto.EventsHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "eventsHistory")
public interface EventsHistoryFeignClient {

    @GetMapping("/api/fetch")
    ResponseEntity<EventsHistoryDto> getEventsHistoryByUserId(String userId, Jwt jwt);

    @GetMapping("/api/fetch/eventId/{eventId}")
    public ResponseEntity<EventsHistoryDto> getEventHistoryByUserIdAndEventId(String userId, Long eventId, Jwt jwt);
}
