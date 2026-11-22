package com.example.oftapp.backend;

import com.example.oftapp.backend.controller.AuthController;
import com.example.oftapp.backend.controller.ExamenController;
import com.example.oftapp.backend.controller.PacienteController;
import com.example.oftapp.backend.dto.LoginRequest;
import com.example.oftapp.backend.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OftAppBackendApplicationTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private PacienteController pacienteController;

    @Autowired
    private ExamenController examenController;

    @Test
    void contextLoadsAndSeedsSyntheticData() {
        assertNotNull(authController);
        assertFalse(pacienteController.listarTodos("MEDICO").isEmpty());
        assertFalse(examenController.listarExamenes("MEDICO", null, null).isEmpty());
    }

    @Test
    void loginEndpointIssuesRoleToken() {
        ResponseEntity<LoginResponse> resp = authController.login(
                new LoginRequest("dr.valenzuela@clinicaofta.cl", "dr123456", "MEDICO")
        );
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().isAutenticado());
        assertNotNull(resp.getBody().getToken());
    }
}
