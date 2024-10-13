package com.kike.suscription.service.impl;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.entity.Suscription;
import com.kike.suscription.exception.SuscriptionAlreadyExistsException;
import com.kike.suscription.mapper.SuscriptionMapper;
import com.kike.suscription.repository.SuscriptionRepository;
import com.kike.suscription.service.ISuscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SuscriptionServiceImpl implements ISuscriptionService {

    SuscriptionRepository suscriptionRepository;

    @Override
    public ResponseDto createSuscription(SuscriptionDto suscriptionDto) {

        String userId = suscriptionDto.getUserId();
        String vendorId = suscriptionDto.getVendorId();

        suscriptionRepository.findByUserIdAndVendorId(userId, vendorId)
                .orElseThrow(() -> new SuscriptionAlreadyExistsException(userId, vendorId));

        SuscriptionMapper.mapToSuscription(suscriptionDto, new Suscription());


        return null;
    }
}
