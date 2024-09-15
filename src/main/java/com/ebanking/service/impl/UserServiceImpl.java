package com.ebanking.service.impl;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.dto.UserDisplayDto;
import com.ebanking.mapper.UserMapper;
import com.ebanking.models.Role;
import com.ebanking.models.UserEntity;
import com.ebanking.repository.RoleRepository;
import com.ebanking.repository.UserRepository;
import com.ebanking.service.EncryptDataService;
import com.ebanking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private EncryptDataService encryptDataService;

    @Override
    public void saveUser(RegistrationDto registrationDto) {

        UserEntity user = userMapper.toUserEntity(registrationDto);

        String salt = encryptDataService.createSalt();

        String password = registrationDto.getPassword();

        byte[] saltInBytes = salt.getBytes();

        String combinedPasswordAndSalt = encryptDataService.combinePasswordAndSalt(password, saltInBytes);

        String hashedPassword = encryptDataService.hashPassword(combinedPasswordAndSalt);

        Role role = roleRepository.findByName("USER");

        List<Role> roles = Collections.singletonList(role);

        user.setHashedPassword(hashedPassword);

        user.setSalt(salt);

        user.setRoles(roles);

        userRepository.save(user);
    }


    @Override
    public void saveUser(UserEntity user) {
        this.userRepository.save(user);
    }


    @Override
    public Optional<UserEntity> findByEmailOptional(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public UserDisplayDto getUserDisplayDto(String username) {

        Optional<UserEntity> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        UserEntity user = userOptional.get();

        return userMapper.toUserDisplayDto(user);
    }

    @Override
    public boolean existsByEmail(String email) {

        Optional<UserEntity> user = userRepository.findByEmail(email);

        return user.isPresent();
    }

    @Override
    public Optional<UserEntity> findByUsernameOptional(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {

        Optional<UserEntity> user = userRepository.findByUsername(username);

        return user.isPresent();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {

        return this.userRepository.findById(id);
    }

    @Override
    public List<UserEntity> findAll() {

        return this.userRepository.findAll();
    }
}
