package com.kike.eventsHistory.dto;


import com.kike.eventsHistory.constants.HistoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(
        name = "Response",
        description = "Schema to hold events history information"
)
@AllArgsConstructor
@NoArgsConstructor
public class EventsHistoryDto {

    @Schema(
            description = "Unique identifier of the user ", example = "23159018745"
    )
    @NotNull(message = "User id cannot be empty")
    private String userId;

    @Schema(
            description = "Unique identifier of the event ", example = "93159014745"
    )
    @NotNull(message = "Event id cannot be empty")
    private Long eventId;

    @Schema(
            description = "the history type. It can be 'EVENT_ORGANIZED' for vendors history when event is created, 'BOOKED_EVENT' for users when booked for event, 'ATTENDED_EVENT' when user has attended an event", example = "93159014745"
    )
    @NotNull(message = "Event id cannot be empty")
    private HistoryType historyType;
}
