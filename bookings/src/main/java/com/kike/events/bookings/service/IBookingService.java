package com.kike.events.bookings.service;

import com.kike.events.bookings.dto.BookingDto;


public interface IBookingService {

    void createBooking(BookingDto bookingDto);

//    BookingDto fetchBooking(Long userId, Long serviceId);
//
//    boolean updateBooking(BookingDto bookingDto);
//
//    void deleteBooking(Long id);
}
