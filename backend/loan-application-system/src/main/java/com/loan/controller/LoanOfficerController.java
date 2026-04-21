package com.loan.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.dto.LoanOfficerRequestDto;
import com.loan.dto.UserProfileResponse;
import com.loan.service.LoanOfficerService;

@RestController
@RequestMapping("/api/loanOfficer")
@CrossOrigin(origins = "*")
public class LoanOfficerController {

	private final LoanOfficerService loanOfficerService;

	public LoanOfficerController(LoanOfficerService loanOfficerService) {
		this.loanOfficerService = loanOfficerService;
	}

	@PostMapping("/addProfile")
	public ResponseEntity<UserProfileResponse> addProfile(@RequestBody LoanOfficerRequestDto request) {
		return ResponseEntity.ok(loanOfficerService.createLoanOfficer(request));
	}

	@GetMapping("/viewProfile/{id}")
	public ResponseEntity<UserProfileResponse> viewProfile(@PathVariable Integer id) {
		return ResponseEntity.ok(loanOfficerService.getLoanOfficerById(id));
	}

	@GetMapping("/viewAllProfiles")
	public ResponseEntity<List<UserProfileResponse>> viewAllProfiles(Pageable pageable) {
		return ResponseEntity.ok(loanOfficerService.getAllLoanOfficers(pageable));
	}

	@PutMapping("/updateProfile/{id}")
	public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable Integer id,
			@RequestBody LoanOfficerRequestDto request) {
		return ResponseEntity.ok(loanOfficerService.updateLoanOfficer(id, request));
	}

	@DeleteMapping("/deleteProfile/{id}")
	public ResponseEntity<ApiResponse> deleteProfile(@PathVariable Integer id) {
		return ResponseEntity.ok(loanOfficerService.deleteLoanOfficer(id));
	}

}
