package com.example.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserTypeDto {

    @Schema(
            description = "Unique identifier of user type", example = "2"
    )
    @NotEmpty(message = "Id of the user type cannot be empty")
    private Long id;

    @Schema(
            description = "Type of the user", example = "vendor"
    )
    @NotEmpty(message = "User type cannot be empty")
    private String type;
}
