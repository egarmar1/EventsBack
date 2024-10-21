package com.kike.eventsHistory.repository;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.entity.EventsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventsHistoryRepository extends JpaRepository<EventsHistory,Long> {

    Optional<EventsHistory> findByUserIdAndEventId(String userId, Long eventId);

    Optional<EventsHistory> findByUserId();
}
