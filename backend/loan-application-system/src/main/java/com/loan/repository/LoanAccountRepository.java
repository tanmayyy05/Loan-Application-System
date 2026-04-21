package com.loan.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loan.constants.LoanAccountStatus;
import com.loan.entity.LoanAccount;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {

	Page<LoanAccount> findByLoanAccountStatus(LoanAccountStatus loanAccountStatus, Pageable pageable);

	boolean existsByApplication_Id(int applicationId);

	List<LoanAccount> findByUserId(Integer id);

	Optional<LoanAccount> findByUserIdAndLoanAccountStatus(Integer id, LoanAccountStatus active);

	@Query("SELECT COALESCE(SUM(l.loanAmount), 0) FROM LoanAccount l")
	BigDecimal getTotalDisbursedAmount();

}