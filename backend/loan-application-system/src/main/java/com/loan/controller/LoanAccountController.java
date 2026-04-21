package com.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.annotation.CurrentUser;
import com.loan.constants.LoanAccountStatus;
import com.loan.constants.Role;
import com.loan.dto.ApiResponse;
import com.loan.dto.LoanAccountResponseDto;
import com.loan.entity.User;
import com.loan.service.LoanAccountService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/loan-accounts")
@CrossOrigin(origins = "*")
public class LoanAccountController {

	@Autowired
	private LoanAccountService loanAccountService;

	@Operation(summary = "Get Loan Accounts by status", description = "Returns loan accounts filtered by LoanAccountStatus")
	@GetMapping("/getByStatus/{status}")
	public ResponseEntity<List<LoanAccountResponseDto>> getByStatus(@PathVariable String status, Pageable pageable) {

		LoanAccountStatus loanAccountStatus;
		try {
			loanAccountStatus = LoanAccountStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}

		List<LoanAccountResponseDto> response = loanAccountService.getByStatus(loanAccountStatus, pageable);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/defaulted/run")
	public ApiResponse runDefaultCron(@CurrentUser User currentUser) {

		if (currentUser.getRole() != Role.ADMIN) {
			return new ApiResponse("Unauthorized");
		}

		int count = loanAccountService.markDefaultedUsersAndLoans();
		return new ApiResponse(count + " loan accounts marked as DEFAULTED");
	}
	
	@GetMapping("/{loanAccountId}")
	public ResponseEntity<LoanAccountResponseDto> getById(
	        @PathVariable Integer loanAccountId
	) {
	    LoanAccountResponseDto response =
	            loanAccountService.getLoanAccountById(loanAccountId);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/my")
	public ResponseEntity<List<LoanAccountResponseDto>> getMyLoanAccounts(
	        @CurrentUser User currentUser
	) {
	    List<LoanAccountResponseDto> response =
	            loanAccountService.getLoanAccountsByUser(currentUser);

	    return ResponseEntity.ok(response);
	}


}
