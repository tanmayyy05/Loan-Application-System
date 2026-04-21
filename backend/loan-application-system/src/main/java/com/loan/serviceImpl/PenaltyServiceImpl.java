package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.EmiStatus;
import com.loan.dto.EmiScheduleResponse;
import com.loan.entity.EmiSchedule;
import com.loan.repository.EmiScheduleRepository;
import com.loan.service.PenaltyService;
@Service
public class PenaltyServiceImpl implements PenaltyService {

    // Penalty rate = 2% per late month
    private static final BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.02);

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Penalty Rule:
     * 1. Penalty is applied ONLY to the oldest unpaid (PENDING) EMI.
     * 2. Penalty is calculated as 2% of EMI amount per late month.
     * 3. Next EMI penalty starts only after previous EMI is paid.
     * 4. This avoids stacked penalties and matches real banking behavior.
     */
    @Override
    @Transactional
    public EmiScheduleResponse calculatePenalty(Integer loanAccountId) {

        // Fetch the oldest unpaid EMI
        EmiSchedule emi = emiScheduleRepository
                .findFirstByLoanAccountIdAndEmiStatusOrderByDueDateAsc(
                        loanAccountId, EmiStatus.OVERDUE)
                .orElseThrow(() -> new RuntimeException("No pending EMI found"));

        LocalDate today = LocalDate.now();

        // Calculate number of late months (calendar-month based)
        int lateMonths = (int) java.time.temporal.ChronoUnit.MONTHS
                .between(emi.getDueDate().withDayOfMonth(1),
                         today.withDayOfMonth(1));

        if (lateMonths <= 0) {
            return modelMapper.map(emi, EmiScheduleResponse.class);
        }

        // Penalty = EMI amount × 2% × number of late months
        BigDecimal penaltyAmount = emi.getEmiAmount()
                .multiply(PENALTY_RATE)
                .multiply(BigDecimal.valueOf(lateMonths))
                .setScale(2, RoundingMode.HALF_UP);

        // Update EMI with penalty details
        emi.setPenaltyAmount(penaltyAmount);
        emi.setIsPenaltyApplied(true);
        emi.setTotalPayableAmount(
                emi.getEmiAmount().add(penaltyAmount)
        );

        emiScheduleRepository.save(emi);

        return modelMapper.map(emi, EmiScheduleResponse.class);
    }
}
