package com.kike.events.bookings.service.impl;

import com.kike.events.bookings.dto.BookingDto;
import com.kike.events.bookings.dto.ResponseDto;
import com.kike.events.bookings.entity.Booking;
import com.kike.events.bookings.exception.BookingAlreadyExistsException;
import com.kike.events.bookings.exception.ResourceNotFoundException;
import com.kike.events.bookings.mapper.BookingMapper;
import com.kike.events.bookings.repository.BookingRepository;
import com.kike.events.bookings.service.IBookingService;
import com.kike.events.bookings.service.client.EventsFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private BookingRepository bookingRepository;
    private EventsFeignClient eventsFeignClient;
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override

    public void createBooking(BookingDto bookingDto, String userId) {

        Optional<Booking> existingBooking = bookingRepository.findByUserIdAndEventId(userId, bookingDto.getEventId());

        if (existingBooking.isPresent())
            throw new BookingAlreadyExistsException("The book with serviceId: " +
                    bookingDto.getEventId() + " and userId: " +
                    userId + " already exists");

        Booking booking = BookingMapper.mapToBooking(bookingDto, new Booking());
        booking.setUserId(userId);

        ResponseEntity<ResponseDto> responseDtoResponseEntity = eventsFeignClient.updateCurrentBookingsCount(bookingDto.getEventId());

        if (responseDtoResponseEntity.getStatusCode() == HttpStatus.OK)
            bookingRepository.save(booking);

    }

    @Override
    public BookingDto fetchbooking(Long eventId, String userId) {

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", userId + " and " + eventId + " respectively"));

        return BookingMapper.mapToBookingDto(booking, new BookingDto());
    }

    @Override
    public boolean updatebooking(BookingDto bookingDto, String userId) {


        Booking booking = bookingRepository.findByUserIdAndEventId(userId, bookingDto.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", userId + " " + bookingDto.getEventId() + " respectively"));


        BookingMapper.mapToBooking(bookingDto, booking);
        booking.setUserId(userId);

        bookingRepository.save(booking);

        return true;
    }

    @Override
    public void deletebooking(Long eventId, String userId) {
        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", userId + " " + eventId + " respectively"));

        bookingRepository.deleteById(booking.getId());
    }

//    @Override
//    public BookingDto fetchBooking(Long userId, Long serviceId) {
//        return null;
//    }
//
//    @Override
//    public boolean updateBooking(BookingDto bookingDto) {
//        return false;
//    }
//
//    @Override
//    public void deleteBooking(Long id) {
//
//    }
}
