package com.loan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.loan.constants.LoanAccountStatus;
import com.loan.dto.LoanAccountResponseDto;
import com.loan.entity.User;

public interface LoanAccountService {

	List<LoanAccountResponseDto> getByStatus(LoanAccountStatus status, Pageable pageable);

	int markDefaultedUsersAndLoans();

	LoanAccountResponseDto getLoanAccountById(Integer loanAccountId);

	List<LoanAccountResponseDto> getLoanAccountsByUser(User currentUser);


}
