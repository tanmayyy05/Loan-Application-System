package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.LoanAccountStatus;
import com.loan.dto.LoanEligibilityResponse;
import com.loan.entity.LoanAccount;
import com.loan.entity.LoanApplication;
import com.loan.entity.User;
import com.loan.repository.LoanAccountRepository;
import com.loan.repository.LoanApplicationRepository;
import com.loan.service.LoanEligibilityService;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    
    @Autowired
    private LoanAccountRepository loanAccountRepository;
    
    @Override
    public boolean checkEligibility(User user, BigDecimal emi) {

        return checkAge(user)
                && checkCibilScore(user)
                && checkFoir(user, emi)
                && checkPreviousLoans(user)
                && checkDefaultedUser(user);
    }

    // =====================  AGE CHECK =====================
    public boolean checkAge(User user) {

        if (user.getDateOfBirth() == null) {
            return false;
        }

        int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();
        return age >= 21 && age <= 60;
    }

    // =====================  CIBIL SCORE (MOCK) =====================
    public boolean checkCibilScore(User user) {

        int score = 300; // base score

        if (user.getMonthlyIncome() != null) {
            if (user.getMonthlyIncome().compareTo(new BigDecimal("100000")) >= 0) {
                score += 250;
            } else if (user.getMonthlyIncome().compareTo(new BigDecimal("50000")) >= 0) {
                score += 200;
            } else {
                score += 100;
            }
        }

        if (user.getEmploymentType() != null) {
            score += 100;
        }

        if (user.getPanNumber() != null && !user.getPanNumber().isBlank()) {
            score += 100;
        }

        return score >= 650;
    }

    // ===================== FOIR CHECK =====================
    public boolean checkFoir(User user, BigDecimal emi) {

        if (user.getMonthlyIncome() == null ||
            user.getMonthlyIncome().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        BigDecimal foir = emi
                .divide(user.getMonthlyIncome(), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return foir.compareTo(BigDecimal.valueOf(50)) <= 0;
    }

    // ===================== PREVIOUS LOAN ACCOUNT CHECK =====================
    public boolean checkPreviousLoans(User user) {

        List<LoanApplication> loans =
                loanApplicationRepository.findByUserId(user.getId());

        return loans.stream()
                .noneMatch(loan ->
                        loan.getApplicationStatus() == ApplicationStatus.LOAN_APPROVED ||
                        loan.getApplicationStatus() == ApplicationStatus.SANCTION_LETTER_ACCEPTED ||
                        loan.getApplicationStatus() == ApplicationStatus.LOAN_DISBURSED
                );
    }
    
	public boolean canApplyForNewLoan(User user) {

		if (hasBlockingInProgressApplication(user)) {
			return false;
		}

		if (hasBlockingDisbursedLoan(user)) {
			return false;
		}

		return true;
	}


    // ===================== DEFAULTED USER FLAG =====================
    public boolean checkDefaultedUser(User user) {
        return !Boolean.TRUE.equals(user.getIsDefaulted());
    }
    
    private boolean hasBlockingInProgressApplication(User user) {

        List<LoanApplication> applications =
                loanApplicationRepository.findByUserId(user.getId());

        return applications.stream().anyMatch(app ->
                app.getApplicationStatus() == ApplicationStatus.DOCUMENT_UPLOAD_PENDING ||
                app.getApplicationStatus() == ApplicationStatus.DOCUMENT_VERIFICATION_PENDING ||
                app.getApplicationStatus() == ApplicationStatus.DOCUMENT_RETURNED_FOR_CORRECTION ||
                app.getApplicationStatus() == ApplicationStatus.DOCUMENT_APPROVED ||
                app.getApplicationStatus() == ApplicationStatus.LOAN_APPROVED ||
                app.getApplicationStatus() == ApplicationStatus.SANCTION_LETTER_SENT ||
                app.getApplicationStatus() == ApplicationStatus.SANCTION_LETTER_ACCEPTED
        );
    }
    
    private boolean hasBlockingDisbursedLoan(User user) {

        List<LoanAccount> loanAccounts =
                loanAccountRepository.findByUserId(user.getId());

        return loanAccounts.stream().anyMatch(account ->
                account.getLoanAccountStatus() == LoanAccountStatus.ACTIVE ||
                account.getLoanAccountStatus() == LoanAccountStatus.OVERDUE ||
                account.getLoanAccountStatus() == LoanAccountStatus.DEFAULTED
        );
    }
    
    private Integer getRejectionCooldownDays(User user) {

        Optional<LoanApplication> rejected =
                loanApplicationRepository
                        .findTopByUserIdAndApplicationStatusInOrderByAppliedAtDesc(
                                user.getId(),
                                List.of(
                                    ApplicationStatus.NOT_ELIGIBLE,
                                    ApplicationStatus.LOAN_REJECTED,
                                    ApplicationStatus.SANCTION_LETTER_REJECTED,
                                    ApplicationStatus.DOCUMENT_REJECTED
                                )
                        );

        if (rejected.isEmpty()) {
            return 0;
        }

        LocalDate appliedDate = rejected.get()
                .getAppliedAt()
                .toLocalDate();

        LocalDate eligibleDate = appliedDate.plusMonths(3);

        if (!eligibleDate.isAfter(LocalDate.now())) {
            return 0; // cooldown completed
        }

        return (int) ChronoUnit.DAYS.between(LocalDate.now(), eligibleDate);
    }


	@Override
	public LoanEligibilityResponse canApplyForLoan(User user) {
		 // 1. In-progress application
	    if (hasBlockingInProgressApplication(user)) {
	        return new LoanEligibilityResponse(
	                false,
	                "You already have a loan application in progress",
	                null
	        );
	    }

	    // 2. Active / risky loan
	    if (hasBlockingDisbursedLoan(user)) {
	        return new LoanEligibilityResponse(
	                false,
	                "You have an active or overdue loan",
	                null
	        );
	    }
	    
	    // 3. Rejection cooldown
	    Integer cooldownDays = getRejectionCooldownDays(user);
	    if (cooldownDays > 0) {
	        return new LoanEligibilityResponse(
	                false,
	                "You were recently not eligible or rejected. Please try again after " + cooldownDays + " days",
	                cooldownDays
	        );
	    }

	    return new LoanEligibilityResponse(true,
	            "You can apply for a loan",0);
	}

}
