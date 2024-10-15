package com.kike.notifications.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "suscription")
public interface SuscriptionFeignClient {

    @GetMapping("/api/fetch/vendorId/{vendorId}")
    public ResponseEntity<List<String>> fetchSuscriptionsByVendorId(@PathVariable String vendorId);
}
