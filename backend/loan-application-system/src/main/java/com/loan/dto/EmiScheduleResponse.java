package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.loan.constants.EmiStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmiScheduleResponse {

    private Integer id;
    private int emiNumber;
    private LocalDate dueDate;
    private BigDecimal emiAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal totalPayableAmount;
    private EmiStatus emiStatus;
}
