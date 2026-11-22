package com.example.oftapp.backend.controller;

import com.example.oftapp.backend.model.PacienteEntity;
import com.example.oftapp.backend.repository.PacienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "2. Pacientes Oftalmologicos", description = "Operaciones sobre fichas de pacientes sinteticos de OftApp")
public class PacienteController {

    private final PacienteRepository repo;

    public PacienteController(PacienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @Operation(summary = "Listar pacientes sinteticos")
    public List<PacienteEntity> listarTodos(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol
    ) {
        List<PacienteEntity> todos = repo.findAll();
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return todos.stream()
                    .filter(p -> "14.821.903-2".equalsIgnoreCase(p.getRut()))
                    .toList();
        }
        return todos;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar paciente por ID")
    public ResponseEntity<PacienteEntity> buscarPorId(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Consultar paciente por RUT chileno")
    public ResponseEntity<PacienteEntity> buscarPorRut(@PathVariable String rut) {
        return repo.findByRutIgnoreCase(rut)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo paciente sintetico")
    public ResponseEntity<?> registrar(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @RequestBody PacienteEntity nuevo
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "El rol PACIENTE solo tiene permisos de lectura"));
        }
        nuevo.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ficha de paciente")
    public ResponseEntity<?> actualizar(
            @RequestHeader(value = "X-User-Role", defaultValue = "MEDICO") String rol,
            @PathVariable Long id,
            @RequestBody PacienteEntity datos
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Permiso denegado para rol PACIENTE"));
        }
        return repo.findById(id).map(actual -> {
            actual.setRut(datos.getRut());
            actual.setNombreCompleto(datos.getNombreCompleto());
            actual.setEdad(datos.getEdad());
            actual.setPrevision(datos.getPrevision());
            actual.setActivo(datos.isActivo());
            actual.setCodigoFicha(datos.getCodigoFicha());
            return ResponseEntity.ok(repo.save(actual));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar paciente sintetico")
    public ResponseEntity<?> eliminar(
            @RequestHeader(value = "X-User-Role", defaultValue = "ADMINISTRADOR") String rol,
            @PathVariable Long id
    ) {
        if ("PACIENTE".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Solo personal autorizado puede eliminar fichas"));
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
