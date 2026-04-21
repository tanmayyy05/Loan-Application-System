package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.ClosureType;
import com.loan.constants.EmiStatus;
import com.loan.constants.LoanAccountStatus;
import com.loan.constants.PaymentType;
import com.loan.dto.ApiResponse;
import com.loan.dto.ForecloseRequest;
import com.loan.dto.RepaymentRequest;
import com.loan.dto.RepaymentResponse;
import com.loan.entity.EmiSchedule;
import com.loan.entity.LoanAccount;
import com.loan.entity.Repayment;
import com.loan.repository.EmiScheduleRepository;
import com.loan.repository.LoanAccountRepository;
import com.loan.repository.RepaymentRepository;
import com.loan.service.RepaymentService;

@Service
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final LoanAccountRepository loanAccountRepository;

    private final EmiScheduleRepository emiScheduleRepository;

    private final ModelMapper modelMapper;

    public RepaymentServiceImpl(RepaymentRepository repaymentRepository,
            LoanAccountRepository loanAccountRepository,
            EmiScheduleRepository emiScheduleRepository,
            ModelMapper modelMapper) {
        this.repaymentRepository = repaymentRepository;
        this.loanAccountRepository = loanAccountRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.modelMapper = modelMapper;
    }

//    @Override
//    @Transactional
//    public RepaymentResponse pay(RepaymentRequest request) {
//        LoanAccount loanAccount = loanAccountRepository.findById(request.getLoanAccountId())
//                .orElseThrow(() -> new RuntimeException("Loan account not found"));
//
//        Repayment repayment = new Repayment();
//        repayment.setLoanAccount(loanAccount);
//        if (request.getEmiId() != null) {
//            EmiSchedule emi = emiScheduleRepository.findById(request.getEmiId())
//                    .orElseThrow(() -> new RuntimeException("EMI not found"));
//            repayment.setEmi(emi);
//            emi.setEmiStatus(EmiStatus.PAID);
//            emiScheduleRepository.save(emi);
//        }
//
//        if (request.getPaidAmount() == null || request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new RuntimeException("Invalid paid amount");
//        }
//
//        repayment.setPaidAmount(request.getPaidAmount());
//        repayment.setPaymentDate(LocalDate.now());
//        repayment.setPaymentMode(request.getPaymentMode());
//        repayment.setPaymentType(request.getPaymentType());
//
//        repayment = repaymentRepository.save(repayment);
//
//        BigDecimal outstanding = loanAccount.getOutstandingBalance() == null ? BigDecimal.ZERO
//                : loanAccount.getOutstandingBalance();
//
//        outstanding = outstanding.subtract(request.getPaidAmount());
//        if (outstanding.compareTo(BigDecimal.ZERO) < 0) {
//            outstanding = BigDecimal.ZERO;
//        }
//
//        loanAccount.setOutstandingBalance(outstanding);
//        if (outstanding.compareTo(BigDecimal.ZERO) == 0) {
//            loanAccount.setLoanAccountStatus(LoanAccountStatus.CLOSED);
//            loanAccount.setClosureType(ClosureType.NORMAL);
//            loanAccount.setClosureDate(LocalDate.now());
//        }
//
//        loanAccountRepository.save(loanAccount);
//
//        return modelMapper.map(repayment, RepaymentResponse.class);
//    }
    
    @Override
    @Transactional
    public RepaymentResponse pay(RepaymentRequest request) {

        if (request.getPaidAmount() == null || request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid paid amount");
        }

        EmiSchedule emi = null;
        if (request.getEmiId() != null) {
            emi = emiScheduleRepository.findById(request.getEmiId())
                    .orElseThrow(() -> new RuntimeException("EMI not found"));
        }


        LoanAccount loanAccount = emi.getLoanAccount();

        // -------------------------------
        // SAVE REPAYMENT
        // -------------------------------
        Repayment repayment = new Repayment();
        repayment.setLoanAccount(loanAccount);
        repayment.setEmi(emi);
        repayment.setPaidAmount(request.getPaidAmount());
        repayment.setPaymentDate(LocalDate.now());
        repayment.setPaymentMode(request.getPaymentMode());
        repayment.setPaymentType(request.getPaymentType());

        repayment = repaymentRepository.save(repayment);

        // -------------------------------
        // EMI STATUS UPDATE
        // -------------------------------
        if (emi != null) {
            emi.setEmiStatus(EmiStatus.PAID);
            emiScheduleRepository.save(emi);
        }

        // -------------------------------
        // OUTSTANDING UPDATE
        // -------------------------------

        BigDecimal outstanding = loanAccount.getOutstandingBalance();

        //Monthly interest calculation
        BigDecimal monthlyRate = loanAccount.getInterestRate()
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal interestForMonth = outstanding
                .multiply(monthlyRate)
                .setScale(2, RoundingMode.HALF_UP);

        //Principal paid (EMI − interest)
        BigDecimal principalPaid = emi != null
                ? emi.getEmiAmount().subtract(interestForMonth)
                : request.getPaidAmount(); // fallback (for safety)

        if (principalPaid.compareTo(BigDecimal.ZERO) < 0) {
            principalPaid = BigDecimal.ZERO;
        }

        //Reduce ONLY principal from outstanding
        outstanding = outstanding.subtract(principalPaid);

        if (outstanding.compareTo(BigDecimal.ZERO) < 0) {
            outstanding = BigDecimal.ZERO;
        }

        loanAccount.setOutstandingBalance(outstanding);

        // -------------------------------
        // LOAN CLOSURE CHECK
        // -------------------------------
//        if (outstanding.compareTo(BigDecimal.ZERO) == 0) {
//            loanAccount.setLoanAccountStatus(LoanAccountStatus.CLOSED);
//            loanAccount.setClosureType(ClosureType.NORMAL);
//            loanAccount.setClosureDate(LocalDate.now());
//        }
        
        BigDecimal CLOSURE_TOLERANCE = BigDecimal.ONE; // ₹1 tolerance

        if (outstanding.compareTo(CLOSURE_TOLERANCE) <= 0) {
            loanAccount.setOutstandingBalance(BigDecimal.ZERO);
            loanAccount.setLoanAccountStatus(LoanAccountStatus.CLOSED);
            loanAccount.setClosureType(ClosureType.NORMAL);
            loanAccount.setClosureDate(LocalDate.now());
        }

        loanAccountRepository.save(loanAccount);

        return modelMapper.map(repayment, RepaymentResponse.class);
    }

    @Override
    public List<RepaymentResponse> getByLoanAccountId(Integer loanAccountId) {
        List<Repayment> repayments = repaymentRepository.findByLoanAccountIdOrderByPaymentDateDesc(loanAccountId);
        return repayments.stream().map(r -> modelMapper.map(r, RepaymentResponse.class)).toList();
    }

