package com.example.oftapp.backend.controller;

import com.example.oftapp.backend.dto.EstadoUpdateRequest;
import com.example.oftapp.backend.model.AuditLogEntity;
import com.example.oftapp.backend.model.ExamenEntity;
import com.example.oftapp.backend.repository.AuditLogRepository;
import com.example.oftapp.backend.repository.ExamenRepository;
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
@RequestMapping("/api/v1/examenes")
@Tag(name = "3. Examenes Oftalmologicos", description = "CRUD y trazabilidad clinica de examenes oftalmologicos por rol")
public class ExamenController {

    private final ExamenRepository examenRepo;
    private final AuditLogRepository auditRepo;

    public ExamenController(ExamenRepository examenRepo, AuditLogRepository auditRepo) {
        this.examenRepo = examenRepo;
        this.auditRepo = auditRepo;
    }

    @GetMapping
    @Operation(summary = "Listar examenes oftalmologicos filtrados por rol, RUT o estado")
    public List<ExamenEntity> listarExamenes(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @RequestParam(required = false) String pacienteRut,
            @RequestParam(required = false) String estado
    ) {
        String rutFiltro = pacienteRut;
        if ("PACIENTE".equalsIgnoreCase(rol) && (rutFiltro == null || rutFiltro.isBlank())) {
            rutFiltro = "14.821.903-2";
        }

        List<ExamenEntity> base = (rutFiltro != null && !rutFiltro.isBlank())
                ? examenRepo.findByPacienteRutIgnoreCase(rutFiltro)
                : examenRepo.findAll();

        if (estado != null && !estado.isBlank() && !"Todos".equalsIgnoreCase(estado)) {
            return base.stream()
                    .filter(e -> estado.equalsIgnoreCase(e.getEstado()))
                    .toList();
        }
        return base;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar detalle de examen por ID")
    public ResponseEntity<ExamenEntity> obtenerPorId(@PathVariable String id) {
        return examenRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo examen oftalmologico")
    public ResponseEntity<?> crearExamen(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @RequestBody ExamenEntity examen
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "El rol PACIENTE no puede registrar examenes clinicos"));
        }

        if (examen.getId() == null || examen.getId().isBlank()) {
            examen.setId("EXAM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (examen.getEstado() == null || examen.getEstado().isBlank()) {
            examen.setEstado("Pendiente");
        }
        if (examen.getFecha() == null || examen.getFecha().isBlank()) {
            examen.setFecha(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        }

        ExamenEntity guardado = examenRepo.save(examen);
        registrarLogForense(
                "Registro de Examen Clinico",
                "examenes",
                "Creado",
                guardado.getId() + " (" + guardado.getPacienteNombre() + ")",
                "Ingreso de " + guardado.getTipoExamen() + " en ojo " + guardado.getOjoEvaluado()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar examen oftalmologico completo")
    public ResponseEntity<?> actualizarExamen(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @PathVariable String id,
            @RequestBody ExamenEntity datos
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Permiso denegado para rol PACIENTE"));
        }
        return examenRepo.findById(id).map(actual -> {
            actual.setPacienteRut(datos.getPacienteRut());
            actual.setPacienteNombre(datos.getPacienteNombre());
            actual.setPacienteEdad(datos.getPacienteEdad());
            actual.setTipoExamen(datos.getTipoExamen());
            actual.setProtocolo(datos.getProtocolo());
            actual.setSucursalBox(datos.getSucursalBox());
            actual.setOjoEvaluado(datos.getOjoEvaluado());
            actual.setDocumentoNombre(datos.getDocumentoNombre());
            actual.setDocumentoTamano(datos.getDocumentoTamano());
            actual.setDocumentoFormato(datos.getDocumentoFormato());
            actual.setObservaciones(datos.getObservaciones());
            actual.setEstado(datos.getEstado());
            actual.setFecha(datos.getFecha());
            actual.setFirmaElectronica(datos.getFirmaElectronica());
            return ResponseEntity.ok(examenRepo.save(actual));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de validacion clinica del examen")
    public ResponseEntity<?> actualizarEstado(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @PathVariable String id,
            @RequestBody EstadoUpdateRequest req
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Solo personal medico puede validar o cambiar estado de examenes"));
        }
        return examenRepo.findById(id).map(actual -> {
            String previo = actual.getEstado();
            if (req.getNuevoEstado() != null && !req.getNuevoEstado().isBlank()) {
                actual.setEstado(req.getNuevoEstado());
            }
            if (req.getObservaciones() != null && !req.getObservaciones().isBlank()) {
                actual.setObservaciones(req.getObservaciones());
            }
            if (req.getFirmaElectronica() != null && !req.getFirmaElectronica().isBlank()) {
                actual.setFirmaElectronica(req.getFirmaElectronica());
            }
            ExamenEntity guardado = examenRepo.save(actual);
            registrarLogForense(
                    "Cambio de Estado de Examen",
                    "examenes",
                    "Autorizado",
                    guardado.getId(),
                    "Transicion de estado: " + previo + " -> " + guardado.getEstado()
            );
            return ResponseEntity.ok(guardado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar examen oftalmologico")
    public ResponseEntity<?> eliminarExamen(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol,
            @PathVariable String id
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Permiso denegado"));
        }
        if (!examenRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        examenRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void registrarLogForense(String titulo, String categoria, String estado, String afectado, String detalle) {
        long now = System.currentTimeMillis();
        String logId = "#LOG-" + (882300 + (now % 10000));
        String hora = new SimpleDateFormat("HH:mm 'hrs'").format(new Date(now));
        AuditLogEntity log = new AuditLogEntity(
                logId,
                titulo,
                categoria,
                estado,
                "Prof. Dr. R. Valenzuela",
                "Entidad:",
                afectado,
                "Trazabilidad Clinica",
                detalle,
                null,
                null,
                "HOY, 24 MAYO 2024",
                hora,
                UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", ""),
                now
        );
        auditRepo.save(log);
    }
}
