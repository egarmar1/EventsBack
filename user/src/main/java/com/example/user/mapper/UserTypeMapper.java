package com.example.user.mapper;

import com.example.user.dto.UserTypeDto;
import com.example.user.entity.UserType;

public class UserTypeMapper {
    public static UserTypeDto mapToUserTypeDto(UserType userType, UserTypeDto userTypeDto){
        userTypeDto.setId(userType.getId());
        userTypeDto.setType(userType.getType());

        return userTypeDto;
    }

    public static UserType mapToUserType(UserTypeDto userTypeDto, UserType userType){
        userType.setId(userTypeDto.getId());
        userType.setType(userTypeDto.getType());

        return userType;
    }
}
