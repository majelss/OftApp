package com.example.oftapp.backend.controller;

import com.example.oftapp.backend.dto.LoginRequest;
import com.example.oftapp.backend.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Autenticacion y Roles", description = "Endpoints de sesion y roles de OftApp")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario por rol")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, null, null, null, null, null, "Credenciales invalidas"));
        }

        String rol = (req.getRol() == null || req.getRol().isBlank())
                ? "MEDICO"
                : req.getRol().trim().toUpperCase();

        String token = "oftapp_tok_" + rol.toLowerCase() + "_" + UUID.randomUUID().toString().substring(0, 12);
        String nombre = switch (rol) {
            case "PACIENTE" -> "Mariana Gonzalez Riquelme";
            case "ADMINISTRADOR" -> "Dra. Sofia Valenzuela Morales";
            case "TECNICO" -> "T.M. Lucas Morales";
            default -> "Dr. Carlos Valenzuela";
        };

        return ResponseEntity.ok(new LoginResponse(
                true, 1L, nombre, req.getEmail().trim(), rol, token, "Acceso autorizado para " + rol
        ));
    }

    @GetMapping("/roles")
    @Operation(summary = "Consultar roles del sistema")
    public ResponseEntity<List<Map<String, String>>> obtenerRoles() {
        return ResponseEntity.ok(List.of(
                Map.of("codigo", "PACIENTE", "permisos", "Ver sus propios examenes e historial"),
                Map.of("codigo", "TECNICO", "permisos", "Registrar examenes y adjuntar documentos"),
                Map.of("codigo", "MEDICO", "permisos", "Consultar historial y validar examenes"),
                Map.of("codigo", "ADMINISTRADOR", "permisos", "Gestionar usuarios, sistema y auditoria")
        ));
    }
}
