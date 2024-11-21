package com.kike.eventReviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventReviewsResponseDto {

    @Schema(
            description = "Unique identifier of the Event Review", example = "19410240183"
    )
    @NotNull(message = "EventReviews Id cannot be emtpy")
    private Long id;

    @Schema(
            description = "Unique identifier of the Event", example = "19410240183"
    )
    @NotNull(message = "Event Id cannot be emtpy")
    private Long eventId;

    @Schema(
            description = "Unique identifier of the User", example = "sjd2389Sjfi2198kdfp"
    )
    @NotNull(message = "Event Id cannot be emtpy")
    private String userId;

    @Schema(
            description = "Unique identifier of the User", example = "sjd2389Sjfi2198kdfp"
    )
    @Min(value = 0, message = "Rating minimum value is 0")
    @Max(value = 10, message = "Rating maximum value is 10")
    private Integer rating;

    @Size(max = 10000, message = "Message is too long, can't be greater than 10000 characters")
    private String comment;
}
