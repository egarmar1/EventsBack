package com.kike.events.dto;

import com.kike.events.constants.Availability;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Schema(
        name = "EventCreate",
        description = "Schema to hold to create an event"
)
@Data
@AllArgsConstructor
public class EventCreateDto {



    @Schema(description = "Title of the Event ", example = "Bachata Class in center of Valencia")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @Schema(
            description = "Title of the Event ", example = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required "
    )
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Schema(
            description = "Price for signing up ", example = "10.9"
    )
    @PositiveOrZero(message = "Price must be greater or equal than zero")
    private BigDecimal price;

    @Schema(
            description = "Unique identifier for vendor", example = "154"
    )
    @Positive(message = "Vendor must be a positive number")
    private int vendorId;

    @Schema(
            description = "Availability of the event, it shows wether is posible to book a place or not", example = "AVAILABLE"
    )
    @NotNull(message = "The type of availability cannot be null")
    private Availability availability;

    @Schema(
            description = "Maximum number of accepted bookings in an event", example = "50"
    )
    private Long maxNumBookings;
}
