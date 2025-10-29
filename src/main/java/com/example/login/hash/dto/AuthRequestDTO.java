package com.example.login.hash.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String nombre;
    private String email;
    private String password;
}
