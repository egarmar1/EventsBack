package com.kike.events.bookings.entity;

import com.kike.events.bookings.constants.State;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long serviceId;
    private LocalDateTime bookDate;
    @Enumerated(EnumType.STRING)
    private State state;


}
