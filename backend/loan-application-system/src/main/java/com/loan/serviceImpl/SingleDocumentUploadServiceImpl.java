package com.loan.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.DocumentStatus;
import com.loan.constants.DocumentType;
import com.loan.constants.EmailTemplateName;
import com.loan.entity.DocumentUpload;
import com.loan.entity.LoanApplication;
import com.loan.entity.User;
import com.loan.repository.DocumentUploadRepository;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.UserRepository;
import com.loan.service.EmailSendService;
import com.loan.service.SingleDocumentUploadService;
import org.springframework.security.access.AccessDeniedException;

@Service
public class SingleDocumentUploadServiceImpl implements SingleDocumentUploadService {

	/*
	 * private static final String UPLOAD_DIR =
	 * "G:/FusionJavaTraining/loanProjectuploads/loan-documents/";
	 */

	@Value("${file.upload.dir}")
	private String uploadDir;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private DocumentUploadRepository documentUploadRepository;
    
    @Autowired
    private DocumentUploadServiceImpl documentUploadServiceImpl;
    
    @Autowired
    EmailSendService emailSendService;

	/*
	 * public void uploadSingleDocumnet(int applicationId, MultipartFile file,
	 * DocumentType documentType) {
	 * 
	 * try { // Get logged-in user (JWT) String username =
	 * SecurityContextHolder.getContext() .getAuthentication() .getName();
	 * 
	 * User user = userRepository .findByEmailOrMobileNumber(username, username)
	 * .orElseThrow(() -> new RuntimeException("User not found"));
	 * 
	 * // Get loan application LoanApplication application =
	 * loanApplicationRepository .findById(applicationId) .orElseThrow(() -> new
	 * RuntimeException("Loan application not found"));
	 * 
	 * // Ownership check (CRITICAL) if
	 * (!application.getUser().getId().equals(user.getId())) { throw new
	 * RuntimeException("Unauthorized access"); }
	 * 
	 * 
	 * // 4️⃣ Prevent duplicate document upload boolean alreadyUploaded =
	 * documentUploadRepository .existsByLoanApplicationIdAndDocumentType(
	 * applicationId, documentType);
	 * 
	 * if (alreadyUploaded) { throw new RuntimeException( documentType +
	 * " already uploaded"); }
	 * 
	 * 
	 * // Create directory if not exists
	 * Files.createDirectories(Paths.get(uploadDir));
	 * 
	 * // Generate safe filename String fileName = applicationId + "_" +
	 * //documentType + "_" + //UUID.randomUUID() + "_" +
	 * file.getOriginalFilename();
	 * 
	 * Path filePath = Paths.get(uploadDir).resolve(fileName);
	 * 
	 * // Save file Files.copy(file.getInputStream(), filePath);
	 * 
	 * // Save document record DocumentUpload document = new DocumentUpload();
	 * document.setApplication(application); document.setDocumentType(documentType);
	 * document.setDocumentUrl(filePath.toString());
	 * document.setDocumentStatus(DocumentStatus.UPLOADED);
	 * document.setUploadedAt(LocalDateTime.now());
	 * 
	 * documentUploadRepository.save(document);
	 * 
	 * // Move application to verification stage application.setApplicationStatus(
	 * ApplicationStatus.DOCUMENT_VERIFICATION_PENDING);
	 * loanApplicationRepository.save(application);
	 * 
	 * } catch (IOException e) { throw new
	 * RuntimeException("Document upload failed", e); } }
	 * 
	 */
	@Override
	public void uploadSingleDocumnet(int loanApplicationId, MultipartFile file, DocumentType documentType) {
		// Get logged-in user
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmailOrMobileNumber(username, username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Get Laon Application
		LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new RuntimeException("Application not found"));

		// Ownership check
		if (!loanApplication.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("Not your application");
		}

		// create directory
		// Files.createDirectories(Paths.get(uploadDir));
		new File(uploadDir).mkdirs();

		// Save new file
		String newFileName = file.getOriginalFilename();

		// String newPath = uploadDir + File.separator + newFileName;
		Path filePath = Paths.get(uploadDir, newFileName);

		try {
			file.transferTo(filePath.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String newPath = filePath.toString();

		Optional<DocumentUpload> optional = documentUploadRepository
				.findByApplicationIdAndDocumentType(loanApplicationId, documentType);

		DocumentUpload documentUpload;

		if (optional.isPresent()) {
			documentUpload = optional.get();

			// delete old file
			File oldFile = new File(documentUpload.getDocumentUrl());
			if (oldFile.exists()) {
				oldFile.delete();
			}
		} else {
			documentUpload = new DocumentUpload();
			documentUpload.setApplication(loanApplication);
			documentUpload.setDocumentType(documentType);
		}
		documentUpload.setDocumentUrl(newPath);
		documentUpload.setDocumentStatus(DocumentStatus.UPLOADED);
		documentUpload.setUploadedAt(LocalDateTime.now());
		documentUploadRepository.save(documentUpload);
            		  
//		updateApplicationStatusAndNotify(loanApplication);
		documentUploadServiceImpl.updateApplicationStatus(loanApplication);
	}
	
	
	/*********************************************************************************************************/
	public void updateApplicationStatusAndNotify(LoanApplication loanApplication) {

	    long uploadedCount = documentUploadRepository .countByApplicationIdAndDocumentStatus(loanApplication.getId(),
	    		DocumentStatus.UPLOADED);

	    int totalRequiredDocs = DocumentType.values().length;

	    // If all documents uploaded
	    if (uploadedCount == totalRequiredDocs) {

	        loanApplication.setApplicationStatus(
	                ApplicationStatus.DOCUMENT_VERIFICATION_PENDING
	        );
	        loanApplicationRepository.save(loanApplication);
	    }
	}
	
/********************************************************************************************************/
	
}
