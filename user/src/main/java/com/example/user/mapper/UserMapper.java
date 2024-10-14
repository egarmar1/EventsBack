package com.example.user.mapper;

import com.example.user.dto.UserDto;
import com.example.user.entity.Users;

public class UserMapper {

    public static UserDto mapToUserDto(Users user, UserDto userDto){
        userDto.setUserId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setTypeId(user.getTypeId());

        return userDto;
    }

    public static Users mapToUser(UserDto userDto, Users user){
        user.setId(userDto.getUserId());
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setTypeId(userDto.getTypeId());

        return user;
    }
}
