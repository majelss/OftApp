package com.example.oftapp.backend.repository;

import com.example.oftapp.backend.model.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, String> {
    List<AuditLogEntity> findAllByOrderByTimestampMillisDesc();
}
