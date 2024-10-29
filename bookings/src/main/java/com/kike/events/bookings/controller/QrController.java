package com.kike.events.bookings.controller;

import com.kike.events.bookings.service.IBookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST API for qr operations in FASTBOOK",
        description = "REST APIs qr operations in FASTBOOK"
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class QrController {

    private IBookingService bookingService;

    @GetMapping("/fetchQR/bookingId/{bookingId}")
    public ResponseEntity<byte[]> getQrImage(@PathVariable Long bookingId,
                                             @AuthenticationPrincipal Jwt jwt) {

        byte[] qrImage = bookingService.fetchQr(bookingId , jwt);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }
}
