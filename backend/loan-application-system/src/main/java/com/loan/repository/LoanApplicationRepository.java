package com.loan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.constants.ApplicationStatus;
import com.loan.entity.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {

	List<LoanApplication> findByUserId(Integer userId);

	Page<LoanApplication> findByUserId(Integer userId, Pageable pageable);

	List<LoanApplication> findByApplicationStatus(ApplicationStatus applicationStatus);

	Page<LoanApplication> findByApplicationStatus(ApplicationStatus applicationStatus, Pageable pageable);

	Optional<LoanApplication> findTopByUser_IdOrderByAppliedAtDesc(Integer id);
	
	List<LoanApplication> findByApplicationStatusNot(ApplicationStatus applicationStatus);

	Optional<LoanApplication> findTopByUserIdAndApplicationStatusInOrderByAppliedAtDesc(Integer id,
			List<ApplicationStatus> of);

	long countByApplicationStatusIn(List<ApplicationStatus> of);
}