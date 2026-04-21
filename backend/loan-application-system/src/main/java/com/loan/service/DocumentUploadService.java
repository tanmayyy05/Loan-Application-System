package com.loan.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.loan.constants.DocumentType;
import com.loan.dto.DocumentResponseDto;
import com.loan.entity.User;

public interface DocumentUploadService {
	
    void uploadDocuments(Integer loanApplicationId, List<MultipartFile> files, List<DocumentType> documentTypes);
	
	public void uploadSingleDocumnet(int loanApplicationId,  MultipartFile file, DocumentType documentType);

	void approveDocument(Integer documentId, User currentUser);

	void rejectDocument(Integer documentId, String remarks, User currentUser);

	void returnDocument(Integer documentId, String remarks, User currentUser);

    List<DocumentResponseDto> getDocumentsByLoanApplicationId(Integer loanApplicationId);

}
