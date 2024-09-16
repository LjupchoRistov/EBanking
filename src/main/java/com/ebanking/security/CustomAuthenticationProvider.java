package com.ebanking.security;

import com.ebanking.models.UserEntity;
import com.ebanking.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            List<GrantedAuthority> authorities = getAuthorities(username);

            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean authenticateUser(String username, String password) {
        //todo: get user from database
        UserEntity targetUser = this.userService.findByUsername(username);

        if (targetUser == null)
            return false;

        //todo: get database credentials
        String targetUserPassword = targetUser.getHashedPassword();
        String targetUserSalt = targetUser.getSalt();

        //todo: hash the input password
        String inputUserPassword = hashPassword(combinePasswordAndSalt(password, targetUserSalt.getBytes()));

        return targetUserPassword.equals(inputUserPassword);
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

    // todo: get user roles and create authorities
    private List<GrantedAuthority> getAuthorities(String username) {
        // todo: Retrieve user roles from the database or any other source
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }
}
