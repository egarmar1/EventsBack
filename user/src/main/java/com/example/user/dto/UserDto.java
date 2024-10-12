package com.example.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {


    @Schema(description = "Unique identifier for the user from keycloak", example = "1mp12dfdge2ga28k")
    @NotEmpty(message = "User id cannot be empty")
    private String userId;

    @Schema(
            description = "Full name of the user ", example = "Mike Williams "
    )
    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;


    @Schema(
            description = "Email of the user ", example = "williams32@gmail.com"
    )
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Schema(
            description = "Unique identifier for the type of user", example = "2"
    )
    @NotEmpty(message = "Full name cannot be empty")
    private Long typeId;
}
