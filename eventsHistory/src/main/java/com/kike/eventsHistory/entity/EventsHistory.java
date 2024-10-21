package com.kike.eventsHistory.entity;

import com.kike.eventsHistory.constants.HistoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class EventsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Long eventId;
    @Enumerated(EnumType.STRING)
    private HistoryType historyType;
}
