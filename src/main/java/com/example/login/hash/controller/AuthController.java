package com.example.login.hash.controller;

import com.example.login.hash.dto.AuthRequestDTO;
import com.example.login.hash.dto.AuthResponseDTO;

import com.example.login.hash.entity.Provider;
import com.example.login.hash.entity.Usuario;
import com.example.login.hash.service.JwtService;
import com.example.login.hash.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        // Retornamos un nuevo objeto AuthResponseDTO con el token
        return new AuthResponseDTO(token);
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody AuthRequestDTO registerRequest) {
        String encodePassword= passwordEncoder.encode(registerRequest.getPassword());
        Usuario newUser= new Usuario();
        newUser.setNombre(registerRequest.getNombre());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(encodePassword);
        newUser.setProvider(Provider.LOCAL);
        return usuarioService.registrarUsuarioLocal(newUser);
    }
}
