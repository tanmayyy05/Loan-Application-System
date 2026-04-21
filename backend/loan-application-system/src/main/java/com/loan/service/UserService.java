package com.loan.service;

import com.loan.dto.PageResponse;
import com.loan.dto.RegisterRequestDto;
import com.loan.dto.UserProfileRequest;
import com.loan.dto.UserProfileResponse;

public interface UserService {

	public void saveUser(RegisterRequestDto registerRequestDto);

	public UserProfileResponse updateLastLoginAt(String email);

	public UserProfileResponse getUserProfile(int userId);

	public void updateUserProfile(UserProfileRequest request);

	public void deleteUserProfile(int userId);

	public PageResponse<UserProfileResponse> getAllCustomers(int page, int size, String sortBy, String sortDir);

}
