package com.example.login.hash.controller;

import com.example.login.hash.entity.Producto;
import com.example.login.hash.entity.Usuario;
import com.example.login.hash.repository.ProductoRepository;
import com.example.login.hash.repository.UsuarioRepository; // Importa el repo de usuarios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importa Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyecta el repo de usuarios para poder buscar al dueño

    // LISTAR solo los productos del usuario actual
    @GetMapping
    public List<Producto> getAllProductos(Authentication authentication) {
        String userEmail = authentication.getName();
        return productoRepository.findByUsuarioEmail(userEmail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        return productoRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build()); // 403 Forbidden si no es suyo
    }

    // CREAR un producto y asignarlo al usuario actual
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto, Authentication authentication) {
        String userEmail = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado, no se puede crear el producto."));

        producto.setUsuario(usuario); // Asigna el dueño al producto
        return productoRepository.save(producto);
    }

    // ACTUALIZAR un producto (verificando que sea del usuario actual)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails, Authentication authentication) {
        String userEmail = authentication.getName();
        return productoRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(producto -> {
                    producto.setProducto(productoDetails.getProducto());
                    producto.setDescripcion(productoDetails.getDescripcion());
                    producto.setPrecioUnitario(productoDetails.getPrecioUnitario());
                    producto.setStock(productoDetails.getStock());
                    return ResponseEntity.ok(productoRepository.save(producto));
                }).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // ELIMINAR un producto (verificando que sea del usuario actual)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        return productoRepository.findByIdAndUsuarioEmail(id, userEmail)
                .map(producto -> {
                    productoRepository.delete(producto);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}