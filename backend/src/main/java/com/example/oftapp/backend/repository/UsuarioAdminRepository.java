package com.example.oftapp.backend.repository;

import com.example.oftapp.backend.model.UsuarioAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioAdminRepository extends JpaRepository<UsuarioAdminEntity, String> {
}
