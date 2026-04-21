package com.loan.service;

import java.util.List;

import com.loan.entity.AuditLog;
import com.loan.entity.User;

public interface AuditLogService {
	
	void logAction(String action, User user);
	
	List<AuditLog> getAllAuditLogs();

}
