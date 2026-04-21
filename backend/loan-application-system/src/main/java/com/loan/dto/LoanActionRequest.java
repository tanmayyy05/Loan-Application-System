package com.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanActionRequest {

    private int applicationId;

    private String rejectionReason;
    
    private String remarks;
}
