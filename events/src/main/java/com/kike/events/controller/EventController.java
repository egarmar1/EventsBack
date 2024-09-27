package com.kike.events.controller;

import com.kike.events.constants.EventConstants;
import com.kike.events.dto.EventDto;
import com.kike.events.dto.ResponseDto;
import com.kike.events.service.IEventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kike.events.constants.EventConstants.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api",consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class EventController {

    IEventService iEventService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEvent(@Valid @RequestBody EventDto eventDto){

        iEventService.createEvent(eventDto);

        return ResponseEntity
                .status(CREATED)
                .body(new ResponseDto(STATUS_200,MESSAGE_201));
    }
}
