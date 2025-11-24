package com.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    public List<Long> getOtherUsers(Long userId) {
        String url = String.format("http://localhost:8088/users/accounts/%d", userId);

        // 1. Preparamos el Header con el Token
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Usamos exchange para enviar la petición autenticada
        ResponseEntity<Long[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Long[].class
        );

        // 3. Procesamos la respuesta
        Long[] body = response.getBody();

        // System.out.println(Arrays.toString(body)); // Debug opcional

        if (body == null || body.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.asList(body);
    }

    // ==========================================
    // MÉTODO AUXILIAR (Privado)
    // Extrae el token de la petición actual y crea los headers
    // ==========================================
    private HttpEntity<Void> createRequestEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();

        try {
            // Obtenemos el contexto de la petición actual
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                // Extraemos el header "Authorization" completo
                String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

                if (authHeader != null) {
                    headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al propagar el token en UserClient: " + e.getMessage());
        }

        return new HttpEntity<>(headers);
    }
}