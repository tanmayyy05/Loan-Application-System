package com.loan.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
	
	private String fullName;
	private String email;
	private String mobileNumber;
	private String password;
	private LocalDate dateOfBirth;
	
	/*
	 * fullname email, mobilenumber, password, dateOfBirth,
	 */ 

}
