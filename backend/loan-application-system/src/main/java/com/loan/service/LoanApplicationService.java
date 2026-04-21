package com.loan.service;

import java.util.List;

import com.loan.dto.ApiResponse;
import com.loan.dto.LoanActionRequest;
import com.loan.dto.LoanApplicationDetailsResponseDto;
import com.loan.dto.LoanApplicationRequestDto;
import com.loan.dto.LoanApplicationResponseDto;
import com.loan.dto.PageResponse;
import com.loan.entity.User;

public interface LoanApplicationService {

	LoanApplicationResponseDto applyForLoan(LoanApplicationRequestDto requestDto);

	PageResponse<LoanApplicationResponseDto> getByUserId(Integer userId, int page, int size, String sortBy, String sortDir);

	LoanApplicationDetailsResponseDto getDetailsById(Integer applicationId);

	PageResponse<LoanApplicationResponseDto> getLoanApplicationsByStatus(String status, int page, int size, String sortBy, String sortDir);

	ApiResponse approve(LoanActionRequest request, User currentUser);

	ApiResponse reject(LoanActionRequest request, User currentUser);

	ApiResponse submit(Integer applicationId, User currentUser);

	ApiResponse disburseLoan(int applicationId, User currentUser);

	LoanApplicationDetailsResponseDto getLatestApplication(User currentUser);

}
