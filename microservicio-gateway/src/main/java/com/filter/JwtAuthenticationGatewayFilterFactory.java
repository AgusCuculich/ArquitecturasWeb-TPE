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

                // 🛑 LÓGICA DE EXCLUSIÓN AÑADIDA 🛑
                String path = exchange.getRequest().getURI().getPath();

                // Definir las rutas públicas (deben coincidir con las reglas del SecurityConfig)
                // Se utiliza startsWith porque la ruta puede ser /users, /users/, /users?param=x
                boolean isPublicRoute =
                        path.startsWith("/users") && exchange.getRequest().getMethod().matches("POST");

                // Excluir la ruta específica de login
                boolean isLogin = path.equals("/users/login") || path.equals("/users/login/");

                if (isPublicRoute || isLogin) {
                    // Si la ruta es pública, saltamos la verificación del token y dejamos pasar.
                    return chain.filter(exchange);
                }
                // 🛑 FIN LÓGICA DE EXCLUSIÓN 🛑

                // 1. Validar que exista el header (Solo si la ruta no es pública)
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED, "No hay header de autorización");
                }
                // ... el resto de la lógica del filtro sigue igual ...

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                // 2. Validar formato Bearer
                if (authHeader == null || !authHeader.startsWith(PREFIX)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED, "Formato de token inválido");
                }

                // 3. Validar Token y Firma
                try {
                    // ... (lógica de verificación del token)
                    String token = authHeader.replace(PREFIX, "");
                    Jwts.parser()
                            .setSigningKey(SECRET.getBytes())
                            .parseClaimsJws(token);

                    return chain.filter(exchange);

                } catch (Exception e) {
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