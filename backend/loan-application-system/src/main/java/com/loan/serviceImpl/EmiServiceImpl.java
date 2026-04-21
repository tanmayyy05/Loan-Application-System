package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.constants.EmiStatus;
import com.loan.constants.LoanAccountStatus;
import com.loan.dto.EmiListResponse;
import com.loan.dto.EmiScheduleResponse;
import com.loan.entity.EmiSchedule;
import com.loan.entity.LoanAccount;
import com.loan.entity.User;
import com.loan.repository.EmiScheduleRepository;
import com.loan.repository.LoanAccountRepository;
import com.loan.service.EmiService;

@Service
public class EmiServiceImpl implements EmiService {

	@Autowired
	private LoanAccountRepository loanAccountRepository;
	
	@Autowired
	private EmiScheduleRepository emiScheduleRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
    public List<EmiScheduleResponse> generateEmiSchedule(Integer loanAccountId) {

        LoanAccount loanAccount = loanAccountRepository.findById(loanAccountId)
                .orElseThrow(() -> new RuntimeException("Loan account not found"));

        if (loanAccount.getLoanAccountStatus() != LoanAccountStatus.ACTIVE) {
            throw new RuntimeException("EMI can be generated only for ACTIVE loans");
        }

        // Avoid duplicate EMI generation
        if (emiScheduleRepository.existsByLoanAccount_Id(loanAccountId)) {
            throw new RuntimeException("EMI schedule already generated");
        }

        BigDecimal principal = loanAccount.getLoanAmount();
        BigDecimal annualRate = loanAccount.getInterestRate();
        int tenure = loanAccount.getTenureMonths();

        BigDecimal emiAmount = calculateEmi(principal, annualRate, tenure);

        List<EmiSchedule> emiSchedules = new ArrayList<>();
        LocalDate dueDate = loanAccount.getDisbursedDate().plusMonths(1);

        for (int i = 1; i <= tenure; i++) {
            EmiSchedule emi = new EmiSchedule();
            emi.setLoanAccount(loanAccount);
            emi.setEmiNumber(i);
            emi.setDueDate(dueDate);
            emi.setEmiAmount(emiAmount);
            emi.setEmiStatus(EmiStatus.PENDING);
            emi.setIsPenaltyApplied(false);
            emi.setPenaltyAmount(BigDecimal.ZERO);
            emi.setTotalPayableAmount(emiAmount);

            emiSchedules.add(emi);
            dueDate = dueDate.plusMonths(1);
        }

        emiScheduleRepository.saveAll(emiSchedules);
        return emiSchedules.stream()
                .map(emi -> modelMapper.map(emi, EmiScheduleResponse.class))
                .toList();
    }

    private BigDecimal calculateEmi(BigDecimal principal,
                                   BigDecimal annualRate,
                                   int tenureMonths) {

        BigDecimal monthlyRate =
                annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRPowerN =
                monthlyRate.add(BigDecimal.ONE).pow(tenureMonths);

        BigDecimal numerator =
                principal.multiply(monthlyRate).multiply(onePlusRPowerN);

        BigDecimal denominator =
                onePlusRPowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

	@Override
	public List<EmiScheduleResponse> getEmisByLoanAccountId(Integer loanAccountId) {
		 List<EmiSchedule> emis =
	                emiScheduleRepository.findByLoanAccountIdOrderByEmiNumberAsc(loanAccountId);

	        return emis.stream()
	                .map(emi -> modelMapper.map(emi, EmiScheduleResponse.class))
	                .toList();
	}

	@Override
	public EmiListResponse getCurrentLoanEmis(User user) {

	    Optional<LoanAccount> optionalLoanAccount =
	            loanAccountRepository.findByUserIdAndLoanAccountStatus(
	                    user.getId(),
	                    LoanAccountStatus.ACTIVE
	            );

	    // ✅ No active loan
	    if (optionalLoanAccount.isEmpty()) {
	        return new EmiListResponse(
	                false,
	                "You do not have any active loan",
	                List.of()
	        );
	    }

	    LoanAccount loanAccount = optionalLoanAccount.get();

	    List<EmiScheduleResponse> emis =
	            emiScheduleRepository
	                    .findByLoanAccountIdOrderByEmiNumberAsc(
	                            loanAccount.getId()
	                    )
	                    .stream()
	                    .map(emi -> modelMapper.map(emi, EmiScheduleResponse.class))
	                    .toList();

	    return new EmiListResponse(
	            true,
	            "Active loan EMI schedule fetched successfully",
	            emis
	    );
	}

}
