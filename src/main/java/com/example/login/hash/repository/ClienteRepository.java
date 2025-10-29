package com.example.login.hash.repository;

import com.example.login.hash.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByUsuarioEmail(String email);

    Optional<Cliente> findByIdAndUsuarioEmail(Long id, String email);
}
