package com.client;

import com.dtos.ReporteMonopatinesDTO;
import com.utils.EstadoMonopatin;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RideClient {

    private final RestTemplate restTemplate;

    public List<ReporteMonopatinesDTO> obtenerReporte(Date inicio, Date fin, boolean incluirPausas) {
        String url = String.format(
                "http://localhost:8089/rides/reporte?inicio=%tF&fin=%tF&incluirPausas=%b",
                inicio, fin, incluirPausas
        );

        ReporteMonopatinesDTO[] response = restTemplate.getForObject(url, ReporteMonopatinesDTO[].class);
        return Arrays.asList(response);
    }

}
