package com.example.login.hash.dto;

import com.example.login.hash.entity.Provider;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private Provider provider;
}
