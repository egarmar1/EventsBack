package com.kike.events.bookings.mapper;

import com.kike.events.bookings.dto.BookingDto;
import com.kike.events.bookings.entity.Booking;

public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking, BookingDto bookingDto) {
        bookingDto.setUserId(booking.getUserId());
        bookingDto.setBookDate(booking.getBookDate());
        bookingDto.setState(booking.getState());
        bookingDto.setEventId(booking.getServiceId());

        return bookingDto;
    }


    public static Booking mapToBooking(BookingDto bookingDto, Booking booking) {
        booking.setBookDate(bookingDto.getBookDate());
        booking.setState(bookingDto.getState());
        booking.setUserId(bookingDto.getUserId());
        booking.setServiceId(bookingDto.getEventId());

        return booking;
    }
}
