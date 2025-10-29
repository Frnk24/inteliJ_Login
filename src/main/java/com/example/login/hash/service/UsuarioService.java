package com.example.login.hash.service;


import com.example.login.hash.dto.ChangePasswordRequestDTO;
import com.example.login.hash.entity.Usuario;
import com.example.login.hash.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.example.login.hash.entity.Provider;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> ListarUsuario() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarLoggin(String user) {
        return usuarioRepository.findByEmail(user).orElse(null);
    }

    public Usuario registrarUsuarioLocal(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminarU(int id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email) // Cambia "findByUser" por el nombre de tu método
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado email: " + email));
    }

    public Usuario procesarUsuarioOAuth2(OAuth2User oauth2User, Provider provider){
        String email=oauth2User.getAttribute("email");
        String nombre=oauth2User.getAttribute("name");

        Optional<Usuario> usuarioExistente=usuarioRepository.findByEmail(email);

        if (usuarioExistente.isPresent()){
            return usuarioExistente.get();
        }else{
            Usuario nuevoUsuario=new Usuario();
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setProvider(provider);
            return usuarioRepository.save(nuevoUsuario);
        }
    }

    public void changePassword(String userEmail, ChangePasswordRequestDTO request) {
        // 1. Busca al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 2. Verifica que sea un usuario LOCAL (los de Google/Facebook no pueden cambiar contraseña)
        if (usuario.getProvider() != Provider.LOCAL) {
            throw new IllegalStateException("Solo los usuarios locales pueden cambiar su contraseña.");
        }

        // 3. Verifica que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("La contraseña actual es incorrecta.");
        }

        // 4. Verifica que la nueva contraseña y la confirmación coincidan
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalArgumentException("La nueva contraseña y la confirmación no coinciden.");
        }

        // 5. Hashea y actualiza la nueva contraseña
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 6. Guarda los cambios en la base de datos
        usuarioRepository.save(usuario);
    }
}
