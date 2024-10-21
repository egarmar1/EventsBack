package com.kike.events.bookings.controller;


import com.kike.events.bookings.constants.BookingConstants;
import com.kike.events.bookings.dto.BookingDto;
import com.kike.events.bookings.dto.ErrorResponseDto;
import com.kike.events.bookings.dto.ResponseDto;
import com.kike.events.bookings.exception.UnauthorizedException;
import com.kike.events.bookings.service.IBookingService;
import com.kike.events.bookings.service.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import static com.kike.events.bookings.constants.BookingConstants.*;

import static org.springframework.http.HttpStatus.*;

@Tag(
        name = "CRUD REST API for bookings in FASTBOOK",
        description = "CRUD REST APIs for bookings in FASTBOOK to CREATE," +
                " UPDATE, FETCH AND DELETE bookings. For CREATE, UPDATE and DELETE jwt access token" +
                " is needed since the userId is taken from that access token"
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BookingController {

    IBookingService iBookingService;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Operation(
            summary = "Create booking REST API",
            description = "REST API to create a booking that a user is going to sing up for in FASTBOOK. " +
                    "The userId is taken from JWT OAUTH2 Token",
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
                            responseCode = "409",
                            description = "HTTP Status CONFLICT",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "HTTP Status UNAUTHORIZED",
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
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                     @RequestParam(required = false) String userId,
                                                     @AuthenticationPrincipal Jwt jwt) {

        iBookingService.createBooking(bookingDto, userId, jwt);

        return ResponseEntity
                .status(CREATED)
                .body(new ResponseDto(STATUS_201, MESSAGE_201));
    }


    @Operation(
            summary = "Fetch booking REST API",
            description = "REST API to fetch a booking of a specific user inside FASTBOOK ",
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
    public ResponseEntity<BookingDto> fetchbooking(@RequestParam Long eventId, @RequestParam String userId) {

        BookingDto bookingResponseDto = iBookingService.fetchbooking(eventId, userId);

        return ResponseEntity
                .status(OK)
                .body(bookingResponseDto);
    }

    @Operation(
            summary = "Update booking REST API",
            description = "REST API to update a booking for specific user inside FASTBOOK." +
                    " The userId is taken from JWT OAUTH2 Token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "HTTP Status UNAUTHORIZED",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    public ResponseEntity<ResponseDto> updatebooking(@Valid @RequestBody BookingDto bookingDto,
                                                     @RequestParam(required = false) String userId,
                                                     @AuthenticationPrincipal Jwt jwt) {

        boolean isUpdated = iBookingService.updatebooking(bookingDto, userId, jwt);

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
            summary = "DELETE booking REST API",
            description = "REST API to delete a booking of a specific user inside FASTBOOK." +
                    " The userId is taken from JWT OAUTH2 Token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "HTTP Status UNAUTHORIZED",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deletebooking(@RequestParam Long eventId,
                                                     @RequestParam(required = false) String userId,
                                                     @AuthenticationPrincipal Jwt jwt) {

        iBookingService.deleteBooking(eventId, userId, jwt);

        return ResponseEntity
                .status(OK)
                .body(new ResponseDto(STATUS_200, MESSAGE_200));
    }


    @Operation(
            summary = "Set as marked booking REST API",
            description = "REST API for vendors to mark the booking of a user as attended." +
                    " This is used when the vendors check users at the moment they attend to the event",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "HTTP Status BAD REQUEST",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "HTTP Status UNAUTHORIZED",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
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
    @PutMapping("/setAsAttended")
    public ResponseEntity<ResponseDto> setBookingAsAttended(@RequestParam Long eventId,
                                                          @RequestParam String userId,
                                                          @AuthenticationPrincipal Jwt jwt){

        iBookingService.setBookingAsAttended(eventId,userId,jwt);

        return ResponseEntity.status(OK).body(new ResponseDto(STATUS_200, MESSAGE_200));
    }
}
