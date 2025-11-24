package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // Activa @PreAuthorize
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
                        // No necesitamos permitir /login aquí porque este microservicio no loguea.
                        // Solo permitimos /error por si Spring necesita reportar fallos.
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/monopatines/execute-sql").permitAll()
                        .requestMatchers(HttpMethod.POST, "/paradas/execute-sql").permitAll()

                        // TODO LO DEMÁS requiere token:
                        .anyRequest().authenticated()
                )
                // Desactivamos login básico para que no moleste
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}