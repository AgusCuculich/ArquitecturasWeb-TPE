package com.config;

import com.config.JWTAuthorizationFilter; // Asegúrate de tener este filtro (el que extiende OncePerRequestFilter)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class LoginConfiguration {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public LoginConfiguration(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. PERMITIR POST para /users (CREACIÓN DE USUARIO) <--- LÍNEA AÑADIDA
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // 2. PERMITIR POST para /users/login (INICIO DE SESIÓN)
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()

                        // Por si acaso, permitimos el /login sin /users (solo si existe esa ruta)
                        // .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        // Cualquier otra petición debe estar autenticada
                        .anyRequest().authenticated()
                )
                // Asegúrate de que este filtro solo se añada si es un endpoint que requiere autenticación,
                // pero si lo dejas aquí, está bien ya que el filtro puede ignorar rutas permitidas.
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}