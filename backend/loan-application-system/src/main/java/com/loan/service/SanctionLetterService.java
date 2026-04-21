package com.loan.service;

import com.loan.dto.SanctionLetterResponseDto;

public interface SanctionLetterService {

	SanctionLetterResponseDto getByLoanApplicationId(Integer loanApplicationId);

	void acceptSanctionLetter(Integer sanctionLetterId);

	void rejectSanctionLetter(Integer sanctionLetterId);
	
	public void sanctionLoan(Integer applicationId);
	
	 void rejectLoan(Integer applicationId, String rejectionReason);

}
