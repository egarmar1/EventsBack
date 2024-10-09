package com.kike.events.controller;

import com.kike.events.constants.EventConstants;
import com.kike.events.dto.*;
import com.kike.events.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kike.events.constants.EventConstants.*;
import static org.springframework.http.HttpStatus.*;

@Tag(
        name = "CRUD REST API for events in FASTBOOK",
        description = "CRUD REST APIs for events in FASTBOOK to CREATE," +
                " UPDATE, FETCH AND DELETE account details "
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

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
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
    @PostMapping("/create")
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateDto eventCreateDto) {

        EventResponseDto eventResponseDto = iEventService.createEvent(eventCreateDto);

        log.info("Executing method createEvent");


        return ResponseEntity
                .status(CREATED)
                .body(eventResponseDto);
    }


    @Operation(
            summary = "Fetch event REST API",
            description = "REST API to fetch an event that a company is going to post inside FASTBOOK ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "HTTP Status NOT FOUND"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status INTERNAL SERVER ERROR",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
    @GetMapping("/fetch")
    public ResponseEntity<EventResponseDto> fetchEvent(@Valid @RequestParam Long id) {

        log.info("Fetch method before getting event");

        EventResponseDto eventResponseDto = iEventService.fetchEvent(id);

        log.info("Fetch method after getting event {}", eventResponseDto);
        return ResponseEntity
                .status(OK)
                .body(eventResponseDto);
    }

    @Operation(
            summary = "Update event REST API",
            description = "REST API to update an event that a company has posted inside FASTBOOK ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "HTTP Status NOT FOUND"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status INTERNAL SERVER ERROR",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateEvent(@Valid @RequestBody EventUpdateDto eventUpdateDto) {

        boolean isUpdated = iEventService.updateEvent(eventUpdateDto);

        if (isUpdated)
            return ResponseEntity
                    .status(OK)
                    .body(new ResponseDto(STATUS_200, MESSAGE_200));
        else
            return ResponseEntity
                    .status(EXPECTATION_FAILED)
                    .body(new ResponseDto(STATUS_417, MESSAGE_417_UPDATE));
    }


    @Operation(
            summary = "DELETE event REST API",
            description = "REST API to delete an event that a company has posted inside FASTBOOK ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "HTTP Status NOT FOUND"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status INTERNAL SERVER ERROR",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteEvent(@RequestParam Long id) {

        iEventService.deleteEvent(id);



        return ResponseEntity
                .status(OK)
                .body(new ResponseDto(STATUS_200, MESSAGE_200));
    }

    @PutMapping("/update/currentBookingsCount")
    public ResponseEntity<ResponseDto> updateCurrentBookingsCount(@Valid @RequestParam Long eventId) {

        boolean isUpdated = iEventService.increaseCurrentBookings(eventId);

        log.info("Inside updateCurrentBookingsCount");
        if (isUpdated)
            return ResponseEntity
                    .status(OK)
                    .body(new ResponseDto(STATUS_200, MESSAGE_200));
        else
            return ResponseEntity
                    .status(EXPECTATION_FAILED)
                    .body(new ResponseDto(STATUS_417, MESSAGE_417_UPDATE));
    }

}
