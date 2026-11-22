package com.example.oftapp.backend.config;

import com.example.oftapp.backend.model.AuditLogEntity;
import com.example.oftapp.backend.model.ExamenEntity;
import com.example.oftapp.backend.model.PacienteEntity;
import com.example.oftapp.backend.model.UsuarioAdminEntity;
import com.example.oftapp.backend.repository.AuditLogRepository;
import com.example.oftapp.backend.repository.ExamenRepository;
import com.example.oftapp.backend.repository.PacienteRepository;
import com.example.oftapp.backend.repository.UsuarioAdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Inicializador de datos sinteticos al iniciar el microservicio Spring Boot.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDatabase(
            PacienteRepository pacienteRepo,
            ExamenRepository examenRepo,
            UsuarioAdminRepository usuarioRepo,
            AuditLogRepository auditRepo
    ) {
        return args -> {
            if (pacienteRepo.count() == 0) {
                pacienteRepo.saveAll(List.of(
                        new PacienteEntity(null, "14.821.903-2", "Mariana Gonzalez Riquelme", 42, "FONASA Tramo B", true, "#OF-8941"),
                        new PacienteEntity(null, "18.332.194-K", "Carlos Venegas Soto", 38, "Isapre Colmena", true, "#OF-8942"),
                        new PacienteEntity(null, "12.784.551-8", "Beatriz Morales Lara", 51, "FONASA Tramo C", true, "#OF-8943")
                ));
            }

            if (examenRepo.count() == 0) {
                examenRepo.saveAll(List.of(
                        new ExamenEntity(
                                "EXAM-2026-001",
                                "14.821.903-2",
                                "Mariana Gonzalez Riquelme",
                                42,
                                "Campimetria Visual Computarizada 24-2",
                                "Protocolo estandar SITA-Fast activado",
                                "Sucursal Providencia - Box 3 Oftalmo",
                                "OD",
                                "resultado_oct_scan_01.pdf",
                                "2.4 MB",
                                "PDF",
                                "Sin hallazgos patologicos en cuadrante nasal superior.",
                                "Validado",
                                "24/10/2026 10:30",
                                "Dr. C. Mendoza"
                        ),
                        new ExamenEntity(
                                "EXAM-2026-002",
                                "18.332.194-K",
                                "Carlos Venegas Soto",
                                38,
                                "Tomografia de Coherencia Optica (OCT)",
                                "Protocolo Macular 3D de alta resolucion",
                                "Sucursal Puerto Montt - Box 1 Consulta",
                                "AO",
                                "oct_macular_hd.pdf",
                                "3.8 MB",
                                "PDF",
                                "Grosor foveal central dentro de limites normales bilaterales.",
                                "Pendiente",
                                "24/10/2026 09:15",
                                "Dr. C. Mendoza"
                        ),
                        new ExamenEntity(
                                "EXAM-2026-003",
                                "12.784.551-8",
                                "Beatriz Morales Lara",
                                51,
                                "Topografia Corneal Pentacam",
                                "Mapeo de elevacion anterior y posterior",
                                "Sucursal Las Condes - Box 5 Diagnostico",
                                "OI",
                                "topografia_pentacam.pdf",
                                "4.1 MB",
                                "PDF",
                                "Astigmatismo miopico regular con la regla en ojo izquierdo.",
                                "Observado",
                                "23/10/2026 16:45",
                                "Dr. C. Mendoza"
                        )
                ));
            }

            if (usuarioRepo.count() == 0) {
                usuarioRepo.save(new UsuarioAdminEntity(
                        "USR-84920",
                        "medico",
                        "Dra. Sofia Valenzuela Morales",
                        "15.842.109-K",
                        "svalenzuela@oftalmolaser.cl",
                        "+56 9 8765 4321",
                        true,
                        "SIS-78942-CL",
                        "Polo Anterior y Catarata",
                        "Box 04 - Torre Quirurgica Oftalmo",
                        true,
                        true,
                        false,
                        true,
                        "Modificado hace 2h"
                ));
            }

            if (auditRepo.count() == 0) {
                long baseTime = 1716550000000L;
                auditRepo.saveAll(List.of(
                        new AuditLogEntity(
                                "#LOG-882190",
                                "Modificacion de Permisos",
                                "usuarios",
                                "Autorizado",
                                "Prof. Dr. R. Valenzuela",
                                "Afectado:",
                                "Claudia Vidal (Adm.)",
                                "Accion y Privilegio Concedido",
                                "Permiso concedido: Reemision directa de duplicados clinicos con firma delegada.",
                                null,
                                null,
                                "HOY, 24 MAYO 2024",
                                "11:42 hrs",
                                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                                baseTime + 4000L
                        ),
                        new AuditLogEntity(
                                "#LOG-882144",
                                "Creacion de Usuario Medico",
                                "usuarios",
                                "Creado",
                                "Prof. Dr. R. Valenzuela",
                                "Afectado:",
                                "Dr. Camilo Arancibia",
                                "Especialidad & Certificacion",
                                "Medico Oftalmologo (Glaucoma). Registro SIS #49821 validado; firma electronica avanzada vinculada.",
                                null,
                                null,
                                "HOY, 24 MAYO 2024",
                                "10:15 hrs",
                                "8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4",
                                baseTime + 3000L
                        ),
                        new AuditLogEntity(
                                "#LOG-881903",
                                "Modificacion de Parametro",
                                "parametros",
                                "Configuración",
                                "Prof. Dr. R. Valenzuela",
                                "Entidad:",
                                "Umbrales Tonometria",
                                "Comparativa Forense",
                                "Ajuste de umbral critico de presion intraocular.",
                                "22 mmHg",
                                "21 mmHg",
                                "HOY, 24 MAYO 2024",
                                "09:30 hrs",
                                "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae",
                                baseTime + 2000L
                        )
                ));
            }
        };
    }
}
