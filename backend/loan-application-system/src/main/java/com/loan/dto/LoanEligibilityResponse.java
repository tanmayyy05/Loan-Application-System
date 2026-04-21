package com.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEligibilityResponse {

    private boolean canApply;
    private String reason;        // human readable
    private Integer waitDays;     // optional (for cool-off)
}
