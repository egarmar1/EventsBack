package com.kike.events.bookings.service;

import com.kike.events.bookings.dto.BookingDto;
import org.springframework.security.oauth2.jwt.Jwt;


public interface IBookingService {

    void createBooking(BookingDto bookingDto, String userId, Jwt jwt);

    BookingDto fetchbooking(Long eventId, String userId);

    boolean updatebooking(BookingDto bookingDto, String userId, Jwt jwt);

    void deleteBooking(Long eventId, String userId, Jwt jwt);

    void deleteAllbookingsWithEventId(Long eventId);

//    BookingDto fetchBooking(Long userId, Long serviceId);
//
//    boolean updateBooking(BookingDto bookingDto);
//
//    void deleteBooking(Long id);
}
