package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan.annotation.CurrentUser;
import com.loan.dto.ApiResponse;
import com.loan.dto.LoanActionRequest;
import com.loan.dto.LoanApplicationDetailsResponseDto;
import com.loan.dto.LoanApplicationRequestDto;
import com.loan.dto.LoanApplicationResponseDto;
import com.loan.dto.LoanEligibilityResponse;
import com.loan.dto.PageResponse;
import com.loan.entity.User;
import com.loan.service.LoanApplicationService;
import com.loan.service.LoanEligibilityService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/loan-applications")
@CrossOrigin(origins = "*")
public class LoanApplicationController {

	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	LoanEligibilityService loanEligibilityService;

	@Operation(summary = "Apply for Loan", description = "Logged-in user applies for a loan")
	@PostMapping("/apply")
	public ResponseEntity<LoanApplicationResponseDto> applyLoan(
			@RequestBody LoanApplicationRequestDto loanApplicationRequestDto) {
		System.out.println(" Loan Application Controller ");
		LoanApplicationResponseDto loanApplicationResponseDto = loanApplicationService
				.applyForLoan(loanApplicationRequestDto);
		return new ResponseEntity<LoanApplicationResponseDto>(loanApplicationResponseDto, HttpStatus.CREATED);

	}

	// All applications of a user (with pagination & sorting)
	@GetMapping("/user")
	public ResponseEntity<PageResponse<LoanApplicationResponseDto>> getUserLoanApplications(
			@CurrentUser User currentUser,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    PageResponse<LoanApplicationResponseDto> response =
	            loanApplicationService.getByUserId(currentUser.getId(), page, size, sortBy, sortDir);

	    return ResponseEntity.ok(response);
	}


	// Single application with documents
	@GetMapping("/{id}")
	public ResponseEntity<LoanApplicationDetailsResponseDto> getApplicationDetails(@PathVariable Integer id) {
		 System.out.println("Controller reached with id = " + id);
		return ResponseEntity.ok(loanApplicationService.getDetailsById(id));
	}

	// Get loan applications by status (or all) with pagination & sorting
	@GetMapping
	public ResponseEntity<PageResponse<LoanApplicationResponseDto>> getLoanApplications(
	        @RequestParam(required = false, defaultValue = "ALL") String status,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "desc") String sortDir) {

	    return ResponseEntity.ok(
	            loanApplicationService.getLoanApplicationsByStatus(
	                    status, page, size, sortBy, sortDir
	            )
	    );
	}
	
	 @PutMapping("/submit/{applicationId}")
	    public ResponseEntity<ApiResponse> submitLoan(@PathVariable Integer applicationId,
	                                                   @CurrentUser User currentUser) {
	        ApiResponse response = loanApplicationService.submit(applicationId, currentUser);
	        return ResponseEntity.ok(response);
	    }

	 @PutMapping("/approve")
	    public ResponseEntity<ApiResponse> approveLoan(@RequestBody LoanActionRequest request,
	                                                   @CurrentUser User currentUser) {
	        ApiResponse response = loanApplicationService.approve(request, currentUser);
	        return ResponseEntity.ok(response);
	    }

	    @PutMapping("/reject")
	    public ResponseEntity<ApiResponse> rejectLoan(@RequestBody LoanActionRequest request,
	                                                  @CurrentUser User currentUser) {
	        ApiResponse response = loanApplicationService.reject(request, currentUser);
	        return ResponseEntity.ok(response);
	    }
	    
		@PostMapping("/{applicationId}/disburse")
		public ResponseEntity<ApiResponse> disburse(@PathVariable("applicationId") int applicationId,
				@CurrentUser User currentUser) {
			ApiResponse response = loanApplicationService.disburseLoan(applicationId, currentUser);
			return ResponseEntity.ok(response);
		}
		
		@GetMapping("/latest")
		public ResponseEntity<LoanApplicationDetailsResponseDto> getLatestLoanApplication(
		        @CurrentUser User currentUser) {

		    LoanApplicationDetailsResponseDto response =
		            loanApplicationService.getLatestApplication(currentUser);

		    return ResponseEntity.ok(response);
		}

		@GetMapping("/can-apply")
		public ResponseEntity<LoanEligibilityResponse> canApply(
		        @CurrentUser User user
		) {
		    return ResponseEntity.ok(
		            loanEligibilityService.canApplyForLoan(user)
		    );
		}

	    
}
