package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.LoanType;
import com.loan.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDetailsResponseDto {

    private Integer applicationId;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
    private BigDecimal calculatedEmi;
    private ApplicationStatus applicationStatus;
    private LocalDateTime appliedAt;
    private boolean finalEligibility;
    private User user;
    private List<DocumentResponseDto> documents;
}
