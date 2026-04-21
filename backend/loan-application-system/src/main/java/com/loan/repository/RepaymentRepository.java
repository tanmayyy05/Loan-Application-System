package com.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.Repayment;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Integer> {

    List<Repayment> findByLoanAccountIdOrderByPaymentDateDesc(Integer loanAccountId);

}
