package com.kike.eventsHistory.exception;

import com.kike.eventsHistory.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = EventsHistoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEventsHistoryAlreadyExistsException(Exception exc, WebRequest webReq){

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webReq.getDescription(false),
                HttpStatus.CONFLICT,
                exc.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
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


}
