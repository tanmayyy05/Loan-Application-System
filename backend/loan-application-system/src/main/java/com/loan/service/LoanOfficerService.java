package com.loan.service;

import java.util.List;
import org.springframework.data.domain.Pageable;

import com.loan.dto.ApiResponse;
import com.loan.dto.LoanOfficerRequestDto;
import com.loan.dto.UserProfileResponse;

public interface LoanOfficerService {
	UserProfileResponse createLoanOfficer(LoanOfficerRequestDto request);

	UserProfileResponse getLoanOfficerById(Integer id);

	List<UserProfileResponse> getAllLoanOfficers(Pageable pageable);

	UserProfileResponse updateLoanOfficer(Integer id, LoanOfficerRequestDto request);

	ApiResponse deleteLoanOfficer(Integer id);
}
