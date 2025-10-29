package com.example.login.hash.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY) // Muchos clientes pueden pertenecer a Un usuario.
    @JoinColumn(name = "usuario_id", nullable = false) // Esto crea la columna 'usuario_id' en la tabla 'cliente'.
    @JsonIgnore // Evita que al pedir un cliente, se traiga el usuario y se cree un bucle infinito.
    private Usuario usuario;
}
