package com.loan.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loan.constants.Role;
import com.loan.dto.PageResponse;
import com.loan.dto.RegisterRequestDto;
import com.loan.dto.UserProfileRequest;
import com.loan.dto.UserProfileResponse;
import com.loan.entity.User;
import com.loan.exception.UserNotFoundException;
import com.loan.repository.UserRepository;
import com.loan.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void saveUser(RegisterRequestDto registerRequestDto) {

		// check user already exist or not

		Optional<User> optional = userRepository.findByEmailOrMobileNumber(registerRequestDto.getEmail(),
				registerRequestDto.getMobileNumber());
		boolean userExists = optional.isPresent();
		if (userExists) {
			throw new RuntimeException("User already exists with given email or mobile number");
		}

		// Conversion RegisterRequestDto to User Entity
		User user = modelMapper.map(registerRequestDto, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		
		if(registerRequestDto.getDateOfBirth()!= null) {
			int age = Period.between(registerRequestDto.getDateOfBirth(), LocalDate.now()).getYears();
			user.setAge(age);
		}
		
		LocalDateTime now = LocalDateTime.now();

		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		// user.setLastLoginAt(now);
		this.userRepository.save(user);
	}

	@Override
	public UserProfileResponse updateLastLoginAt(String email) {
		User user = userRepository.findByEmailOrMobileNumber(email, email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		LocalDateTime now = LocalDateTime.now();
		user.setLastLoginAt(now);
		user.setUpdatedAt(now);

		userRepository.save(user);
		return modelMapper.map(user, UserProfileResponse.class);
	}

	@Override
	public UserProfileResponse getUserProfile(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

		return modelMapper.map(user, UserProfileResponse.class);
	}

	@Override
	public void updateUserProfile(UserProfileRequest request) {

		User user = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getId()));

		// Unique checks
		if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), request.getId())) {
			throw new RuntimeException("Email already in use");
		}

		if (request.getMobileNumber() != null
				&& userRepository.existsByMobileNumberAndIdNot(request.getMobileNumber(), request.getId())) {
			throw new RuntimeException("Mobile number already in use");
		}

		if (request.getAadhaarNumber() != null
				&& userRepository.existsByAadhaarNumberAndIdNot(request.getAadhaarNumber(), request.getId())) {
			throw new RuntimeException("Aadhaar number already in use");
		}

		if (request.getPanNumber() != null
				&& userRepository.existsByPanNumberAndIdNot(request.getPanNumber(), request.getId())) {
			throw new RuntimeException("PAN number already in use");
		}

		if (request.getBankAccount() != null
				&& userRepository.existsByBankAccountAndIdNot(request.getBankAccount(), request.getId())) {
			throw new RuntimeException("Bank account already in use");
		}

		// Map request → entity
		modelMapper.map(request, user);

		// Auto-calculate age from DOB
		if (request.getDateOfBirth() != null) {
			user.setAge(Period.between(request.getDateOfBirth(), LocalDate.now()).getYears());
		}

		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
	}

	@Override
	public void deleteUserProfile(int userId) {
		Optional<User> optional = this.userRepository.findById(userId);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setIsDeleted(true);
			user.setUpdatedAt(LocalDateTime.now());
			this.userRepository.save(user);
		}
	}

	@Override
	public PageResponse<UserProfileResponse> getAllCustomers(
	        int page, int size, String sortBy, String sortDir) {

	    Sort sort = sortDir.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<User> customerPage =
	            userRepository.findByRoleAndIsDeletedFalse(Role.USER, pageable);

	    List<UserProfileResponse> content = customerPage.getContent()
	            .stream()
	            .map(user -> modelMapper.map(user, UserProfileResponse.class))
	            .toList();

	    PageResponse<UserProfileResponse> response = new PageResponse<>();
	    response.setContent(content);
	    response.setPage(customerPage.getNumber());
	    response.setSize(customerPage.getSize());
	    response.setTotalElements(customerPage.getTotalElements());
	    response.setTotalPages(customerPage.getTotalPages());
	    response.setLast(customerPage.isLast());

	    return response;

	}


}
