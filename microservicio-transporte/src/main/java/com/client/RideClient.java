package com.client;

import com.dtos.ReporteMonopatinesDTO;
import com.dtos.RideCountResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Component
public class RideClient {

    private final RestTemplate restTemplate;

    // Inyección de dependencia del RestTemplate
    public RideClient(@Qualifier("restTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // --- MÉTODO 1: Obtener Reporte ---
    public List<ReporteMonopatinesDTO> obtenerReporte(Date inicio, Date fin, boolean incluirPausas) {
        String url = String.format(
                "http://localhost:8089/rides/reporte?inicio=%tF&fin=%tF&incluirPausas=%b",
                inicio, fin, incluirPausas
        );

        // 1. Preparamos el Header con el Token
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Usamos exchange para enviar el header
        ResponseEntity<ReporteMonopatinesDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                ReporteMonopatinesDTO[].class
        );

        // 3. Retornamos la lista (validando nulos)
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }

    // --- MÉTODO 2: Estadísticas de Scooters ---
    public List<Long> getScooterStats(int anio, int viajes) {
        String url = String.format("http://localhost:8089/rides/stats?anio=%d&viajes=%d", anio, viajes);

        // 1. Preparamos el Header con el Token
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Usamos exchange
        ResponseEntity<RideCountResultDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                RideCountResultDTO[].class
        );

        RideCountResultDTO[] respBody = response.getBody();

        if (respBody == null || respBody.length == 0) {
            return Collections.emptyList();
        }

        // 3. Procesamos la respuesta para obtener solo los IDs
        List<Long> ids = new ArrayList<>();
        for (RideCountResultDTO dto : respBody) {
            // System.out.println("ID: " + dto.getId_scooter()); // Debug opcional
            ids.add(dto.getId_scooter());
        }

        return ids;
    }

    // ==========================================
    // MÉTODO AUXILIAR (Privado)
    // Extrae el token de la petición actual y crea los headers
    // ==========================================
    private HttpEntity<Void> createRequestEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();

        try {
            // Obtenemos el contexto de la petición actual (el request que llegó al microservicio)
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                // Extraemos el header "Authorization" completo
                String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

                if (authHeader != null) {
                    headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al propagar el token en RideClient: " + e.getMessage());
        }

        return new HttpEntity<>(headers);
    }
}