package com.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.dto.ForecloseRequest;
import com.loan.dto.RepaymentRequest;
import com.loan.dto.RepaymentResponse;
import com.loan.service.RepaymentService;

@RestController
@RequestMapping("/api/repayments")
@CrossOrigin(origins = "*")
public class RepaymentController {

    @Autowired
    private RepaymentService repaymentService;

    @PostMapping("/pay")
    public ResponseEntity<RepaymentResponse> pay(@RequestBody RepaymentRequest request) {
        RepaymentResponse response = repaymentService.pay(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{loanAccountId}")
    public ResponseEntity<List<RepaymentResponse>> getByLoanAccountId(@PathVariable Integer loanAccountId) {
        List<RepaymentResponse> response = repaymentService.getByLoanAccountId(loanAccountId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/foreclose")
    public ResponseEntity<ApiResponse> foreclose(@RequestBody ForecloseRequest request) {
        ApiResponse response = repaymentService.foreclose(request);
        return ResponseEntity.ok(response);
    }

}
