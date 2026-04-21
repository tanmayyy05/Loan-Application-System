package com.loan.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.loan.constants.EmiStatus;
import com.loan.entity.EmiSchedule;
import com.loan.entity.LoanAccount;

@Repository
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Integer> {

	boolean existsByLoanAccount_Id(Integer loanAccountId);

	List<EmiSchedule> findByLoanAccountIdOrderByEmiNumberAsc(Integer loanAccountId);

	Optional<EmiSchedule> findFirstByLoanAccountIdAndEmiStatusOrderByDueDateAsc(Integer loanAccountId,
			EmiStatus emiStatus);

	List<EmiSchedule> findByEmiStatusAndDueDateBefore(EmiStatus pending, LocalDate today);

	@Query("""
			    SELECT DISTINCT la
			    FROM EmiSchedule e
			    JOIN e.loanAccount la
			    WHERE e.emiStatus = com.loan.constants.EmiStatus.OVERDUE
			      AND e.dueDate <= :cutoffDate
			""")
	List<LoanAccount> findLoanAccountsWithEmiOverdue90Days(@Param("cutoffDate") LocalDate cutoffDate);
}
