package com.loan.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.constants.Role;
import com.loan.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// Optional<User> findByEmail(String email);
	Optional<User> findByEmailOrMobileNumber(String email, String mobileNumber);

	boolean existsByEmailAndIdNot(String email, Integer id);

	boolean existsByMobileNumberAndIdNot(String mobileNumber, Integer id);

	boolean existsByAadhaarNumberAndIdNot(String aadhaarNumber, Integer id);

	boolean existsByBankAccountAndIdNot(String bankAccount, Integer id);

	boolean existsByPanNumberAndIdNot(String panNumber, Integer id);

    Page<User> findByRoleAndIsDeletedFalse(Role role, Pageable pageable);

	Optional<User> findByEmail(String email);

	Optional<User> findByRole(Role role);
	
	long countByRoleAndIsDeletedFalse(Role role);

}