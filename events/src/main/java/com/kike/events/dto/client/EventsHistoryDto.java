package com.kike.events.dto.client;


import com.kike.events.constants.HistoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsHistoryDto {

    @NotNull(message = "User id cannot be empty")
    private String userId;

    @NotNull(message = "Event id cannot be empty")
    private Long eventId;

    @NotNull(message = "Event id cannot be empty")
    private HistoryType historyType;
}
