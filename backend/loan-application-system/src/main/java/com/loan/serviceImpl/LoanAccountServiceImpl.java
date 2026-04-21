package com.loan.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.LoanAccountStatus;
import com.loan.dto.LoanAccountResponseDto;
import com.loan.entity.LoanAccount;
import com.loan.entity.User;
import com.loan.repository.EmiScheduleRepository;
import com.loan.repository.LoanAccountRepository;
import com.loan.repository.UserRepository;
import com.loan.service.LoanAccountService;

@Service
public class LoanAccountServiceImpl implements LoanAccountService {

	@Autowired
	private LoanAccountRepository loanAccountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmiScheduleRepository emiScheduleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<LoanAccountResponseDto> getByStatus(LoanAccountStatus status, Pageable pageable) {

		var page = loanAccountRepository.findByLoanAccountStatus(status, pageable);

		return page.stream().map(a -> {
			LoanAccountResponseDto dto = modelMapper.map(a, LoanAccountResponseDto.class);
			dto.setApplicationId(a.getApplication() != null ? a.getApplication().getId() : null);
			dto.setUserId(a.getUser() != null ? a.getUser().getId() : null);
			return dto;
		}).toList();
	}

	@Override
	@Transactional
	public int markDefaultedUsersAndLoans() {

		LocalDate cutoffDate = LocalDate.now().minusDays(90);

		List<LoanAccount> loanAccounts = emiScheduleRepository.findLoanAccountsWithEmiOverdue90Days(cutoffDate);

		int defaultedCount = 0;

		for (LoanAccount loanAccount : loanAccounts) {

			if (loanAccount.getLoanAccountStatus() == LoanAccountStatus.DEFAULTED) {
				continue;
			}

			// Mark loan defaulted
			loanAccount.setLoanAccountStatus(LoanAccountStatus.DEFAULTED);
			loanAccountRepository.save(loanAccount);

			// Mark user defaulted
			User user = loanAccount.getUser();
			if (!Boolean.TRUE.equals(user.getIsDefaulted())) {
				user.setIsDefaulted(true);
				userRepository.save(user);
			}

			defaultedCount++;
		}

		return defaultedCount;
	}

	@Override
	public LoanAccountResponseDto getLoanAccountById(Integer loanAccountId) {

	    LoanAccount loanAccount = loanAccountRepository.findById(loanAccountId)
	            .orElseThrow(() -> new RuntimeException("Loan account not found"));

	    return new LoanAccountResponseDto(
	            loanAccount.getId(),
	            loanAccount.getApplication().getId(),
	            loanAccount.getUser().getId(),
	            loanAccount.getLoanAmount(),
	            loanAccount.getInterestRate(),
	            loanAccount.getTenureMonths(),
	            loanAccount.getEmiAmount(),
	            loanAccount.getOutstandingBalance(),
	            loanAccount.getLoanAccountStatus(),
	            loanAccount.getClosureType(),
	            loanAccount.getDisbursedDate(),
	            loanAccount.getClosureDate()
	    );
	}

	@Override
	public List<LoanAccountResponseDto> getLoanAccountsByUser(User user) {

	    List<LoanAccount> accounts =
	            loanAccountRepository.findByUserId(user.getId());

	    return accounts.stream()
	            .map(account -> new LoanAccountResponseDto(
	                    account.getId(),
	                    account.getApplication().getId(),
	                    account.getUser().getId(),
	                    account.getLoanAmount(),
	                    account.getInterestRate(),
	                    account.getTenureMonths(),
	                    account.getEmiAmount(),
	                    account.getOutstandingBalance(),
	                    account.getLoanAccountStatus(),
	                    account.getClosureType(),
	                    account.getDisbursedDate(),
	                    account.getClosureDate()
	            ))
	            .toList();
	}



}
