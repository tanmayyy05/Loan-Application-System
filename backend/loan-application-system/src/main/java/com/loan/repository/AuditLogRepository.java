package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer>{

}
