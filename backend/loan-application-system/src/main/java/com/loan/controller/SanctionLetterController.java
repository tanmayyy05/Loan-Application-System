package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.dto.SanctionLetterResponseDto;
import com.loan.service.SanctionLetterService;

@RestController
@RequestMapping("/api/sanction-letter")
@CrossOrigin(origins = "*")
public class SanctionLetterController {

	@Autowired
	private SanctionLetterService sanctionLetterService;

	@GetMapping("/{loanApplicationId}")
	public ResponseEntity<SanctionLetterResponseDto> getSanctionLetter(@PathVariable Integer loanApplicationId) {

		return ResponseEntity.ok(sanctionLetterService.getByLoanApplicationId(loanApplicationId));
	}

	@PutMapping("/{id}/accept")
	public ResponseEntity<ApiResponse> acceptSanctionLetter(@PathVariable Integer id) {

		sanctionLetterService.acceptSanctionLetter(id);
		return ResponseEntity.ok(new ApiResponse("Sanction Letter accepted successfully"));
	}

	@PutMapping("/{id}/reject")
	public ResponseEntity<ApiResponse> rejectSanctionLetter(@PathVariable Integer id) {

		sanctionLetterService.rejectSanctionLetter(id);
		return ResponseEntity.ok(new ApiResponse("Sanction Letter rejected successfully"));
	}
	
	//Admin sanction loan and send mail
//	@PostMapping("/{loanApplicationId}/send")
//  public ResponseEntity<ApiResponse> sanctionLoan(@PathVariable Integer loanApplicationId) {
//		sanctionLetterService.sanctionLoan(loanApplicationId);
//	   return ResponseEntity.ok(new ApiResponse("Loan sanctioned and email sent"));		
//	}
//	
//	@PostMapping("/reject/{applicationId}")
//    public ResponseEntity<ApiResponse> rejectLoan(@PathVariable Integer applicationId, @RequestParam String reason) {
//
//		sanctionLetterService.rejectLoan(applicationId, reason);
//        return ResponseEntity.ok(new ApiResponse("Loan application rejected and email sent"));
//    }
}
