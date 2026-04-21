package com.loan.dto;

import com.loan.constants.PaymentMode;
import com.loan.constants.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecloseRequest {

    private Integer loanAccountId;

    private PaymentMode paymentMode;

    private PaymentType paymentType;
}
