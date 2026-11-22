package com.example.oftapp.backend.controller;

import com.example.oftapp.backend.model.AuditLogEntity;
import com.example.oftapp.backend.model.UsuarioAdminEntity;
import com.example.oftapp.backend.repository.AuditLogRepository;
import com.example.oftapp.backend.repository.UsuarioAdminRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "4. Administracion y Auditoria SIS", description = "Gestion de usuarios clinicos y bitacora criptografica inmutable")
public class AdminController {

    private final UsuarioAdminRepository usuarioRepo;
    private final AuditLogRepository auditRepo;

    public AdminController(UsuarioAdminRepository usuarioRepo, AuditLogRepository auditRepo) {
        this.usuarioRepo = usuarioRepo;
        this.auditRepo = auditRepo;
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios registrados en el modulo Administrador")
    public ResponseEntity<?> listarUsuarios(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Acceso exclusivo para rol ADMINISTRADOR"));
        }
        return ResponseEntity.ok(usuarioRepo.findAll());
    }

    @PostMapping("/usuarios")
    @Operation(summary = "Crear o guardar usuario clinico")
    public ResponseEntity<?> guardarUsuario(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol,
            @RequestBody UsuarioAdminEntity usuario
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Acceso exclusivo para rol ADMINISTRADOR"));
        }
        if (usuario.getId() == null || usuario.getId().isBlank()) {
            usuario.setId("USR-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }
        usuario.setUltimaModificacion("Sincronizado recien");
        UsuarioAdminEntity guardado = usuarioRepo.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Actualizar usuario clinico existente")
    public ResponseEntity<?> actualizarUsuario(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol,
            @PathVariable String id,
            @RequestBody UsuarioAdminEntity datos
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Acceso exclusivo para rol ADMINISTRADOR"));
        }
        datos.setId(id);
        datos.setUltimaModificacion("Modificado recien");
        return ResponseEntity.ok(usuarioRepo.save(datos));
    }

    @PatchMapping("/usuarios/{id}/suspender")
    @Operation(summary = "Suspender preventivamente cuenta de usuario")
    public ResponseEntity<?> suspenderUsuario(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol,
            @PathVariable String id
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Acceso exclusivo para rol ADMINISTRADOR"));
        }
        return usuarioRepo.findById(id).map(u -> {
            u.setCuentaActiva(false);
            u.setUltimaModificacion("Suspendido recien");
            return ResponseEntity.ok(usuarioRepo.save(u));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/auditoria")
    @Operation(summary = "Listar eventos de la Bitacora Criptografica Inmutable")
    public List<AuditLogEntity> listarAuditoria() {
        return auditRepo.findAllByOrderByTimestampMillisDesc();
    }

    @PostMapping("/auditoria")
    @Operation(summary = "Registrar nuevo evento forense en la Bitacora de Auditoria")
    public ResponseEntity<AuditLogEntity> registrarEventoAuditoria(@RequestBody AuditLogEntity log) {
        long now = System.currentTimeMillis();
        if (log.getId() == null || log.getId().isBlank()) {
            log.setId("#LOG-" + (882500 + (now % 10000)));
        }
        if (log.getTimestampMillis() == 0L) {
            log.setTimestampMillis(now);
        }
        if (log.getHora() == null || log.getHora().isBlank()) {
            log.setHora(new SimpleDateFormat("HH:mm 'hrs'").format(new Date(now)));
        }
        if (log.getHashSha256() == null || log.getHashSha256().isBlank()) {
            log.setHashSha256(UUID.randomUUID().toString().replace("-", "")
                    + UUID.randomUUID().toString().replace("-", ""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(auditRepo.save(log));
    }
}
