package com.loan.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanOfficerRequestDto {
	private Integer id;
	private String fullName;
	private String email;
	private String mobileNumber;
	private LocalDate dateOfBirth;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private Integer age;
	private String gender;

}
