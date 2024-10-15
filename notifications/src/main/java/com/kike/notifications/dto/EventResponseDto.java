package com.kike.notifications.dto;

import com.kike.notifications.constants.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String vendorId;

    private Availability availability;

    private Long maxNumBookings;

    private Long currentNumBookings;
}
