package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.loan.constants.Role;
import com.loan.dto.ApiResponse;
import com.loan.dto.LoanOfficerRequestDto;
import com.loan.dto.UserProfileResponse;
import com.loan.entity.User;
import com.loan.exception.UserNotFoundException;
import com.loan.repository.UserRepository;
import com.loan.service.LoanOfficerService;
import com.loan.service.PasswordResetService;

@Service
public class LoanOfficerServiceImpl implements LoanOfficerService {
	
	@Autowired
	PasswordResetService passwordResetService;

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;

	public LoanOfficerServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserProfileResponse createLoanOfficer(LoanOfficerRequestDto request) {
		LoanOfficerRequestDto dtoCopy = LoanOfficerRequestDto.builder().fullName(request.getFullName())
				.email(request.getEmail()).mobileNumber(request.getMobileNumber()).dateOfBirth(request.getDateOfBirth())
				.address(request.getAddress()).city(request.getCity()).state(request.getState())
				.pincode(request.getPincode()).age(request.getAge()).gender(request.getGender()).build();

		User user = modelMapper.map(dtoCopy, User.class);
		user.setRole(Role.LOAN_OFFICER);
		user.setPassword(null);
		user.setIsDeleted(false);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		User saved = userRepository.save(user);
		passwordResetService.sendSetPasswordMail(saved);
		return modelMapper.map(saved, UserProfileResponse.class);		
	}

	@Override
	public UserProfileResponse getLoanOfficerById(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if (Boolean.TRUE.equals(user.getIsDeleted()) || user.getRole() != Role.LOAN_OFFICER) {
			throw new UserNotFoundException("Loan officer not found with id: " + id);
		}
		return modelMapper.map(user, UserProfileResponse.class);
	}

	@Override
	public List<UserProfileResponse> getAllLoanOfficers(Pageable pageable) {
		Page<User> page = userRepository.findByRoleAndIsDeletedFalse(Role.LOAN_OFFICER, pageable);
		List<User> users = page.getContent();
		return users.stream().map(u -> modelMapper.map(u, UserProfileResponse.class)).collect(Collectors.toList());
	}

	@Override
	public UserProfileResponse updateLoanOfficer(Integer id, LoanOfficerRequestDto request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if (Boolean.TRUE.equals(user.getIsDeleted()) || user.getRole() != Role.LOAN_OFFICER) {
			throw new UserNotFoundException("Loan officer not found with id: " + id);
		}

		LoanOfficerRequestDto dtoCopy = LoanOfficerRequestDto.builder().fullName(request.getFullName())
				.email(request.getEmail()).mobileNumber(request.getMobileNumber()).dateOfBirth(request.getDateOfBirth())
				.address(request.getAddress()).city(request.getCity()).state(request.getState())
				.pincode(request.getPincode()).age(request.getAge()).gender(request.getGender()).build();

		modelMapper.map(dtoCopy, user);
		user.setRole(Role.LOAN_OFFICER); // enforce role
		user.setUpdatedAt(LocalDateTime.now());

		User saved = userRepository.save(user);
		return modelMapper.map(saved, UserProfileResponse.class);
	}

	@Override
	public ApiResponse deleteLoanOfficer(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if (Boolean.TRUE.equals(user.getIsDeleted()) || user.getRole() != Role.LOAN_OFFICER) {
			throw new UserNotFoundException("Loan officer not found with id: " + id);
		}
		user.setIsDeleted(true);
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
		return new ApiResponse("Loan officer deleted successfully");
	}

}
