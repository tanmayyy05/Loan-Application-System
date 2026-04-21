package com.loan.service;

import com.loan.dto.AdminDashboardResponse;
import com.loan.dto.LoanOfficerDashboardResponse;

public interface DashboardService {

	public AdminDashboardResponse getAdminDashboard();

	public LoanOfficerDashboardResponse getLoanOfficerDashboard(Integer id);

}