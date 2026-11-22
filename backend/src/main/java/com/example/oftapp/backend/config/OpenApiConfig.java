package com.example.oftapp.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de Springdoc OpenAPI / Swagger UI y CORS.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI oftAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OftApp — Microservicios Clínicos Oftalmológicos (API v1)")
                        .description("API RESTful con persistencia JPA/H2 para gestión de pacientes ficticios, " +
                                "exámenes oftalmológicos, documentos clínicos, control de acceso por rol (X-User-Role: PACIENTE, MEDICO, TECNICO, ADMINISTRADOR) " +
                                "y bitácora criptográfica de auditoría (DSY1105 - Evaluación Parcial 4 y 5).")
                        .version("2.4.0")
                        .contact(new Contact()
                                .name("Equipo OftApp - Duoc UC")
                                .email("soporte@clinicaofta.cl"))
                        .license(new License()
                                .name("Uso Académico - Datos 100% Sintéticos")
                                .url("https://github.com/majelss/OftApp")));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
