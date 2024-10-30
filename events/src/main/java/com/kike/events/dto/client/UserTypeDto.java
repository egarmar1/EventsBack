package com.kike.events.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTypeDto {

    public UserTypeDto(String type){
        this.type = type;
    }
    private Long id;
    private String type;
}
