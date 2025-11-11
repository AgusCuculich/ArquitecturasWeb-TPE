package com.client;

import com.dto.UsuarioViajeCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RideClient {
    private final RestTemplate restTemplate;

    public List<UsuarioViajeCountDTO> obtenerUsuariosCantViajes(Date desde, Date hasta) {
        String url = String.format(
                "http://localhost:8089/rides/ranking-usuarios?desde=%tF&hasta=%tF",
                desde,hasta
        );

        UsuarioViajeCountDTO[] response = restTemplate.getForObject(url, UsuarioViajeCountDTO[].class);
        return Arrays.asList(response);
    }
}
