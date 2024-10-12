package com.kike.suscription.service;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;

public interface ISuscriptionService {

    ResponseDto createSuscription(SuscriptionDto suscriptionDto);
}
