package com.kike.suscription.dto.clients;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserTypeDto {

    private Long id;
    private String type;
}