//    @Override
//    @Transactional
//    public ApiResponse foreclose(ForecloseRequest request) {
//        LoanAccount loanAccount = loanAccountRepository.findById(request.getLoanAccountId())
//                .orElseThrow(() -> new RuntimeException("Loan account not found"));
//
//        if (loanAccount.getLoanAccountStatus() == LoanAccountStatus.CLOSED) {
//            return new ApiResponse("Loan already closed");
//        }
//
//        BigDecimal outstanding = loanAccount.getOutstandingBalance() == null ? BigDecimal.ZERO
//                : loanAccount.getOutstandingBalance();
//
//        if (outstanding.compareTo(BigDecimal.ZERO) > 0) {
//            Repayment repayment = new Repayment();
//            repayment.setLoanAccount(loanAccount);
//            repayment.setPaidAmount(outstanding);
//            repayment.setPaymentDate(LocalDate.now());
//            repayment.setPaymentMode(request.getPaymentMode());
//            repayment.setPaymentType(request.getPaymentType());
//            repaymentRepository.save(repayment);
//        }
//
//        loanAccount.setOutstandingBalance(BigDecimal.ZERO);
//        loanAccount.setLoanAccountStatus(LoanAccountStatus.CLOSED);
//        loanAccount.setClosureType(ClosureType.FORECLOSED);
//        loanAccount.setClosureDate(LocalDate.now());
//        loanAccountRepository.save(loanAccount);
//
//        // mark remaining EMIs as FORECLOSED
//        List<EmiSchedule> emis = emiScheduleRepository.findByLoanAccountIdOrderByEmiNumberAsc(request.getLoanAccountId());
//        for (EmiSchedule emi : emis) {
//            if (emi.getEmiStatus() != EmiStatus.PAID) {
//                emi.setEmiStatus(EmiStatus.FORECLOSED);
//            }
//        }
//        emiScheduleRepository.saveAll(emis);
//
//        return new ApiResponse("Loan foreclosed successfully");
//    }
    
	@Override
	@Transactional
	public ApiResponse foreclose(ForecloseRequest request) {

		LoanAccount loanAccount = loanAccountRepository.findById(request.getLoanAccountId())
				.orElseThrow(() -> new RuntimeException("Loan account not found"));

		if (loanAccount.getLoanAccountStatus() == LoanAccountStatus.CLOSED) {
			return new ApiResponse("Loan already closed");
		}

		BigDecimal outstanding = loanAccount.getOutstandingBalance() == null ? BigDecimal.ZERO
				: loanAccount.getOutstandingBalance();

		// -------------------------------
		// CALCULATE INTEREST TILL FORECLOSURE
		// -------------------------------
		BigDecimal foreclosureAmount = outstanding;

		if (outstanding.compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal monthlyRate = loanAccount.getInterestRate()
					.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
					.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

			// Charging full month's interest (simple & acceptable)
			BigDecimal accruedInterest = outstanding.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);

			foreclosureAmount = outstanding.add(accruedInterest);

			// SAVE FORECLOSURE REPAYMENT
			Repayment repayment = new Repayment();
			repayment.setLoanAccount(loanAccount);
			repayment.setPaidAmount(foreclosureAmount);
			repayment.setPaymentDate(LocalDate.now());
			repayment.setPaymentMode(request.getPaymentMode());
			repayment.setPaymentType(PaymentType.FORECLOSURE);
			repaymentRepository.save(repayment);
		}

		// -------------------------------
		// CLOSE LOAN ACCOUNT
		// -------------------------------
		loanAccount.setOutstandingBalance(BigDecimal.ZERO);
		loanAccount.setLoanAccountStatus(LoanAccountStatus.CLOSED);
		loanAccount.setClosureType(ClosureType.FORECLOSED);
		loanAccount.setClosureDate(LocalDate.now());
		loanAccountRepository.save(loanAccount);

		// -------------------------------
		// MARK REMAINING EMIs AS FORECLOSED
		// -------------------------------
		List<EmiSchedule> emis = emiScheduleRepository
				.findByLoanAccountIdOrderByEmiNumberAsc(request.getLoanAccountId());

		for (EmiSchedule emi : emis) {
			if (emi.getEmiStatus() != EmiStatus.PAID) {
				emi.setEmiStatus(EmiStatus.FORECLOSED);
			}
		}
		emiScheduleRepository.saveAll(emis);

		return new ApiResponse("Loan foreclosed successfully");
	}

}
