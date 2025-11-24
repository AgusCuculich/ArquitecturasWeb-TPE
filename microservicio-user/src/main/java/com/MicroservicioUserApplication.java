package com;

import com.entity.User;
import com.repository.UserRepository;
import com.utils.PasswordUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.utils.Roles;

@SpringBootApplication
public class MicroservicioUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioUserApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
