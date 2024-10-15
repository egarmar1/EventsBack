package com.kike.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventCreatedDto {
    private Long eventId;
    private String vendorId;
}
