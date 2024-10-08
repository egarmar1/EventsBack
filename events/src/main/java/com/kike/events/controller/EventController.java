package com.kike.events.controller;

import com.kike.events.constants.EventConstants;
import com.kike.events.dto.EventDto;
import com.kike.events.dto.ResponseDto;
import com.kike.events.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kike.events.constants.EventConstants.*;
import static org.springframework.http.HttpStatus.*;

@Tag(
        name = "CRUD REST API for events in SportEvents",
        description = "CRUD REST APIs for events in SportEvents to CREATE," +
                " UPDATE, FETCH AND DELETE events"
)
@RestController
@RequestMapping(value = "/api",consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class EventController {

    IEventService iEventService;

    @Operation(
            summary = "Create event REST API",
            description = "REST API to create an event that a company is going to post inside FASTBOOK ",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "HTTP Status CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status INTERNAL SERVER ERROR",
                            content = @Content(
                                    schema = @Schema(implementation = Error.class)
                            )
                    )
            }

    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEvent(@Valid @RequestBody EventDto eventDto){

        iEventService.createEvent(eventDto);

        return ResponseEntity
                .status(CREATED)
                .body(new ResponseDto(STATUS_200,MESSAGE_201));
    }
}
