package com.loan.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.loan.service.LoanAccountService;

@Service
public class LoanDefaultCronService {

	@Autowired
	private LoanAccountService loanAccountService;

	@Scheduled(cron = "0 0 2 * * ?")
	public void markDefaultedLoansJob() {
		int count = loanAccountService.markDefaultedUsersAndLoans();
		System.out.println("Defaulted users updated: " + count);
	}
}
