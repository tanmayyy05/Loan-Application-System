package com.loan.entity;

import com.loan.constants.EmiStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "emi_schedule")
public class EmiSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "loan_account_id", nullable = false)
	private LoanAccount loanAccount;

	@Column(name = "emi_number")
	private Integer emiNumber;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "emi_amount")
	private BigDecimal emiAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "emi_status")
	private EmiStatus emiStatus;

	@Column(name = "is_penalty_applied")
	private Boolean isPenaltyApplied;

	@Column(name = "penalty_amount")
	private BigDecimal penaltyAmount;

	@Column(name = "total_payable_amount")
	private BigDecimal totalPayableAmount;

	@Column(name = "penalty_date")
	private LocalDate penaltyDate;

	@Column(name = "penalty_reason")
	private String penaltyReason;

	public EmiSchedule() {
	}

	public EmiSchedule(Integer id, LoanAccount loanAccount, Integer emiNumber, LocalDate dueDate, BigDecimal emiAmount,
			EmiStatus emiStatus, Boolean isPenaltyApplied, BigDecimal penaltyAmount, BigDecimal totalPayableAmount,
			LocalDate penaltyDate, String penaltyReason) {
		super();
		this.id = id;
		this.loanAccount = loanAccount;
		this.emiNumber = emiNumber;
		this.dueDate = dueDate;
		this.emiAmount = emiAmount;
		this.emiStatus = emiStatus;
		this.isPenaltyApplied = isPenaltyApplied;
		this.penaltyAmount = penaltyAmount;
		this.totalPayableAmount = totalPayableAmount;
		this.penaltyDate = penaltyDate;
		this.penaltyReason = penaltyReason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoanAccount getLoanAccount() {
		return loanAccount;
	}

	public void setLoanAccount(LoanAccount loanAccount) {
		this.loanAccount = loanAccount;
	}

	public Integer getEmiNumber() {
		return emiNumber;
	}

	public void setEmiNumber(Integer emiNumber) {
		this.emiNumber = emiNumber;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}

	public EmiStatus getEmiStatus() {
		return emiStatus;
	}

	public void setEmiStatus(EmiStatus emiStatus) {
		this.emiStatus = emiStatus;
	}

	public Boolean getIsPenaltyApplied() {
		return isPenaltyApplied;
	}

	public void setIsPenaltyApplied(Boolean isPenaltyApplied) {
		this.isPenaltyApplied = isPenaltyApplied;
	}

	public BigDecimal getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public BigDecimal getTotalPayableAmount() {
		return totalPayableAmount;
	}

	public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
		this.totalPayableAmount = totalPayableAmount;
	}

	public LocalDate getPenaltyDate() {
		return penaltyDate;
	}

	public void setPenaltyDate(LocalDate penaltyDate) {
		this.penaltyDate = penaltyDate;
	}

	public String getPenaltyReason() {
		return penaltyReason;
	}

	public void setPenaltyReason(String penaltyReason) {
		this.penaltyReason = penaltyReason;
	}
}
