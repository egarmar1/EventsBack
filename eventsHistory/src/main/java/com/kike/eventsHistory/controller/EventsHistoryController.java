package com.kike.eventsHistory.controller;

import com.kike.eventsHistory.dto.ErrorResponseDto;
import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.dto.ResponseDto;
import com.kike.eventsHistory.service.IEventsHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kike.eventsHistory.constants.EventsHistoryConstants.*;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsHistoryController {

    private IEventsHistoryService eventsHistoryService;
    private static final Logger log = LoggerFactory.getLogger(EventsHistoryController.class);

    @Operation(
            summary = "update event history REST API",
            description = "REST API to update a history event of a specific user inside FASTBOOK ",
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
                            description = "HTTP Status NOT FOUND",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    public ResponseEntity<ResponseDto> updateEventHistory(@RequestBody EventsHistoryDto eventsHistoryDto){

        eventsHistoryService.updateEventHistory(eventsHistoryDto);

        return ResponseEntity.status(OK).body(new ResponseDto(STATUS_200, MESSAGE_200));
    }

    @Operation(
            summary = "Fetch event history REST API",
            description = "REST API to fetch all the history of events of a specific user inside FASTBOOK ",
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
                            description = "HTTP Status NOT FOUND",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    public ResponseEntity<List<EventsHistoryDto>> getAllEventsHistoryByUserId(@RequestParam(required = false) String userId,
                                                                     @AuthenticationPrincipal Jwt jwt){

        List<EventsHistoryDto> eventsHistoryDto = eventsHistoryService.fetchAllByUserId(userId, jwt);

        return ResponseEntity
                .status(OK)
                .body(eventsHistoryDto);
    }

    @Operation(
            summary = "Fetch event history REST API",
            description = "REST API to fetch all the history of events of a specific user inside FASTBOOK ",
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
                            description = "HTTP Status NOT FOUND",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    @GetMapping("/fetch/eventId/{eventId}")
    public ResponseEntity<EventsHistoryDto> getEventHistoryByUserIdAndEventId(@RequestParam(required = false) String userId,
                                                                              @PathVariable Long eventId,
                                                                              @AuthenticationPrincipal Jwt jwt){

        EventsHistoryDto eventHistoryDto = eventsHistoryService.fetchByUserIdAndEventId(userId,eventId, jwt);

        return ResponseEntity
                .status(OK)
                .body(eventHistoryDto);
    }
}
