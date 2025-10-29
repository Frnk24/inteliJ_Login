package com.example.login.hash.controller;

import com.example.login.hash.entity.Cliente;
import com.example.login.hash.entity.Usuario;
import com.example.login.hash.repository.ClienteRepository;
import com.example.login.hash.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- MÉTODOS CORRECTOS Y SEGUROS ---

    // LISTAR solo los clientes del usuario actual
    @GetMapping
    public List<Cliente> getAllClientes(Authentication authentication) {
        String userEmail = authentication.getName();
        return clienteRepository.findByUsuarioEmail(userEmail);
    }

    // OBTENER UN CLIENTE (verificando que sea del usuario actual)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        return clienteRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // CREAR un cliente y asignarlo al usuario actual
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente, Authentication authentication) {
        String userEmail = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }

    // ACTUALIZAR un cliente (verificando que sea del usuario actual)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails, Authentication authentication) {
        String userEmail = authentication.getName();
        return clienteRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(cliente -> {
                    cliente.setNombre(clienteDetails.getNombre());
                    cliente.setApellido(clienteDetails.getApellido());
                    cliente.setEmail(clienteDetails.getEmail());
                    return ResponseEntity.ok(clienteRepository.save(cliente));
                }).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // ELIMINAR un cliente (verificando que sea del usuario actual)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        return clienteRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}