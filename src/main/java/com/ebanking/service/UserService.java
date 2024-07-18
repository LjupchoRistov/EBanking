package com.ebanking.service;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.dto.UserDisplayDto;
import com.ebanking.models.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(RegistrationDto registrationDto);

    void saveUser(UserEntity user);

    Optional<UserEntity> findByEmailOptional(String email);

    UserDisplayDto getUserDisplayDto(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsernameOptional(String username);

    boolean existsByUsername(String username);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAll();
}
