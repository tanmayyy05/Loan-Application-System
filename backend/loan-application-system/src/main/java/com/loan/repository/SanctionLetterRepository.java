package com.loan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.SanctionLetter;

@Repository
public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, Integer> {

    Optional<SanctionLetter> findByApplicationId(Integer applicationId);
}
