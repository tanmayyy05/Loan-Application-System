package com.loan.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "eligibility_check")
public class EligibilityCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "application_id", nullable = false, unique = true)
	private LoanApplication application;

	@Column(name = "cibil_score")
	private Integer cibilScore;

	@Column(name = "cibil_eligible")
	private Boolean cibilEligible;

	@Column(name = "age_eligible")
	private Boolean ageEligible;

	@Column(name = "foir_percentage")
	private BigDecimal foirPercentage;

	@Column(name = "foir_eligible")
	private Boolean foirEligible;

	@Column(name = "is_already_loan_active")
	private Boolean isAlreadyLoanActive;

	@Column(name = "is_defaulted")
	private Boolean isDefaulted;

	@Column(name = "final_eligible")
	private Boolean finalEligible;

	@Column(name = "checked_at")
	private LocalDateTime checkedAt;

	public EligibilityCheck() {
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

	public Integer getCibilScore() {
		return cibilScore;
	}

	public void setCibilScore(Integer cibilScore) {
		this.cibilScore = cibilScore;
	}

	public Boolean getCibilEligible() {
		return cibilEligible;
	}

	public void setCibilEligible(Boolean cibilEligible) {
		this.cibilEligible = cibilEligible;
	}

	public Boolean getAgeEligible() {
		return ageEligible;
	}

	public void setAgeEligible(Boolean ageEligible) {
		this.ageEligible = ageEligible;
	}

	public BigDecimal getFoirPercentage() {
		return foirPercentage;
	}

	public void setFoirPercentage(BigDecimal foirPercentage) {
		this.foirPercentage = foirPercentage;
	}

	public Boolean getFoirEligible() {
		return foirEligible;
	}

	public void setFoirEligible(Boolean foirEligible) {
		this.foirEligible = foirEligible;
	}

	public Boolean getIsAlreadyLoanActive() {
		return isAlreadyLoanActive;
	}

	public void setIsAlreadyLoanActive(Boolean isAlreadyLoanActive) {
		this.isAlreadyLoanActive = isAlreadyLoanActive;
	}

	public Boolean getIsDefaulted() {
		return isDefaulted;
	}

	public void setIsDefaulted(Boolean isDefaulted) {
		this.isDefaulted = isDefaulted;
	}

	public Boolean getFinalEligible() {
		return finalEligible;
	}

	public void setFinalEligible(Boolean finalEligible) {
		this.finalEligible = finalEligible;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}

	public void setCheckedAt(LocalDateTime checkedAt) {
		this.checkedAt = checkedAt;
	}
}
