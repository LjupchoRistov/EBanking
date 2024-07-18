package com.ebanking.service.impl;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.dto.UserDisplayDto;
import com.ebanking.mapper.UserMapper;
import com.ebanking.models.Role;
import com.ebanking.models.UserEntity;
import com.ebanking.repository.RoleRepository;
import com.ebanking.repository.UserRepository;
import com.ebanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    private static final AtomicLong counter = new AtomicLong(1);

    //implement SALT creation
    private String createSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Convert the byte array to a Base64-encoded string
        return Base64.getEncoder().encodeToString(salt);
    }

    //combine pass and salt
    private static String combinePasswordAndSalt(String password, byte[] salt) {
        return password + Base64.getEncoder().encodeToString(salt);
    }

    //hash password
    private static String hashPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {

        UserEntity user = userMapper.toUserEntity(registrationDto);

        String salt = createSalt();

        String hashedPassword = hashPassword(combinePasswordAndSalt(registrationDto.getPassword(), salt.getBytes()));

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
