package com.loan.service;

import com.loan.dto.EmiScheduleResponse;

public interface PenaltyService {

	EmiScheduleResponse calculatePenalty(Integer loanAccountId);

}
