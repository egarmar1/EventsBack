package com.kike.events.bookings.mapper;

import com.kike.events.bookings.dto.BookingCreateDto;
import com.kike.events.bookings.entity.Booking;

public class BookingCreateMapper {

    public static BookingCreateDto mapToBookingDto(Booking booking, BookingCreateDto bookingCreateDto) {


        bookingCreateDto.setBookDate(booking.getBookDate());
        bookingCreateDto.setState(booking.getState());
        bookingCreateDto.setEventId(booking.getEventId());

        return bookingCreateDto;
    }


    public static Booking mapToBooking(BookingCreateDto bookingCreateDto, Booking booking) {

        booking.setBookDate(bookingCreateDto.getBookDate());
        booking.setState(bookingCreateDto.getState());
        booking.setEventId(bookingCreateDto.getEventId());

        return booking;
    }
}
