package com.ebanking.dto;

public record UserDisplayDto(

        Long id,

        String username,

        String email,

        String name,

        String surname,

        String address

) {
}
