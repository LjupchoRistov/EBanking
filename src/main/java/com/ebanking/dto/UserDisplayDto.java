package com.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDisplayDto {

    private Long id;

    private String username;

    private String email;

    private String name;

    private String surname;

    private String address;
}
