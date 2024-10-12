package com.kike.suscription.mapper;

import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.entity.Suscription;

public class SuscriptionMapper {

    public static SuscriptionDto mapToSuscriptionDto(Suscription suscription, SuscriptionDto suscriptionDto){
        suscriptionDto.setVendorId(suscription.getVendorId());
        suscriptionDto.setUserId(suscription.getUserId());

        return suscriptionDto;
    }

    public static Suscription mapToSuscription( SuscriptionDto suscriptionDto, Suscription suscription){
        suscription.setVendorId(suscriptionDto.getVendorId());
        suscription.setUserId(suscriptionDto.getUserId());

        return suscription;
    }
}
