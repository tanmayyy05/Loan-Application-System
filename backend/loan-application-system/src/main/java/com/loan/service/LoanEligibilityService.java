package com.loan.service;

import java.math.BigDecimal;

import com.loan.dto.LoanEligibilityResponse;
import com.loan.entity.User;
public interface LoanEligibilityService {

    boolean checkEligibility(User user, BigDecimal requestedEmi);
    
    public boolean checkAge(User user);
    public boolean checkCibilScore(User user);
    public boolean checkFoir(User user, BigDecimal emi);
    public boolean checkPreviousLoans(User user);
    public boolean checkDefaultedUser(User user);
    public boolean canApplyForNewLoan(User user);
	public LoanEligibilityResponse canApplyForLoan(User user);
}
