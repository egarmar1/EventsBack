package com.kike.events.bookings.exception;

import com.kike.events.bookings.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exc, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorResponseDto = new ErrorResponseDto(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exc, WebRequest webReq){

        var errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exc.getMessage(),
                LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponseDto);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(Exception exc, WebRequest webReq){

        var errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(Exception exc, WebRequest webReq){

        var errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.NOT_FOUND,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponseDto);
    }

    @ExceptionHandler(value = BookingAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingAlreadyExistsException(Exception exc, WebRequest webReq){

        var errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.CONFLICT,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponseDto);
    }

    @ExceptionHandler(value = MissingUserIdFromAdminException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingUserIdFromAdminException(Exception exc, WebRequest webReq){

        var errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }


}
