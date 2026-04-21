package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.loan.constants.PaymentMode;
import com.loan.constants.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentResponse {

    private Integer id;

    private Integer loanAccountId;

    private Integer emiId;

    private BigDecimal paidAmount;

    private LocalDate paymentDate;

    private PaymentMode paymentMode;

    private PaymentType paymentType;
}
