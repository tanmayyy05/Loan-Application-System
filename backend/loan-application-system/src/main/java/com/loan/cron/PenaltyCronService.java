package com.loan.cron;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.EmiStatus;
import com.loan.entity.EmiSchedule;
import com.loan.repository.EmiScheduleRepository;
import com.loan.service.PenaltyService;

@Service
public class PenaltyCronService {

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Autowired
    private PenaltyService penaltyService;

    /**
     * Auto Penalty Cron Job
     * Cron job to run every day at 12:00 AM
     * 1️ Mark overdue EMIs
     * 2️ Apply penalty on oldest overdue EMI per loan account
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight
    public void applyOverduePenalties() {

        LocalDate today = LocalDate.now();

        // Step 1: Mark all PENDING EMIs as OVERDUE if past due date
        List<EmiSchedule> emisToOverdue = emiScheduleRepository
                .findByEmiStatusAndDueDateBefore(EmiStatus.PENDING, today);

        for (EmiSchedule emi : emisToOverdue) {
            emi.setEmiStatus(EmiStatus.OVERDUE);
        }
        emiScheduleRepository.saveAll(emisToOverdue);

        // Step 2: Apply penalty to oldest OVERDUE EMI per loan account
        List<Integer> loanAccountIds = emiScheduleRepository.findAll()
                .stream()
                .filter(e -> e.getEmiStatus() == EmiStatus.OVERDUE)
                .map(e -> e.getLoanAccount().getId())
                .distinct()
                .toList();

        for (Integer loanAccountId : loanAccountIds) {
            try {
                penaltyService.calculatePenalty(loanAccountId);
            } catch (Exception e) {
                // log error but continue processing other loans
                System.out.println("Error applying penalty for loanAccountId "
                        + loanAccountId + ": " + e.getMessage());
            }
        }
    }
}
