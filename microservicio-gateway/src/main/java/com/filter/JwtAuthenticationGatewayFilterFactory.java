package com.filter;


import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
// 1. NOMBRE: Debe terminar en "GatewayFilterFactory" para que el YAML lo encuentre.
// 2. EXTENDS: Debe extender de AbstractGatewayFilterFactory (Reactivo), no de OncePerRequestFilter.
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private final String PREFIX = "Bearer ";
    private final String SECRET = "mySecretKey"; // Misma clave que en el Login

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Validar que exista el header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "No hay header de autorización");
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // 2. Validar formato Bearer
            if (authHeader == null || !authHeader.startsWith(PREFIX)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Formato de token inválido");
            }

            // 3. Validar Token y Firma
            try {
                String token = authHeader.replace(PREFIX, "");

                // Validación tal cual el PDF pero sin "getBody()" final para solo validar firma
                Jwts.parser()
                        .setSigningKey(SECRET.getBytes())
                        .parseClaimsJws(token);

                // Si pasa, dejamos pasar la petición al microservicio destino
                return chain.filter(exchange);

            } catch (Exception e) {
                // Si falla (expirado, firma mal, etc), devolvemos 403
                return onError(exchange, HttpStatus.FORBIDDEN, "Token inválido o expirado");
            }
        };
    }

    // Método auxiliar para manejar errores en modo Reactivo (Mono)
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {
        // Configuración vacía requerida
    }
}