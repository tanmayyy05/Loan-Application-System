package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.dto.RegisterRequestDto;
import com.loan.dto.UserProfileResponse;
import com.loan.model.AuthRequest;
import com.loan.model.AuthResponse;
import com.loan.security.JwtUtil;
import com.loan.service.PasswordResetService;
import com.loan.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordResetService passwordResetService;

   @PostMapping("/register")
	public ResponseEntity<ApiResponse> saveUser(@RequestBody RegisterRequestDto registerRequestDto) {
		this.userService.saveUser(registerRequestDto);
	   return new ResponseEntity<ApiResponse>(new ApiResponse("User Save Successfully"), HttpStatus.CREATED);		
	}
	
	@PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));
        
        UserProfileResponse user = userService.updateLastLoginAt(request.getEmail());
        
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token,user);
    }
	
	
	/*
	 * // Reset password
	 * 
	 * @PostMapping("/reset-password") public ResponseEntity<String>
	 * resetPassword(@RequestParam String token,@RequestParam String newPassword) {
	 * 
	 * passwordResetService.resetPassword(token, newPassword); return
	 * ResponseEntity.ok("Password reset successful"); }
	 */
	
	 // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email) {

        passwordResetService.forgotPassword(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }
	
	//Reset Password
	 @PostMapping("/set-password")
	    public ResponseEntity<ApiResponse> setPassword(
	            @RequestParam String token,
	            @RequestParam String password,
	            @RequestParam String confirmPassword) {

		 passwordResetService.setPassword(token, password,confirmPassword);
		 return new ResponseEntity<ApiResponse>(new ApiResponse("Password set successfully"), HttpStatus.CREATED);		

	        //return ResponseEntity.ok("Password set successfully");
	    }
}
