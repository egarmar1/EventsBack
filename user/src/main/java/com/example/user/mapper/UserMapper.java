package com.example.user.mapper;

import com.example.user.dto.UserDto;
import com.example.user.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto){
        userDto.setUserId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setTypeId(user.getTypeId());

        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user){
        user.setId(userDto.getUserId());
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setTypeId(userDto.getTypeId());

        return user;
    }
}
