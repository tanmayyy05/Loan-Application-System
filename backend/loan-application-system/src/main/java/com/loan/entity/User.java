package com.loan.entity;

import com.loan.constants.EmploymentType;
import com.loan.constants.Role;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "mobile_number") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "full_name")
	private String fullName;

	@Column(nullable = false)
	private String email;

	@Column(name = "mobile_number", nullable = false)
	private String mobileNumber;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	private String address;

	private String city;
	
	private String state;

	@Column(name = "pincode")
	private String pincode;

	private Integer age;

	@Column(name = "gender")
	private String gender;
	
	@Column(name = "bank_account")
	private String bankAccount;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "employment_type")
	private EmploymentType employmentType;

	@Column(name = "monthly_income")
	private BigDecimal monthlyIncome;

	@Column(name = "pan_number")
	private String panNumber;

	@Column(name = "aadhaar_number")
	private String aadhaarNumber;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;
	
	@Column(nullable = false)
	private Boolean isDefaulted = false;

	public User() {
	}

	public User(Integer id, String fullName, String email, String mobileNumber, String password, Role role,
			LocalDate dateOfBirth, String address, String city, String state, String pincode, Integer age,
			String gender, String bankAccount, String ifscCode, EmploymentType employmentType, BigDecimal monthlyIncome,
			String panNumber, String aadhaarNumber, String companyName, Boolean isDeleted, LocalDateTime createdAt,
			LocalDateTime updatedAt, LocalDateTime lastLoginAt, Boolean isDefaulted) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.role = role;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.age = age;
		this.gender = gender;
		this.bankAccount = bankAccount;
		this.ifscCode = ifscCode;
		this.employmentType = employmentType;
		this.monthlyIncome = monthlyIncome;
		this.panNumber = panNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.companyName = companyName;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.lastLoginAt = lastLoginAt;
		this.isDefaulted = isDefaulted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Boolean getIsDefaulted() {
		return isDefaulted;
	}

	public void setIsDefaulted(Boolean isDefaulted) {
		this.isDefaulted = isDefaulted;
	}

}
