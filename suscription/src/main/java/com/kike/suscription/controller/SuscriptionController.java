package com.kike.suscription.controller;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.service.ISuscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SuscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SuscriptionController.class);
    private ISuscriptionService suscriptionService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createSuscription(@Valid @RequestBody SuscriptionDto suscriptionDto) {

        ResponseDto eventResponseDto = suscriptionService.createSuscription(suscriptionDto);

        return ResponseEntity
                .status(CREATED)
                .body(eventResponseDto);
    }
}
