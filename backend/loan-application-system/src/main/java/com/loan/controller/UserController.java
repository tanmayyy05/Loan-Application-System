package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan.annotation.CurrentUser;
import com.loan.dto.ApiResponse;
import com.loan.dto.PageResponse;
import com.loan.dto.UserProfileRequest;
import com.loan.dto.UserProfileResponse;
import com.loan.entity.User;
import com.loan.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	UserService userService;

	// View user Profile
	@GetMapping("/viewProfile")
	public ResponseEntity<UserProfileResponse> viewProfile(@CurrentUser User user) {
		UserProfileResponse response = userService.getUserProfile(user.getId());
		return ResponseEntity.ok(response);
	}

	// Get all customers
	@GetMapping("/customers")
	public ResponseEntity<PageResponse<UserProfileResponse>> getAllCustomers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "fullName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		PageResponse<UserProfileResponse> response = userService.getAllCustomers(page, size, sortBy, sortDir);
		return ResponseEntity.ok(response);
	}

	// update user profile
	@PutMapping("/updateProfile")
	public ResponseEntity<ApiResponse> updateProfile(@RequestBody UserProfileRequest request) {

		userService.updateUserProfile(request);
		return ResponseEntity.ok(new ApiResponse("User Profile updated successfully !!"));
	}

	// Delete user Profile
	@DeleteMapping("/deleteProfile/{userId}")
	public ResponseEntity<ApiResponse> deleteProfile(@PathVariable("userId") int userId) {
		userService.deleteUserProfile(userId);
		return ResponseEntity.ok(new ApiResponse("User profile deleted successfully"));
	}
}
