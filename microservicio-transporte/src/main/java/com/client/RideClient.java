package com.client;

import com.dtos.ReporteMonopatinesDTO;
import com.utils.EstadoMonopatin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RideClient {

    private final RestTemplate restTemplate;

    public List<ReporteMonopatinesDTO> generarReporte() {

        String url = "http://localhost:8082/monopatines/{id}/estado?estado={estado}";

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
