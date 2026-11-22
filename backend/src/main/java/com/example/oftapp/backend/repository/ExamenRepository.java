package com.example.oftapp.backend.repository;

import com.example.oftapp.backend.model.ExamenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<ExamenEntity, String> {
    List<ExamenEntity> findByPacienteRutIgnoreCase(String pacienteRut);
    List<ExamenEntity> findByEstadoIgnoreCase(String estado);
}
