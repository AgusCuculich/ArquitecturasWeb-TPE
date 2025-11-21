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

    @Bean
    CommandLineRunner initData(UserRepository repo) {
        return args -> {
            if (repo.findByName("admin") == null) {
                User admin = new User();
                admin.setName("admin");
                admin.setSurname("Admin");
                admin.setRol(Roles.ADMINISTRADOR); // O el rol que tengas en tu Enum
                // ¡Aquí está la magia! Encriptamos antes de guardar
                admin.setPassword(PasswordUtils.hashPassword("1234"));
                repo.save(admin);
                System.out.println("Usuario 'admin' creado con pass '1234'");
            }
        };
    }

}
