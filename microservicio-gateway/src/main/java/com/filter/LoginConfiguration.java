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
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // 1. PERMITIR POST para crear usuarios a través del Gateway
                        .pathMatchers(HttpMethod.POST, "/users").permitAll()

                        // 2. PERMITIR POST para el login a través del Gateway
                        .pathMatchers(HttpMethod.POST, "/users/login").permitAll()

                        // 3. CUALQUIER OTRA SOLICITUD requiere un token (JWT)
                        .anyExchange().authenticated()
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        return http.build();
    }
}