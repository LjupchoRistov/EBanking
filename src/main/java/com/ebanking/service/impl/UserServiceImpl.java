package com.ebanking.service.impl;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.models.Role;
import com.ebanking.models.UserEntity;
import com.ebanking.repository.RoleRepository;
import com.ebanking.repository.UserRepository;
import com.ebanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private static AtomicLong counter = new AtomicLong(1);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //todo: implement SALT creation
    private String createSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Convert the byte array to a Base64-encoded string
        return Base64.getEncoder().encodeToString(salt);
    }

    //todo: combine pass and salt
    private static String combinePasswordAndSalt(String password, byte[] salt) {
        return password + Base64.getEncoder().encodeToString(salt);
    }

    //todo: hash password
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
        UserEntity user = new UserEntity();
        String salt = createSalt();
        String pinSalt=createSalt();

        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        //todo: set SALT
        user.setSalt(salt);
        user.setPinSalt(pinSalt);
        //todo: set HASHED PASSWORD
        user.setHashedPassword(hashPassword(combinePasswordAndSalt(registrationDto.getPassword(), salt.getBytes())));
        user.setHashPin(hashPassword(combinePasswordAndSalt(registrationDto.getPin(),pinSalt.getBytes())));
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }


    @Override
    public void saveUser(UserEntity user) {
        this.userRepository.save(user);
    }


    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean checkPin(String pin, UserEntity user) {
        String storedSalt=user.getPinSalt();
        String storedHashPin=user.getHashPin();
        String combinePinAndSalt=combinePasswordAndSalt(pin,storedSalt.getBytes());
        String hashedPin=hashPassword(combinePinAndSalt);

        return hashedPin.equals(storedHashPin);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findById(Long id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }
}
