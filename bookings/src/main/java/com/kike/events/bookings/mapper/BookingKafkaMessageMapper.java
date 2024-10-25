package com.kike.events.bookings.mapper;

import com.kike.events.bookings.dto.BookingKafkaMessageDto;
import com.kike.events.bookings.dto.BookingResponseDto;
import com.kike.events.bookings.entity.Booking;

public class BookingKafkaMessageMapper {

    public static BookingKafkaMessageDto mapToBookingDto(Booking booking, BookingKafkaMessageDto bookingKafkaMessageDto) {

        bookingKafkaMessageDto.setId(booking.getId());
        bookingKafkaMessageDto.setBookDate(booking.getBookDate());
        bookingKafkaMessageDto.setState(booking.getState());
        bookingKafkaMessageDto.setEventId(booking.getEventId());
        bookingKafkaMessageDto.setQrUUID(booking.getQrUUID());
        bookingKafkaMessageDto.setUserId(booking.getUserId());

        return bookingKafkaMessageDto;
    }


    public static Booking mapToBooking(BookingKafkaMessageDto bookingKafkaMessageDto, Booking booking) {

        booking.setId(bookingKafkaMessageDto.getId());
        booking.setEventId(bookingKafkaMessageDto.getEventId());
        booking.setQrUUID(bookingKafkaMessageDto.getQrUUID());
        booking.setUserId(bookingKafkaMessageDto.getUserId());
        booking.setBookDate(bookingKafkaMessageDto.getBookDate());
        booking.setState(bookingKafkaMessageDto.getState());

        return booking;
    }
}
