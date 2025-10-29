package com.example.login.hash.controller;

import com.example.login.hash.dto.ChangePasswordRequestDTO;
import com.example.login.hash.dto.MensajeResp;
import com.example.login.hash.dto.UsuarioResponseDTO;
import com.example.login.hash.entity.Usuario;
import com.example.login.hash.repository.UsuarioRepository;
import com.example.login.hash.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @GetMapping("/me")
    public ResponseEntity<Usuario> getUsuarioActual(Authentication authentication) {
        // Spring Security nos da el email del usuario autenticado (del token JWT)
        String userEmail = authentication.getName();

        // Usamos el servicio para buscar al usuario completo en la base de datos
        Usuario usuario = (Usuario) usuarioService.loadUserByUsername(userEmail);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordRequestDTO request) {
        try {
            String userEmail = authentication.getName();
            usuarioService.changePassword(userEmail, request);
            return ResponseEntity.ok(new MensajeResp("200", "Contraseña cambiada con éxito"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MensajeResp("401", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeResp("400", e.getMessage()));
        }
    }

    private UsuarioResponseDTO convertToDto(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setProvider(usuario.getProvider());
        return dto;
    }

    @GetMapping
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
