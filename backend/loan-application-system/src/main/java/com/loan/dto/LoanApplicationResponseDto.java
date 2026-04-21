package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.LoanType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponseDto {
	
	private Integer applicationId;
	private String applicantName;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
    private BigDecimal calculatedEmi;
    private ApplicationStatus applicationStatus;
    private LocalDateTime appliedAt;
    private boolean finalEligibility;
    private Integer userId;
}
