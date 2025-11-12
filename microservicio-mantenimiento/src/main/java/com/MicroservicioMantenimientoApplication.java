package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication

public class MicroservicioMantenimientoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioMantenimientoApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplateMantenimiento() {
        return new RestTemplate();
    }
}
