package com.example.oftapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del Microservicio Clínico OftApp (Spring Boot 3).
 * Expone endpoints REST CRUD documentados con OpenAPI/Swagger UI en:
 * http://localhost:8080/swagger-ui/index.html
 */
@SpringBootApplication
public class OftAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OftAppBackendApplication.class, args);
    }
}
