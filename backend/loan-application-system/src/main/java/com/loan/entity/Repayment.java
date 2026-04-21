package com.loan.entity;

import com.loan.constants.PaymentMode;
import com.loan.constants.PaymentType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment")
public class Repayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "loan_account_id", nullable = false)
	private LoanAccount loanAccount;

	@ManyToOne
	@JoinColumn(name = "emi_id")
	private EmiSchedule emi;

	@Column(name = "paid_amount")
	private BigDecimal paidAmount;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_mode")
	private PaymentMode paymentMode;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	public Repayment() {
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

	public EmiSchedule getEmi() {
		return emi;
	}

	public void setEmi(EmiSchedule emi) {
		this.emi = emi;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
}
