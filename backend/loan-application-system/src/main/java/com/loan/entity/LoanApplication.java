package com.loan.entity;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.LoanType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_application")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "loan_type")
	private LoanType loanType;

	@Column(name = "requested_amount")
	private BigDecimal requestedAmount;

	@Column(name = "tenure_months")
	private Integer tenureMonths;

	@Column(name = "interest_rate")
	private BigDecimal interestRate;

	@Column(name = "calculated_emi")
	private BigDecimal calculatedEmi;

	@Enumerated(EnumType.STRING)
	@Column(name = "application_status")
	private ApplicationStatus applicationStatus;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	@Column(name = "applied_at")
	private LocalDateTime appliedAt;

	@Column(name = "last_updated_at")
	private LocalDateTime lastUpdatedAt;
	
	@Column(name = "loan_application_submitted")
	private Boolean loanApplicationSubmitted = false;

	public LoanApplication() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getCalculatedEmi() {
		return calculatedEmi;
	}

	public void setCalculatedEmi(BigDecimal calculatedEmi) {
		this.calculatedEmi = calculatedEmi;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}

	public LocalDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public Boolean getLoanApplicationSubmitted() {
		return loanApplicationSubmitted;
	}

	public void setLoanApplicationSubmitted(Boolean loanApplicationSubmitted) {
		this.loanApplicationSubmitted = loanApplicationSubmitted;
	}
	
}
