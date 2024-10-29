package com.kike.events.bookings.mapper;

import com.kike.events.bookings.dto.BookingResponseDto;
import com.kike.events.bookings.entity.Booking;

public class BookingResponseMapper {

    public static BookingResponseDto mapToBookingDto(Booking booking, BookingResponseDto bookingResponseDto) {

        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setBookDate(booking.getBookDate());
        bookingResponseDto.setState(booking.getState());
        bookingResponseDto.setEventId(booking.getEventId());
        bookingResponseDto.setQrUUID(booking.getQrUUID());
        bookingResponseDto.setUserId(booking.getUserId());
        bookingResponseDto.setQrExpirationDate(booking.getQrExpirationDate());
        bookingResponseDto.setQrIsDeleted(booking.getQrIsDeleted());


        return bookingResponseDto;
    }


    public static Booking mapToBooking(BookingResponseDto bookingResponseDto, Booking booking) {

        booking.setId(bookingResponseDto.getId());
        booking.setEventId(bookingResponseDto.getEventId());
        booking.setQrUUID(bookingResponseDto.getQrUUID());
        booking.setUserId(bookingResponseDto.getUserId());
        booking.setBookDate(bookingResponseDto.getBookDate());
        booking.setState(bookingResponseDto.getState());
        booking.setQrExpirationDate(bookingResponseDto.getQrExpirationDate());
        booking.setQrIsDeleted(bookingResponseDto.getQrIsDeleted());

        return booking;
    }
}
