package com.loan.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalApplications;
    private long pendingLoanApprovals;
    private BigDecimal totalAmountDisbursed;
}