package com.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.annotation.CurrentUser;
import com.loan.dto.EmiListResponse;
import com.loan.dto.EmiScheduleResponse;
import com.loan.entity.User;
import com.loan.service.EmiService;

@RestController
@RequestMapping("/api/emi")
@CrossOrigin(origins = "*")
public class EmiController {

	@Autowired
	private EmiService emiService;

	@GetMapping("/generate/{loanAccountId}")
	public ResponseEntity<List<EmiScheduleResponse>> generateEmiSchedule(@PathVariable Integer loanAccountId) {

		List<EmiScheduleResponse> emiSchedules = emiService.generateEmiSchedule(loanAccountId);

		return ResponseEntity.ok(emiSchedules);
	}

	@GetMapping("/getByLoanAccountId/{loanAccountId}")
	public ResponseEntity<List<EmiScheduleResponse>> getEmisByLoanAccountId(@PathVariable Integer loanAccountId) {

		List<EmiScheduleResponse> response = emiService.getEmisByLoanAccountId(loanAccountId);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/current")
	public ResponseEntity<EmiListResponse> getCurrentLoanEmis(
	        @CurrentUser User currentUser
	) {
	    return ResponseEntity.ok(
	            emiService.getCurrentLoanEmis(currentUser)
	    );
	}


}
