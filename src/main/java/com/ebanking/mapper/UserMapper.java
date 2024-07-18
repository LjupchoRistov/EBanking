package com.ebanking.mapper;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.dto.UserDisplayDto;
import com.ebanking.models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toUserEntity(RegistrationDto registrationDto) {

        Long id = registrationDto.getId();
        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String name = registrationDto.getName();
        String surname = registrationDto.getSurname();
        String address = registrationDto.getAddress();

        return new UserEntity(id, username, email, name, surname, address);
    }

    public UserDisplayDto toUserDisplayDto(UserEntity userEntity) {

        Long id = userEntity.getId();
        String username = userEntity.getUsername();
        String email = userEntity.getEmail();
        String name = userEntity.getName();
        String surname = userEntity.getSurname();
        String address = userEntity.getAddress();

        return new UserDisplayDto(id, username, email, name, surname, address);
    }
}
