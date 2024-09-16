package com.ebanking.mapper;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.models.UserEntity;

public class UserMapper {
    public static RegistrationDto mapToRegistrationDto(UserEntity user){
        return RegistrationDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .address(user.getAddress())
                .password(user.getHashedPassword())
                .build();
    }
}
