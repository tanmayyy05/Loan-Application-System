package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.annotation.CurrentUser;
import com.loan.dto.AdminDashboardResponse;
import com.loan.dto.LoanOfficerDashboardResponse;
import com.loan.entity.User;
import com.loan.service.DashboardService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@GetMapping("/admin/dashboard")
	public ResponseEntity<AdminDashboardResponse> getDashboard() {
		return ResponseEntity.ok(dashboardService.getAdminDashboard());
	}

	@GetMapping("/loan-officer/dashboard")
	public ResponseEntity<LoanOfficerDashboardResponse> getDashboard(@CurrentUser User user) {
		return ResponseEntity.ok(dashboardService.getLoanOfficerDashboard(user.getId()));
	}
}