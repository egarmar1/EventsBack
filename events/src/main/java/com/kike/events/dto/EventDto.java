package com.kike.events.dto;

import com.kike.events.constants.Availability;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventDto {


    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @PositiveOrZero(message = "Price must be greater or equal than zero")
    private BigDecimal price;

    @Positive(message = "Price must be greater or equal than zero")
    private int vendorId;

    private Availability availability;
}
