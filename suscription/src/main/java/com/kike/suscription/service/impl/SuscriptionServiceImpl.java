package com.kike.suscription.service.impl;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.entity.Suscription;
import com.kike.suscription.mapper.SuscriptionMapper;
import com.kike.suscription.service.ISuscriptionService;

public class SuscriptionServiceImpl implements ISuscriptionService {
    @Override
    public ResponseDto createSuscription(SuscriptionDto suscriptionDto) {

        SuscriptionMapper.mapToSuscription(suscriptionDto, new Suscription());


        return null;
    }
}
