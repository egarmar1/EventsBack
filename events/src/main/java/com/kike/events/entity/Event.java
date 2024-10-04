package com.kike.events.entity;

import com.kike.events.constants.Availability;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Event extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int vendorId;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    private Long maxNumBookings;
    private Long currentNumBookings;

}
