package com.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Dentro de com.filter.LoginConfiguration.java del proyecto API GATEWAY

@Configuration
@EnableWebFluxSecurity
public class LoginConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // 1. AUTORIZACIÓN: Permitimos TODO.
                // ¿Por qué? Porque el filtro "JwtAuthentication" que pusiste en el YAML
                // es el que va a bloquear si no hay token. Si aquí pones ".authenticated()",
                // Spring bloqueará antes de que tu filtro actúe.
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )

                // 2. LO MÁS IMPORTANTE: Desactivar el Login Básico
                // Esto es lo que quitara el error "Basic Realm" y el 401 automático.
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        return http.build();
    }

}