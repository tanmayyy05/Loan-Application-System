package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.loan.constants.ClosureType;
import com.loan.constants.LoanAccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAccountResponseDto {

	private Integer id;
	private Integer applicationId;
	private Integer userId;
	private BigDecimal loanAmount;
	private BigDecimal interestRate;
	private Integer tenureMonths;
	private BigDecimal emiAmount;
	private BigDecimal outstandingBalance;
	private LoanAccountStatus loanAccountStatus;
	private ClosureType closureType;
	private LocalDate disbursedDate;
	private LocalDate closureDate;

}
