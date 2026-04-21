package com.loan.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.EmailTemplateName;
import com.loan.constants.LoanAccountStatus;
import com.loan.constants.Role;
import com.loan.constants.SanctionStatus;
import com.loan.dto.ApiResponse;
import com.loan.dto.DocumentResponseDto;
import com.loan.dto.LoanActionRequest;
import com.loan.dto.LoanApplicationDetailsResponseDto;
import com.loan.dto.LoanApplicationRequestDto;
import com.loan.dto.LoanApplicationResponseDto;
import com.loan.dto.PageResponse;
import com.loan.entity.EligibilityCheck;
import com.loan.entity.LoanAccount;
import com.loan.entity.LoanApplication;
import com.loan.entity.SanctionLetter;
import com.loan.entity.User;
import com.loan.repository.DocumentUploadRepository;
import com.loan.repository.EligibilityCheckRepository;
import com.loan.repository.LoanAccountRepository;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.SanctionLetterRepository;
import com.loan.repository.UserRepository;
import com.loan.service.EmailSendService;
import com.loan.service.EmiCalculationService;
import com.loan.service.LoanApplicationService;
import com.loan.service.LoanEligibilityService;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

	@Autowired
	LoanEligibilityService loanEligibilityService;

	@Autowired
	LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	EmiCalculationService emiCalculationService;

	@Autowired
	private EligibilityCheckRepository eligibilityCheckRepository;

	@Autowired
	private DocumentUploadRepository documentUploadRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private LoanAccountRepository loanAccountRepository;
	
	@Autowired
	private SanctionLetterRepository sanctionLetterRepository;
	
	@Autowired
	private EmiServiceImpl emiServiceImpl;
	
	@Autowired
	private EmailSendService emailSendService;

	@Override
	public LoanApplicationResponseDto applyForLoan(LoanApplicationRequestDto requestDto) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmailOrMobileNumber(username, username)
				.orElseThrow(() -> new RuntimeException("Authenticated user not found"));

		// Update user profile
		user.setPanNumber(requestDto.getPanNumber());
		user.setAadhaarNumber(requestDto.getAadhaarNumber());
		user.setMonthlyIncome(requestDto.getMonthlyIncome());
		user.setEmploymentType(requestDto.getEmploymentType());
		user.setCompanyName(requestDto.getCompanyName());
		user.setAddress(requestDto.getAddress());
		user.setState(requestDto.getState());
		user.setCity(requestDto.getCity());
		user.setPincode(requestDto.getPincode());
		user.setIfscCode(requestDto.getIfscCode());
		user.setGender(requestDto.getGender());
		user.setEmail(requestDto.getEmail());  // update email
		userRepository.save(user);

		// EMI calculation ONLY for FOIR check

		// BigDecimal interestRate = BigDecimal.valueOf(12);
		/*
		 * BigDecimal emi = requestDto.getRequestedAmount() .multiply(interestRate)
		 * .divide(BigDecimal.valueOf(100));
		 */

		// calculation emi method call here
		BigDecimal annualInterestRate = BigDecimal.valueOf(12); // 12% yearly

		BigDecimal emi = emiCalculationService.calculateEmi(requestDto.getRequestedAmount(), // Principal
				annualInterestRate, // Annual rate
				requestDto.getTenureMonths() // Tenure in months
		);

		boolean ageEligible = loanEligibilityService.checkAge(user);
		boolean cibilEligible = loanEligibilityService.checkCibilScore(user);
		boolean foirEligible = loanEligibilityService.checkFoir(user, emi);
		boolean hasNoPreviousLoans = loanEligibilityService.canApplyForNewLoan(user);
		boolean notDefaulted = loanEligibilityService.checkDefaultedUser(user);

		boolean finalEligible =
		        ageEligible &&
		        cibilEligible &&
		        foirEligible &&
		        hasNoPreviousLoans &&
		        notDefaulted;

		LoanApplication loanApplication = new LoanApplication();
		loanApplication.setUser(user);
		loanApplication.setLoanType(requestDto.getLoanType());
		loanApplication.setRequestedAmount(requestDto.getRequestedAmount());
		loanApplication.setTenureMonths(requestDto.getTenureMonths());
		loanApplication.setCalculatedEmi(emi);
		loanApplication.setAppliedAt(LocalDateTime.now());
		loanApplication.setLastUpdatedAt(LocalDateTime.now());
		loanApplication.setInterestRate(BigDecimal.valueOf(12));
		
		if (finalEligible) {
			loanApplication.setApplicationStatus(ApplicationStatus.ELIGIBLE);
		} else {
			loanApplication.setApplicationStatus(ApplicationStatus.NOT_ELIGIBLE);
			loanApplication.setRejectionReason("Eligibility criteria not met");
		}

		LoanApplication saved = loanApplicationRepository.save(loanApplication);

		EligibilityCheck eligibility = new EligibilityCheck();
		eligibility.setApplication(saved);
		eligibility.setAgeEligible(ageEligible);
		eligibility.setCibilEligible(cibilEligible);
		eligibility.setFoirEligible(foirEligible);
		eligibility.setIsAlreadyLoanActive(!hasNoPreviousLoans);
		eligibility.setIsDefaulted(!notDefaulted);
		eligibility.setFinalEligible(finalEligible);
		eligibility.setCheckedAt(LocalDateTime.now());
		eligibilityCheckRepository.save(eligibility);

		LoanApplicationResponseDto response = new LoanApplicationResponseDto();
		response.setApplicationId(saved.getId());
		response.setLoanType(saved.getLoanType());
		response.setRequestedAmount(saved.getRequestedAmount());
		response.setTenureMonths(saved.getTenureMonths());
		response.setCalculatedEmi(saved.getCalculatedEmi());
		response.setApplicationStatus(saved.getApplicationStatus());
		response.setAppliedAt(saved.getAppliedAt());
		response.setFinalEligibility(finalEligible);
		response.setApplicantName(user.getFullName());
		return response;
	}

	// -----------------------------
	// GET ALL APPLICATIONS OF USER
	// -----------------------------
	@Override
	public PageResponse<LoanApplicationResponseDto> getByUserId(Integer userId, int page, int size, String sortBy,
			String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<LoanApplication> applicationPage = loanApplicationRepository.findByUserId(userId, pageable);

		List<LoanApplicationResponseDto> content = applicationPage.getContent().stream().map(app -> {

			boolean finalEligible = eligibilityCheckRepository.findByApplicationId(app.getId())
					.map(EligibilityCheck::getFinalEligible).orElse(false);

			LoanApplicationResponseDto dto = modelMapper.map(app, LoanApplicationResponseDto.class);

			dto.setApplicationId(app.getId());
			dto.setFinalEligibility(finalEligible);

			return dto;
		}).toList();

		return new PageResponse<>(content, applicationPage.getNumber(), applicationPage.getSize(),
				applicationPage.getTotalElements(), applicationPage.getTotalPages(), applicationPage.isLast());
	}

	// -----------------------------
	// GET SINGLE APPLICATION DETAILS
	// -----------------------------
	@Override
	public LoanApplicationDetailsResponseDto getDetailsById(Integer applicationId) {

		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));

		boolean finalEligible = eligibilityCheckRepository.findByApplicationId(applicationId)
				.map(EligibilityCheck::getFinalEligible).orElse(false);

		List<DocumentResponseDto> documents = documentUploadRepository.findByApplication_Id(applicationId).stream()
				.map(doc -> modelMapper.map(doc, DocumentResponseDto.class)).toList();

		LoanApplicationDetailsResponseDto response = modelMapper.map(application,
				LoanApplicationDetailsResponseDto.class);

		response.setApplicationId(application.getId());
		response.setFinalEligibility(finalEligible);
		response.setDocuments(documents);
		response.setUser(application.getUser());

		return response;
	}

	@Override
	public PageResponse<LoanApplicationResponseDto> getLoanApplicationsByStatus(String status, int page, int size,
			String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<LoanApplication> applicationPage;

		if ("ALL".equalsIgnoreCase(status)) {
			applicationPage = loanApplicationRepository.findAll(pageable);
		} else {
			ApplicationStatus applicationStatus;
			try {
				applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException("Invalid application status: " + status);
			}

			applicationPage = loanApplicationRepository.findByApplicationStatus(applicationStatus, pageable);
		}

		List<LoanApplicationResponseDto> content = applicationPage.getContent().stream()
		        .map(app -> {
		            LoanApplicationResponseDto dto =
		                    modelMapper.map(app, LoanApplicationResponseDto.class);

		            dto.setApplicantName(app.getUser().getFullName());
		            dto.setUserId(app.getUser().getId());

		            return dto;
		        })
		        .toList();

		return new PageResponse<>(content, applicationPage.getNumber(), applicationPage.getSize(),
				applicationPage.getTotalElements(), applicationPage.getTotalPages(), applicationPage.isLast());
	}

	@Transactional
	public ApiResponse approve(LoanActionRequest request, User approver) {

		Optional<LoanApplication> optionalApp = loanApplicationRepository.findById(request.getApplicationId());
		if (optionalApp.isEmpty()) {
			return new ApiResponse("Loan application not found");
		}

		LoanApplication application = optionalApp.get();

		if (!application.getApplicationStatus().equals(ApplicationStatus.DOCUMENT_APPROVED)) {
			return new ApiResponse("Loan cannot be approved at this stage");
		}

//		Optional<EligibilityCheck> eligibilityOpt = eligibilityCheckRepository
//				.findByApplicationId(request.getApplicationId());

//		if (eligibilityOpt.isEmpty() || !eligibilityOpt.get().getFinalEligible()) {
//			return new ApiResponse("Applicant is not eligible for loan");
//		}

		if (application.getApplicationStatus() == ApplicationStatus.LOAN_APPROVED
				|| application.getApplicationStatus() == ApplicationStatus.LOAN_REJECTED) {
			return new ApiResponse("Loan application already processed");
		}

		// Update status
		application.setApplicationStatus(ApplicationStatus.LOAN_APPROVED);
		application.setLastUpdatedAt(LocalDateTime.now());
		loanApplicationRepository.save(application);


	    // ======================
	    // 2. Create sanction letter
	    // ======================
	    SanctionLetter sanction = new SanctionLetter();
	    sanction.setApplication(application);

	    sanction.setApprovedAmount(application.getRequestedAmount());
	    sanction.setEmiAmount(application.getCalculatedEmi());
	    sanction.setInterestRate(application.getInterestRate()); // or configurable
	    sanction.setTenureMonths(application.getTenureMonths());

	    sanction.setStatus(SanctionStatus.SENT);
	    sanction.setSentAt(LocalDateTime.now());
	    sanction.setRespondedAt(null);

	    // Sanction validity → 7 days
	    sanction.setValidTill(LocalDate.now().plusDays(7));

	    sanctionLetterRepository.save(sanction);

		//send mail for sanction letter
		User user = application.getUser();

		Map<String, String> vars = Map.of("name", user.getFullName(), "applicationId",
				application.getId().toString(), "amount", application.getRequestedAmount().toString(), "tenure",
				application.getTenureMonths().toString(), "emi", application.getCalculatedEmi().toString());

		emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_SANCTION, user.getEmail(), application.getId(),
				user.getId(), vars);
				
		return new ApiResponse("Loan application approved successfully");
	}

	@Transactional
	public ApiResponse reject(LoanActionRequest request, User rejector) {
		Optional<LoanApplication> optionalApp = loanApplicationRepository.findById(request.getApplicationId());
		if (optionalApp.isEmpty()) {
			return new ApiResponse("Loan application not found");
		}

		LoanApplication application = optionalApp.get();

		if (application.getApplicationStatus() == ApplicationStatus.LOAN_APPROVED
				|| application.getApplicationStatus() == ApplicationStatus.LOAN_REJECTED) {
			return new ApiResponse("Loan application already processed");
		}

		application.setApplicationStatus(ApplicationStatus.LOAN_REJECTED);
		application.setRejectionReason(request.getRejectionReason());
		application.setLastUpdatedAt(LocalDateTime.now());
		loanApplicationRepository.save(application);


		User user = application.getUser();
		
		Map<String, String> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("applicationId", application.getId().toString());
        vars.put("reason", request.getRejectionReason());
		
        emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_REJECTED, user.getEmail(), 
        		application.getId(), user.getId(), vars);		

		return new ApiResponse("Loan application rejected successfully");
	}

	@Override
	public ApiResponse submit(Integer applicationId, User currentUser) {

	    Optional<LoanApplication> optionalApp = loanApplicationRepository.findById(applicationId);

	    if (optionalApp.isEmpty()) {
	        return new ApiResponse("Loan application not found");
	    }

	    LoanApplication application = optionalApp.get();

	    // Optional ownership check
	    if (!application.getUser().getId().equals(currentUser.getId())) {
	        return new ApiResponse("You are not allowed to submit this loan application");
	    }

	    if (Boolean.TRUE.equals(application.getLoanApplicationSubmitted())) {
	        return new ApiResponse("Loan application already submitted");
	    }

	    application.setLoanApplicationSubmitted(true);
	    application.setLastUpdatedAt(LocalDateTime.now());

	    loanApplicationRepository.save(application);

        User user = application.getUser();
        
	    emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_APPLICATION_SUBMITTED,user.getEmail(),
	    		application.getId(), user.getId(),
                Map.of( "name", user.getFullName(),"applicationId", application.getId().toString()));
	    
	    return new ApiResponse("Loan application submitted successfully");
	}

	@Override
	public ApiResponse disburseLoan(int applicationId, User currentUser) {

	    // 0. Authorization
	    if (!(currentUser.getRole() == Role.ADMIN)) {
	        return new ApiResponse("You are not authorized to disburse loans");
	    }

	    LoanApplication application = loanApplicationRepository.findById(applicationId)
	            .orElse(null);

	    if (application == null) {
	        return new ApiResponse("Loan application not found");
	    }

	    if (application.getApplicationStatus() != ApplicationStatus.SANCTION_LETTER_ACCEPTED) {
	        return new ApiResponse("Loan is not ready for disbursement");
	    }

	    SanctionLetter letter = sanctionLetterRepository
	            .findByApplicationId(applicationId)
	            .orElse(null);

	    if (letter == null) {
	        return new ApiResponse("Sanction letter not found");
	    }

	    if (letter.getStatus() != SanctionStatus.ACCEPTED) {
	        return new ApiResponse("Sanction letter not accepted yet");
	    }

	    if (loanAccountRepository.existsByApplication_Id(applicationId)) {
	        return new ApiResponse("Loan already disbursed for this application");
	    }

	    LoanAccount loanAccount = createLoanAccount(application, letter);

	    try {
	        emiServiceImpl.generateEmiSchedule(loanAccount.getId());
	    } catch (RuntimeException ex) {
	        return new ApiResponse("Loan disbursement failed: " + ex.getMessage());
	    }

	    application.setApplicationStatus(ApplicationStatus.LOAN_DISBURSED);
	    application.setLastUpdatedAt(LocalDateTime.now());
	    loanApplicationRepository.save(application);

	    //get user from loanApplication
	       User user = application.getUser();
	       
	       // send mail to user that loan has disbursed
	       emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_DISBURSED, user.getEmail(), applicationId, user.getId(),
	    		   Map.of("name",user.getFullName(),
	    				   "applicationId",String.valueOf(applicationId),
	    				   "amount",application.getRequestedAmount().toString()));
			
	    return new ApiResponse("Loan disbursed successfully");
	}

	private LoanAccount createLoanAccount(LoanApplication app, SanctionLetter letter) {

	    LoanAccount loanAccount = new LoanAccount();
	    loanAccount.setApplication(app);
	    loanAccount.setUser(app.getUser());

	    loanAccount.setLoanAmount(letter.getApprovedAmount());
	    loanAccount.setInterestRate(letter.getInterestRate());
	    loanAccount.setTenureMonths(letter.getTenureMonths());
	    loanAccount.setEmiAmount(letter.getEmiAmount());

	    loanAccount.setOutstandingBalance(letter.getApprovedAmount());
	    loanAccount.setLoanAccountStatus(LoanAccountStatus.ACTIVE);
	    loanAccount.setDisbursedDate(LocalDate.now());

	    return loanAccountRepository.save(loanAccount);
	}
	
	@Override
	public LoanApplicationDetailsResponseDto getLatestApplication(User currentUser) {

		Optional<LoanApplication> optionalApplication = loanApplicationRepository
				.findTopByUser_IdOrderByAppliedAtDesc(currentUser.getId());

		// Always return DTO
		if (optionalApplication.isEmpty()) {
			return new LoanApplicationDetailsResponseDto(); // EMPTY DTO
		}

		LoanApplication application = optionalApplication.get();

		boolean finalEligible = eligibilityCheckRepository.findByApplicationId(application.getId())
				.map(EligibilityCheck::getFinalEligible).orElse(false);

		List<DocumentResponseDto> documents = documentUploadRepository.findByApplication_Id(application.getId())
				.stream().map(doc -> modelMapper.map(doc, DocumentResponseDto.class)).toList();

		LoanApplicationDetailsResponseDto response = modelMapper.map(application,
				LoanApplicationDetailsResponseDto.class);

		response.setApplicationId(application.getId());
		response.setFinalEligibility(finalEligible);
		response.setDocuments(documents);
		response.setUser(application.getUser());

		return response;
	}

}
