package com.example.login.hash.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class corsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todas las rutas del backend
                        .allowedOriginPatterns("*") // Permite cualquier origen (localhost, 127.0.0.1, etc.)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false) // Cambia a true si luego usas cookies o autenticación con sesión
                        .maxAge(3600); // Cache de la política CORS (1 hora)
            }
        };
    }
}
