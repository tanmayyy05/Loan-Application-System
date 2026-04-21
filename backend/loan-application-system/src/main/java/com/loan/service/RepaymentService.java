package com.loan.service;

import java.util.List;

import com.loan.dto.ApiResponse;
import com.loan.dto.ForecloseRequest;
import com.loan.dto.RepaymentRequest;
import com.loan.dto.RepaymentResponse;

public interface RepaymentService {

    RepaymentResponse pay(RepaymentRequest request);

    List<RepaymentResponse> getByLoanAccountId(Integer loanAccountId);

    ApiResponse foreclose(ForecloseRequest request);

}
