package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.entity.AuditLog;
import com.loan.entity.User;
import com.loan.repository.AuditLogRepository;
import com.loan.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	AuditLogRepository auditLogRepository;

	@Override
	public void logAction(String action, User user) {
		AuditLog auditLog = new AuditLog();
		auditLog.setAction(action);
		auditLog.setPerformedBy(user);
		auditLog.setPerformedAt(LocalDateTime.now());

		auditLogRepository.save(auditLog);
	}

	@Override
	public List<AuditLog> getAllAuditLogs() {
		List<AuditLog> listOfAuditLogs = auditLogRepository.findAll();
		return listOfAuditLogs;
	}

}
