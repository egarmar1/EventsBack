package com.kike.events.bookings.service;

import com.kike.events.bookings.constants.State;
import com.kike.events.bookings.dto.BookingCreateDto;
import com.kike.events.bookings.dto.BookingResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;


public interface IBookingService {

    void createBooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt);

    BookingResponseDto fetchbooking(Long eventId, String userId, Jwt jwt);

    boolean updatebooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt);

    void deleteBooking(Long eventId, String userId, Jwt jwt);

    void deleteAllbookingsWithEventId(Long eventId);

    void setBookingAsAttended(String qrToken, Jwt jwt);

    byte[] fetchQr(Long bookingId, Jwt jwt);

    List<BookingResponseDto> fetchBookingsOfUserByState(String userId, State state, Jwt jwt);

    void deleteExpiredQRCodes();

//    BookingDto fetchBooking(Long userId, Long serviceId);
//
//    boolean updateBooking(BookingDto bookingDto);
//
//    void deleteBooking(Long id);
}
