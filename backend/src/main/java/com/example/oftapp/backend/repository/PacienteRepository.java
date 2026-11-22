package com.example.oftapp.backend.repository;

import com.example.oftapp.backend.model.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
    Optional<PacienteEntity> findByRutIgnoreCase(String rut);
}
