package com.kike.suscription.mapper;

import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.entity.Suscription;

public class SuscriptionMapper {

    public static SuscriptionDto mapToSuscriptionDto(Suscription suscription, SuscriptionDto suscriptionDto){
        suscriptionDto.setVendorId(suscription.getVendorId());
        suscriptionDto.setClientId(suscription.getClientId());

        return suscriptionDto;
    }

    public static Suscription mapToSuscription( SuscriptionDto suscriptionDto, Suscription suscription){
        suscription.setVendorId(suscriptionDto.getVendorId());
        suscription.setClientId(suscriptionDto.getClientId());

        return suscription;
    }
}
