package com.kike.events.bookings.service;

import com.kike.events.bookings.dto.BookingCreateDto;
import com.kike.events.bookings.dto.BookingResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;


public interface IBookingService {

    void createBooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt);

    BookingResponseDto fetchbooking(Long eventId, String userId);

    boolean updatebooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt);

    void deleteBooking(Long eventId, String userId, Jwt jwt);

    void deleteAllbookingsWithEventId(Long eventId);

    void setBookingAsAttended(Long eventId, String userId, Jwt jwt);

    byte[] fetchQr(Long bookingId, Jwt jwt);

//    BookingDto fetchBooking(Long userId, Long serviceId);
//
//    boolean updateBooking(BookingDto bookingDto);
//
//    void deleteBooking(Long id);
}
