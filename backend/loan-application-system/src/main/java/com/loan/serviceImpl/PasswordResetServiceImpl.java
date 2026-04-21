package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loan.constants.EmailTemplateName;
import com.loan.entity.PasswordResetToken;
import com.loan.entity.User;
import com.loan.repository.PasswordResetTokenRepository;
import com.loan.repository.UserRepository;
import com.loan.service.EmailSendService;
import com.loan.service.PasswordResetService;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	EmailSendService emailSendService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void sendSetPasswordMail(User user) {

		System.out.println("📧 Sending set-password mail to: " + user.getEmail());

		String token = UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setUserId(user.getId());
		resetToken.setToken(token);
		resetToken.setExpiryTime(LocalDateTime.now().plusHours(24));
		resetToken.setUsed(false);

		passwordResetTokenRepository.save(resetToken);

		String setPasswordLink = "http://localhost:3000/set-password?token=" + token;

		emailSendService.sendTemplateEmail(EmailTemplateName.RESET_PASSWORD, user.getEmail(), null, user.getId(),
				Map.of("name", user.getFullName(), "resetLink", setPasswordLink));
	}

	public void setPassword(String token, String password, String confirmPassword) {

		if (!password.equals(confirmPassword)) {
			throw new RuntimeException("Password and Confirm Password do not match");
		}

		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid reset token"));

		if (resetToken.getUsed()) {
			throw new RuntimeException("Reset link already used");
		}

		if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Reset link expired");
		}

		User user = userRepository.findById(resetToken.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);

		resetToken.setUsed(true);
		passwordResetTokenRepository.save(resetToken);
	}

	@Override
	public void forgotPassword(String email) {

		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

	    // Invalidate previous tokens (important for security)
	    passwordResetTokenRepository.deleteByUserId(user.getId());

	    // Reuse existing logic
	    sendSetPasswordMail(user);	
		
	}
}
