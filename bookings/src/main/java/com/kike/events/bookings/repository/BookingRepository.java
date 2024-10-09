package com.kike.events.bookings.repository;


import com.kike.events.bookings.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByUserIdAndEventId(String userId, Long serviceId);
}
