package com.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.loan.constants.EmploymentType;
import com.loan.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

	private Integer id;

	private String fullName;

	private String email;

	private String mobileNumber;

	private Role role;

	private LocalDate dateOfBirth;

	private String address;

	private String city;
	
	private String state;

	private String pincode;

	private Integer age;

	private String gender;
	
	private String bankAccount;

	private String ifscCode;

	private EmploymentType employmentType;

	private BigDecimal monthlyIncome;

	private String panNumber;

	private String aadhaarNumber;

	private String companyName;

}
