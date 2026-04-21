package com.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfficerDashboardResponse {

    private long totalApplications;
    private long pendingVerification;
    private long documentsReturned;
    private long documentsRejected;
}