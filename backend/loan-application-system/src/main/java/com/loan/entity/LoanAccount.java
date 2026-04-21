package com.loan.entity;

import com.loan.constants.ClosureType;
import com.loan.constants.LoanAccountStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_account")
public class LoanAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "application_id", unique = true)
	private LoanApplication application;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "loan_amount")
	private BigDecimal loanAmount;

	@Column(name = "interest_rate")
	private BigDecimal interestRate;

	@Column(name = "tenure_months")
	private Integer tenureMonths;

	@Column(name = "emi_amount")
	private BigDecimal emiAmount;

	@Column(name = "outstanding_balance")
	private BigDecimal outstandingBalance;

	@Enumerated(EnumType.STRING)
	@Column(name = "loan_account_status")
	private LoanAccountStatus loanAccountStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "closure_type")
	private ClosureType closureType;

	@Column(name = "disbursed_date")
	private LocalDate disbursedDate;

	@Column(name = "closure_date")
	private LocalDate closureDate;

	public LoanAccount() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoanApplication getApplication() {
		return application;
	}

	public void setApplication(LoanApplication application) {
		this.application = application;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}

	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public LoanAccountStatus getLoanAccountStatus() {
		return loanAccountStatus;
	}

	public void setLoanAccountStatus(LoanAccountStatus loanAccountStatus) {
		this.loanAccountStatus = loanAccountStatus;
	}

	public ClosureType getClosureType() {
		return closureType;
	}

	public void setClosureType(ClosureType closureType) {
		this.closureType = closureType;
	}

	public LocalDate getDisbursedDate() {
		return disbursedDate;
	}

	public void setDisbursedDate(LocalDate disbursedDate) {
		this.disbursedDate = disbursedDate;
	}

	public LocalDate getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(LocalDate closureDate) {
		this.closureDate = closureDate;
	}
}
