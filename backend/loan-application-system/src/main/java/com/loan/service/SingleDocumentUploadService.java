package com.loan.service;

import org.springframework.web.multipart.MultipartFile;

import com.loan.constants.DocumentType;
import com.loan.entity.LoanApplication;

public interface SingleDocumentUploadService {
	
	//public void uploadSingleDocumnet(int applicationId, DocumentType documentType, MultipartFile file);
	public void uploadSingleDocumnet(int loanApplicationId,  MultipartFile file, DocumentType documentType);
	
	public void updateApplicationStatusAndNotify(LoanApplication loanApplication);
	
	//public void sendDocumentReminderIfPending(LoanApplication loanApplication);

	
}
