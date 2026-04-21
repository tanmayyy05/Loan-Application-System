package com.loan.entity;

import com.loan.constants.SanctionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sanction_letter")
public class SanctionLetter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "application_id", nullable = false, unique = true)
	private LoanApplication application;

	@Column(name = "approved_amount")
	private BigDecimal approvedAmount;

	@Column(name = "interest_rate")
	private BigDecimal interestRate;

	@Column(name = "tenure_months")
	private Integer tenureMonths;

	@Column(name = "emi_amount")
	private BigDecimal emiAmount;

	@Column(name = "valid_till")
	private LocalDate validTill;

	@Enumerated(EnumType.STRING)
	private SanctionStatus status;

	@Column(name = "sent_at")
	private LocalDateTime sentAt;

	@Column(name = "responded_at")
	private LocalDateTime respondedAt;

	public SanctionLetter() {
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

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
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

	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}

	public SanctionStatus getStatus() {
		return status;
	}

	public void setStatus(SanctionStatus status) {
		this.status = status;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public LocalDateTime getRespondedAt() {
		return respondedAt;
	}

	public void setRespondedAt(LocalDateTime respondedAt) {
		this.respondedAt = respondedAt;
	}
}
