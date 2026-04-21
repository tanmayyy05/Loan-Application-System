package com.loan.service;

import java.util.List;

import com.loan.dto.EmiListResponse;
import com.loan.dto.EmiScheduleResponse;
import com.loan.entity.User;

public interface EmiService {

	List<EmiScheduleResponse> generateEmiSchedule(Integer loanAccountId);

	List<EmiScheduleResponse> getEmisByLoanAccountId(Integer loanAccountId);

	EmiListResponse getCurrentLoanEmis(User currentUser);

}
