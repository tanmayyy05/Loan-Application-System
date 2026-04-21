package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.EmailTemplateName;
import com.loan.constants.Role;
import com.loan.constants.SanctionStatus;
import com.loan.dto.SanctionLetterResponseDto;
import com.loan.entity.LoanApplication;
import com.loan.entity.SanctionLetter;
import com.loan.entity.User;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.SanctionLetterRepository;
import com.loan.repository.UserRepository;
import com.loan.service.EmailSendService;
import com.loan.service.SanctionLetterService;

@Service
public class SanctionLetterServiceImpl implements SanctionLetterService {
	
	
	@Autowired
	private SanctionLetterRepository sanctionLetterRepository;

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private ModelMapper modelMapper;
	
    @Autowired 
     EmailSendService emailSendService;
    
    @Autowired
    UserRepository userRepository;


	@Override
	public SanctionLetterResponseDto getByLoanApplicationId(Integer loanApplicationId) {

		loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new RuntimeException("Loan Application not found with id: " + loanApplicationId));

		SanctionLetter sanctionLetter = sanctionLetterRepository.findByApplicationId(loanApplicationId).orElseThrow(
				() -> new RuntimeException("Sanction Letter not found for application id: " + loanApplicationId));

		return modelMapper.map(sanctionLetter, SanctionLetterResponseDto.class);
	}

	@Override
	@Transactional
	public void acceptSanctionLetter(Integer sanctionLetterId) {

		SanctionLetter letter = sanctionLetterRepository.findById(sanctionLetterId)
				.orElseThrow(() -> new RuntimeException("Sanction Letter not found with id: " + sanctionLetterId));

		if (letter.getStatus() != SanctionStatus.SENT) {
			throw new IllegalStateException("Sanction letter already responded");
		}

		// Update sanction letter
		letter.setStatus(SanctionStatus.ACCEPTED);
		letter.setRespondedAt(LocalDateTime.now());

		// Update loan application status
		LoanApplication application = loanApplicationRepository.findById(letter.getApplication().getId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));

		application.setApplicationStatus(ApplicationStatus.SANCTION_LETTER_ACCEPTED);
		application.setLastUpdatedAt(LocalDateTime.now());

		sanctionLetterRepository.save(letter);
		loanApplicationRepository.save(application);
		
		// user accept the sanction letter send mail to admin
		
		User user = application.getUser();
		User admin=userRepository.findByRole(Role.ADMIN)
			       .orElseThrow(() -> new RuntimeException ("Admin not found"));
		
		Map<String, String>vars=Map.of(
                "userName", user.getFullName(),
                "userEmail", user.getEmail(),
                "applicationId", application.getId().toString(),
                "sanctionLetterId", letter.getId().toString(),
                "acceptedAt", letter.getRespondedAt().toString()
        );
		
		emailSendService.sendTemplateEmail(EmailTemplateName.SANCTION_LETTER_ACCEPTED, admin.getEmail(), application.getId(), 
				user.getId(), vars);
		
	}

	@Override
	@Transactional
	public void rejectSanctionLetter(Integer sanctionLetterId) {

		SanctionLetter letter = sanctionLetterRepository.findById(sanctionLetterId)
				.orElseThrow(() -> new RuntimeException("Sanction Letter not found with id: " + sanctionLetterId));

		if (letter.getStatus() != SanctionStatus.SENT) {
			throw new IllegalStateException("Sanction letter already responded");
		}

		// Update sanction letter
		letter.setStatus(SanctionStatus.REJECTED);
		letter.setRespondedAt(LocalDateTime.now());

		// Update loan application status
		LoanApplication application = loanApplicationRepository.findById(letter.getApplication().getId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));

		application.setApplicationStatus(ApplicationStatus.SANCTION_LETTER_REJECTED);
		application.setLastUpdatedAt(LocalDateTime.now());

		sanctionLetterRepository.save(letter);
		loanApplicationRepository.save(application);
		
		//user reject sanction letter send email to admin
		
		User user = application.getUser();
		User admin=userRepository.findByRole(Role.ADMIN)
			       .orElseThrow(() -> new RuntimeException ("Admin not found"));
		
		Map<String, String>vars=Map.of(
                "userName", user.getFullName(),
                "userEmail", user.getEmail(),
                "applicationId", application.getId().toString(),
                "sanctionLetterId", letter.getId().toString(),
                "rejectedAt", letter.getRespondedAt().toString()
        );
		
		emailSendService.sendTemplateEmail(EmailTemplateName.SANCTION_LETTER_REJECTED, admin.getEmail(), application.getId(), 
				user.getId(), vars);
	}
 
     // loan approved	
	@Override
	public void sanctionLoan(Integer applicationId) {
		LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
	                .orElseThrow(() -> new RuntimeException("Application not found"));
		
		loanApplication.setApplicationStatus(ApplicationStatus.LOAN_APPROVED);
		loanApplicationRepository.save(loanApplication);
		
		User user = loanApplication.getUser();
		
		
		  Map<String, String> vars = Map.of( "name", user.getFullName(),
		  "applicationId", loanApplication.getId().toString(),
		  "amount",loanApplication.getRequestedAmount().toString(), 
		  "tenure",loanApplication.getTenureMonths().toString(), 
		  "emi",loanApplication.getCalculatedEmi().toString() );
		  
		emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_SANCTION, user.getEmail(), 
				applicationId, user.getId(), vars);
	}

	@Override
	public void rejectLoan(Integer applicationId, String rejectionReason) {
		LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan application not found"));
		// update status
		loanApplication.setApplicationStatus(ApplicationStatus.LOAN_REJECTED);
		loanApplication.setRejectionReason(rejectionReason);
		
		User user = loanApplication.getUser();
		
		Map<String, String> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("applicationId", loanApplication.getId().toString());
        vars.put("reason", rejectionReason);
		
        emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_REJECTED, user.getEmail(), 
				applicationId, user.getId(), vars);		

	}

}
