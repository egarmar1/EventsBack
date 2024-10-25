package com.kike.events.bookings.dto;

import com.kike.events.bookings.constants.State;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
        name = "BookingDto",
        description = "Schema to hold booking information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    @Schema(
            description = "Unique identifier of the booking ", example = "91055325"
    )
    @NotNull(message = "User id cannot be empty")
    private Long id;

    @Schema(
            description = "Unique identifier of the user ", example = "1245f0187h5"
    )
    @NotNull(message = "User id cannot be empty")
    private String userId;

    @Schema(
            description = "UUID of the QR that represents the booking", example = "1245f0187h5"
    )
    @NotNull(message = "User id cannot be empty")
    private String qrUUID;

    @Schema(
            description = "Unique identifier of the service ", example = "12459018745"
    )
    @NotNull(message = "Service id cannot be empty")
    private Long eventId;

    @Schema(
            description = "Book date that the user has sign up for", example = "10.9"
    )
    private LocalDateTime bookDate;

    @Schema(
            description = "State of the book, it shows wether it's accepted, pending or cancelled", example = "ACCEPTED"
    )
    @NotNull(message = "The state cannot be null")
    private State state;


}
