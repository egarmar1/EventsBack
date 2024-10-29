package com.kike.eventsHistory.controller;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.dto.ResponseDto;
import com.kike.eventsHistory.service.IEventsHistoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import static com.kike.eventsHistory.constants.EventsHistoryConstants.*;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsHistoryController {

    private IEventsHistoryService eventsHistoryService;
    private static final Logger log = LoggerFactory.getLogger(EventsHistoryController.class);

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateEventHistory(@RequestBody EventsHistoryDto eventsHistoryDto){

        eventsHistoryService.updateEventHistory(eventsHistoryDto);

        return ResponseEntity.status(OK).body(new ResponseDto(STATUS_200, MESSAGE_200));
    }

    @GetMapping("/fetch")
    public ResponseEntity<EventsHistoryDto> getEventsHistoryByUserId(@RequestParam(required = false) String userId,
                                                                     @AuthenticationPrincipal Jwt jwt){

        EventsHistoryDto eventsHistoryDto = eventsHistoryService.fetchByUserId(userId, jwt);

        return ResponseEntity
                .status(OK)
                .body(eventsHistoryDto);
    }
}
