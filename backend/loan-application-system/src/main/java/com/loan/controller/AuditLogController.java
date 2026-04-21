package com.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.entity.AuditLog;
import com.loan.service.AuditLogService;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
	
	@Autowired
	AuditLogService auditLogService;
	
	@GetMapping
	public ResponseEntity<List<AuditLog>>  getAllAuditLogs(){
		List<AuditLog> allAuditLogs = auditLogService.getAllAuditLogs();
		return ResponseEntity.ok(allAuditLogs);
		
		
	}

}
