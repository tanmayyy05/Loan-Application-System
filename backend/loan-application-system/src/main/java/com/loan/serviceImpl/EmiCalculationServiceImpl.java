package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.loan.exception.InvalidLoanDataException;
import com.loan.financeConstants.FinanceConstants;
import com.loan.service.EmiCalculationService;

@Service
public class EmiCalculationServiceImpl implements EmiCalculationService{

	@Override
	public BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualInterestRate, int tenureMonths) {
		
		validate(principal, annualInterestRate, tenureMonths);
		
		 // R = annual rate / 12 / 100
        BigDecimal monthlyRate = annualInterestRate
                .divide(FinanceConstants.MONTHS_IN_YEAR,
                        FinanceConstants.CALCULATION_SCALE,
                        RoundingMode.HALF_UP)
                .divide(FinanceConstants.HUNDRED,
                        FinanceConstants.CALCULATION_SCALE,
                        RoundingMode.HALF_UP);

        // (1 + R)^N
        BigDecimal ratePowerTenure = monthlyRate
                .add(BigDecimal.ONE)
                .pow(tenureMonths);

        // EMI formula
        BigDecimal numerator = principal
                .multiply(monthlyRate)
                .multiply(ratePowerTenure);

        BigDecimal denominator = ratePowerTenure.subtract(BigDecimal.ONE);

        return numerator.divide(
                denominator,
                FinanceConstants.OUTPUT_SCALE,
                RoundingMode.HALF_UP
        );
    }
	
	private void validate(BigDecimal principal,
            BigDecimal annualRate,
            int tenureMonths) {

if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
throw new InvalidLoanDataException("Invalid loan amount");
}

if (annualRate == null || annualRate.compareTo(BigDecimal.ZERO) <= 0) {
throw new InvalidLoanDataException("Invalid interest rate");
}

if (tenureMonths <= 0) {
throw new InvalidLoanDataException("Invalid tenure");
}
}

}
