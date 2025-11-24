package com.client;

import com.utils.EstadoMonopatin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class TransporteClient {

    private final RestTemplate restTemplate;
    public TransporteClient(@Qualifier("restTemplateMantenimiento") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void actualizarEstadoMonopatin(Long monopatinId, EstadoMonopatin estado) {

        // Usamos placeholders {id} y {estado} para que RestTemplate los sustituya
        String url = "http://localhost:8081/monopatines/{id}/estado?estado={estado}";

        // 1. Preparamos el Header con el Token
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Enviamos la petición autenticada
        // Fíjate que pasamos 'requestEntity' en lugar de 'null'
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Void.class,
                monopatinId, // Sustituye {id}
                estado       // Sustituye {estado}
        );
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
            System.err.println("Error al propagar el token en TransporteClient: " + e.getMessage());
        }

        return new HttpEntity<>(headers);
    }
}