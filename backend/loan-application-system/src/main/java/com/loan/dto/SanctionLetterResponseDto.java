package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.loan.constants.SanctionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanctionLetterResponseDto {

    private Integer id;
    private Integer applicationId;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private LocalDate validTill;
    private SanctionStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;
}
