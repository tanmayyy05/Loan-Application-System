package com.loan.service;

import com.loan.entity.User;

public interface PasswordResetService {

	// void sendResetLink(String email);

	// void resetPassword(String token, String newPassword);

	void sendSetPasswordMail(User user);

	//void setPassword(String token, String password);
	
	public void setPassword(String token, String password, String confirmPassword);
	
	void forgotPassword(String email);


}
