package com.kike.suscription.controller;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.service.ISuscriptionService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SuscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SuscriptionController.class);
    private ISuscriptionService suscriptionService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createSuscription(@Valid @RequestBody SuscriptionDto suscriptionDto) {

        suscriptionService.createSuscription(suscriptionDto);

        return ResponseEntity
                .status(CREATED)
                .body(new ResponseDto("200","OK"));
    }

    @GetMapping("/fetch/clientId/{clientId}")
    public ResponseEntity<List<String>> fetchSuscriptionsByClientId(@PathVariable String clientId) {

        List<String> vendorIds = suscriptionService.fetchVendorIdsByClientId(clientId);

        return ResponseEntity
                .status(OK)
                .body(vendorIds);
    }
    @GetMapping("/fetch/vendorId/{vendorId}")
    public ResponseEntity<List<String>> fetchSuscriptionsByVendorId(@PathVariable String vendorId) {

        List<String> clientIds = suscriptionService.fetchClientIdsByVendorId(vendorId);

        return ResponseEntity
                .status(OK)
                .body(clientIds);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteSuscription(@RequestParam String clientId, @RequestParam String vendorId) {

        suscriptionService.deleteSuscription(clientId, vendorId);

        return ResponseEntity
                .status(OK)
                .body(new ResponseDto("200", "Suscription deleted successfully"));
    }

}
