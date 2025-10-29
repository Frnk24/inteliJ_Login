package com.example.login.hash.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String nombre;
    private String email;
    private String password;
}
