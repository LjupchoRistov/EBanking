package com.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    private Long id;
    @NotEmpty(message = "Username cannot be empty!")
    private String username;
    @NotEmpty(message = "Email cannot be empty!")
    private String email;
    @NotEmpty(message = "Name cannot be empty!")
    private String name;
    @NotEmpty(message = "Surname cannot be empty!")
    private String surname;
    @NotEmpty(message = "Address cannot be empty!")
    private String address;
    @NotEmpty(message = "Password cannot be empty!")
    private String password;
    private String repeatPassword;

    public String getRP(){
        return repeatPassword;
    }
}

