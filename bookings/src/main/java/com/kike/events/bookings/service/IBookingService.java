package com.kike.events.bookings.service;

import com.kike.events.bookings.dto.BookingDto;


public interface IBookingService {

    void createBooking(BookingDto bookingDto, String userId);

    BookingDto fetchbooking(Long eventId, String userId);

    boolean updatebooking(BookingDto bookingDto, String userId);

    void deletebooking(Long eventId, String userId);

//    BookingDto fetchBooking(Long userId, Long serviceId);
//
//    boolean updateBooking(BookingDto bookingDto);
//
//    void deleteBooking(Long id);
}
