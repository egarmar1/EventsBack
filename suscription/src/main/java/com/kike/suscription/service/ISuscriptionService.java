package com.kike.suscription.service;

import com.kike.suscription.dto.SuscriptionDto;

import java.util.List;

public interface ISuscriptionService {

    void createSuscription(SuscriptionDto suscriptionDto);

    List<String> fetchVendorIdsByClientId(String clientId);

    List<String> fetchClientIdsByVendorId(String vendorId);

    void deleteSuscription(String clientId, String vendorId);
}
