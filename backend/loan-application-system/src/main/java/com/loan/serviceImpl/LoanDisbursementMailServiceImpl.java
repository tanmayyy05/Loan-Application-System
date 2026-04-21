package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.EmailTemplateName;
import com.loan.entity.LoanApplication;
import com.loan.entity.User;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.UserRepository;
import com.loan.service.EmailSendService;
import com.loan.service.LoanDisbursementMailService;

public class LoanDisbursementMailServiceImpl implements LoanDisbursementMailService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LoanApplicationRepository loanApplicationRepository;
	
	@Autowired 
	EmailSendService emailSendService;

	@Override
	public void disburseLoan(Integer applicationId) {
		
		 //  Get logged-in admin
        String username = SecurityContextHolder .getContext().getAuthentication() .getName();
        
        
        // get the user
       User admin =  userRepository.findByEmailOrMobileNumber(username, username)
    		   .orElseThrow(() -> new RuntimeException("Admin not found"));
       
       if(!admin.getRole().name().equals("ADMIN")) {
    	   throw new AccessDeniedException("Only admin can disburse loan");
       }
       
       // get loan by applicationId
       LoanApplication loanApplication =  loanApplicationRepository.findById(applicationId)
    		   .orElseThrow(()->new RuntimeException("Loan application not found"));
       
       // check application status
       if (loanApplication.getApplicationStatus() != ApplicationStatus.LOAN_APPROVED) {
           throw new RuntimeException("Loan must be approved before disbursement");
       }
       
       //  Update status of loan application
       loanApplication.setApplicationStatus(ApplicationStatus.LOAN_DISBURSED);
       loanApplication.setLastUpdatedAt(LocalDateTime.now());
       loanApplicationRepository.save(loanApplication);

     //get user from loanApplication
       User user = loanApplication.getUser();
       
       // send mail to user that loan has disbursed
       emailSendService.sendTemplateEmail(EmailTemplateName.LOAN_DISBURSED, user.getEmail(), applicationId, user.getId(), 
    		   Map.of(
                       "name", user.getFullName(),
                       "applicationId", applicationId.toString(),
                       "amount", loanApplication.getRequestedAmount().toString()
               ));
		
	}

}
