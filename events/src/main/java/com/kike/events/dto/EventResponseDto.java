package com.kike.events.dto;

import com.kike.events.constants.Availability;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Schema(
        name = "Event Response",
        description = "Schema to hold the event information for response"
)
@Data
public class EventResponseDto {


    @Schema(description = "Unique identifier for the event", example = "1412")
    private Long id;

    @Schema(description = "Title of the Event ", example = "Bachata Class in center of Valencia")
    private String title;

    @Schema(
            description = "Title of the Event ", example = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required "
    )
    private String description;

    @Schema(
            description = "Price for signing up ", example = "10.9"
    )
    private BigDecimal price;

    @Schema(
            description = "Unique identifier for vendor", example = "154"
    )
    private int vendorId;

    @Schema(
            description = "Availability of the event, it shows wether is posible to book a place or not", example = "AVAILABLE"
    )
    private Availability availability;
}