package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.Role;
import com.loan.dto.AdminDashboardResponse;
import com.loan.dto.LoanOfficerDashboardResponse;
import com.loan.repository.LoanAccountRepository;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.UserRepository;
import com.loan.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Autowired
	private LoanAccountRepository loanAccountRepository;

	@Override
	public AdminDashboardResponse getAdminDashboard() {
		long totalUsers = userRepository.countByRoleAndIsDeletedFalse(Role.USER);

		    long totalApplications = loanApplicationRepository.count();

		    long pendingApprovals =
		            loanApplicationRepository.countByApplicationStatusIn(
		                    List.of(
		                            ApplicationStatus.DOCUMENT_APPROVED
		                    )
		            );

		    BigDecimal totalAmountDisbursed =
		            loanAccountRepository.getTotalDisbursedAmount();

		    if (totalAmountDisbursed == null) {
		        totalAmountDisbursed = BigDecimal.ZERO;
		    }

		    return new AdminDashboardResponse(
		            totalUsers,
		            totalApplications,
		            pendingApprovals,
		            totalAmountDisbursed
		    );
	}

	@Override
	public LoanOfficerDashboardResponse getLoanOfficerDashboard(Integer officerId) {
		long totalApplications =
                loanApplicationRepository.count();

        long pendingVerification =
                loanApplicationRepository.countByApplicationStatusIn(
                       List.of(ApplicationStatus.DOCUMENT_VERIFICATION_PENDING)
                );

        long returned =
                loanApplicationRepository.countByApplicationStatusIn(
                     List.of(ApplicationStatus.DOCUMENT_RETURNED_FOR_CORRECTION)
                );

        long rejected =
                loanApplicationRepository.countByApplicationStatusIn(
                       List.of(ApplicationStatus.DOCUMENT_REJECTED)
                );

        return new LoanOfficerDashboardResponse(
                totalApplications,
                pendingVerification,
                returned,
                rejected
        );
	}

}