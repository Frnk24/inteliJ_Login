package com.example.login.hash.repository;

import com.example.login.hash.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Busca todos los productos que pertenecen a un usuario con un email específico
    List<Producto> findByUsuarioEmail(String email);

    // Busca un producto por su ID Y que además pertenezca al usuario con ese email
    Optional<Producto> findByIdAndUsuarioEmail(Long id, String email);
}
