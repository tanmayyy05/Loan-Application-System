package com.loan.model;

import com.loan.dto.UserProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	
	private String token;
	private UserProfileResponse userProfile;
}
