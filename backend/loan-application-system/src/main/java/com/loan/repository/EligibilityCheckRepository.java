package com.loan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.EligibilityCheck;

public interface EligibilityCheckRepository extends JpaRepository<EligibilityCheck, Integer> {

	Optional<EligibilityCheck> findByApplicationId(Integer applicationId);
}
