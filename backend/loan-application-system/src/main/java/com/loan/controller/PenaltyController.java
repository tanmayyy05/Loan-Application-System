package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.EmiScheduleResponse;
import com.loan.service.PenaltyService;

@RestController
@RequestMapping("/api/penalty")
@CrossOrigin(origins = "*")
public class PenaltyController {
	@Autowired
	private PenaltyService penaltyService;

	@PostMapping("/calculate/{loanAccountId}")
	public ResponseEntity<EmiScheduleResponse> calculatePenalty(@PathVariable("loanAccountId") Integer loanAccountId) {

		EmiScheduleResponse response = penaltyService.calculatePenalty(loanAccountId);

		return ResponseEntity.ok(response);
	}
}
