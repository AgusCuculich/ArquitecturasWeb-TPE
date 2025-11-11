package com.client;

import com.dtos.ReporteMonopatinesDTO;
import com.dtos.RideCountResultDTO;
import com.utils.EstadoMonopatin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Long> getScooterStats(int anio, int viajes) {
        String url = String.format("http://localhost:8089/rides/stats?anio=%d&viajes=%d", anio, viajes);
        RideCountResultDTO[] resp = restTemplate.getForObject(url, RideCountResultDTO[].class);
        if (resp == null || resp.length == 0) return Collections.emptyList();


        List<Long> ids = new ArrayList<>();
        for (RideCountResultDTO dto : resp) {
            System.out.println("ID: " + dto.getId_scooter());
            ids.add(dto.getId_scooter());
        }

        return ids;
    }

}
