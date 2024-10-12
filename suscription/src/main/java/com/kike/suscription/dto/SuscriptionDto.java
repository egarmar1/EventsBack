package com.kike.suscription.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SuscriptionDto {


    @Schema(description = "Unique identifier for the user", example = "1mp12dfdge2ga28k")
    @NotEmpty(message = "User id cannot be empty")
    private String userId;

    @Schema(description = "Unique identifier for the vendor", example = "lkaz91odwlkfp12df4")
    @NotEmpty(message = "Vendor id cannot be empty")
    private String vendorId;
}
