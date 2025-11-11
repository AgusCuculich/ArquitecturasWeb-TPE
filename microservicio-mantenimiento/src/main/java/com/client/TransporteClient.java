package com.client;

import com.utils.EstadoMonopatin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TransporteClient {

    private final RestTemplate restTemplate;

    public void actualizarEstadoMonopatin(Long monopatinId, EstadoMonopatin estado) {

        String url = "http://localhost:8081/monopatines/{id}/estado?estado={estado}";

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                Void.class,
                monopatinId,
                estado
        );
    }
}
