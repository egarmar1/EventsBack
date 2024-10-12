package com.kike.events.bookings.service.impl;

import com.kike.events.bookings.dto.BookingDto;
import com.kike.events.bookings.dto.ResponseDto;
import com.kike.events.bookings.entity.Booking;
import com.kike.events.bookings.exception.BookingAlreadyExistsException;
import com.kike.events.bookings.exception.ResourceNotFoundException;
import com.kike.events.bookings.exception.UnauthorizedException;
import com.kike.events.bookings.mapper.BookingMapper;
import com.kike.events.bookings.repository.BookingRepository;
import com.kike.events.bookings.service.IBookingService;
import com.kike.events.bookings.service.auth.JwtService;
import com.kike.events.bookings.service.client.EventsFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private BookingRepository bookingRepository;
    private EventsFeignClient eventsFeignClient;
    private JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override
    public void createBooking(BookingDto bookingDto, String userId, Jwt jwt) {

        if(jwt == null)
            throw new UnauthorizedException("Oauth2 access token is required");

        String actualUserId = jwtService.getRealmRoles(jwt).contains("admin") ?
                userId : jwtService.getUserId(jwt);

        Optional<Booking> existingBooking = bookingRepository.findByUserIdAndEventId(actualUserId, bookingDto.getEventId());

        if (existingBooking.isPresent())
            throw new BookingAlreadyExistsException("The book with serviceId: " +
                    bookingDto.getEventId() + " and userId: " +
                    actualUserId + " already exists");

        Booking booking = BookingMapper.mapToBooking(bookingDto, new Booking());
        booking.setUserId(actualUserId);

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
    public boolean updatebooking(BookingDto bookingDto, String userId, Jwt jwt) {

        if(jwt == null)
            throw new UnauthorizedException("Oauth2 access token is required");

        String actualUserId = jwtService.getRealmRoles(jwt).contains("admin") ?
                userId : jwtService.getUserId(jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, bookingDto.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " " + bookingDto.getEventId() + " respectively"));


        BookingMapper.mapToBooking(bookingDto, booking);
        booking.setUserId(userId);

        bookingRepository.save(booking);

        return true;
    }

    @Override
    public void deleteBooking(Long eventId, String userId, Jwt jwt) {


        if(jwt == null)
            throw new UnauthorizedException("Oauth2 access token is required");

        // If the user is admin, then it chooses the userId, if not it's own userId is taken
        String actualUserId = jwtService.getRealmRoles(jwt).contains("admin") ?
                userId : jwtService.getUserId(jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " " + eventId + " respectively"));

        bookingRepository.deleteById(booking.getId());
    }

    @Override
    @Transactional
    public void deleteAllbookingsWithEventId(Long eventId) {
        List<Booking> bookings = bookingRepository.findByEventId(eventId);

        bookings.forEach(booking -> bookingRepository.deleteById(booking.getId()));
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
