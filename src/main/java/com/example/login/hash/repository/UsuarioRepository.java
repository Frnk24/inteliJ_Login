package com.example.login.hash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.login.hash.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
