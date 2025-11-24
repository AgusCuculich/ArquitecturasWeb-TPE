package com.client;

import com.dto.ReporteTarifaDTO;
import com.dto.UsuarioViajeCountDTO;
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
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RideClient {

    private final RestTemplate restTemplate;

    // --- MÉTODO 1: Lista de Usuarios ---
    public List<UsuarioViajeCountDTO> obtenerUsuariosCantViajes(Date desde, Date hasta) {
        String url = String.format(
                "http://localhost:8089/rides/ranking-usuarios?desde=%tF&hasta=%tF",
                desde, hasta
        );

        // 1. Preparamos la petición con el Token inyectado
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Usamos 'exchange' en lugar de 'getForObject' para enviar los headers
        ResponseEntity<UsuarioViajeCountDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                UsuarioViajeCountDTO[].class
        );

        // 3. Retornamos el cuerpo de la respuesta (si es nulo, devolvemos lista vacía)
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }

    // --- MÉTODO 2: Reporte de Tarifas ---
    public ReporteTarifaDTO reporteTarifas(Date desde, Date hasta){
        String url = String.format(
                "http://localhost:8089/rides/totalFacturado?inicio=%tF&fin=%tF",
                desde, hasta
        );

        // 1. Preparamos la petición con el Token
        HttpEntity<Void> requestEntity = createRequestEntityWithToken();

        // 2. Usamos 'exchange'
        ResponseEntity<ReporteTarifaDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                ReporteTarifaDTO.class
        );

        // 3. Retornamos el objeto
        return response.getBody();
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
                // Extraemos el header "Authorization" completo (ej: "Bearer eyJhb...")
                String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

                if (authHeader != null) {
                    headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al propagar el token: " + e.getMessage());
        }

        return new HttpEntity<>(headers);
    }
}