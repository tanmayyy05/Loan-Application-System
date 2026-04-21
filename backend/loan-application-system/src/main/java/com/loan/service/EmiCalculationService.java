package com.loan.service;

import java.math.BigDecimal;

public interface EmiCalculationService {
	
	 BigDecimal calculateEmi(BigDecimal principal,BigDecimal annualInterestRate,int tenureMonths);

}
