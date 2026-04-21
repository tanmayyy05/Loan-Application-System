package com.loan.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loan.constants.ApplicationStatus;
import com.loan.constants.DocumentStatus;
import com.loan.constants.DocumentType;
import com.loan.constants.EmailTemplateName;
import com.loan.dto.DocumentResponseDto;
import com.loan.entity.DocumentUpload;
import com.loan.entity.LoanApplication;
import com.loan.entity.User;
import com.loan.repository.DocumentUploadRepository;
import com.loan.repository.LoanApplicationRepository;
import com.loan.repository.UserRepository;
import com.loan.service.DocumentUploadService;
import com.loan.service.EmailSendService;

@Service
@Transactional
public class DocumentUploadServiceImpl implements DocumentUploadService {


	/*
	 * private static final String UPLOAD_DIR =
	 * "G:/FusionJavaTraining/loanProjectuploads/loan-documents/";
	 */

	// private static final String UPLOAD_DIR =
	// "G:\\FusionJavaTraining\\loanProjectuploads/loan-documents/";
	
	@Value("${file.upload.dir}")
	private String uploadDir;

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private DocumentUploadRepository documentUploadRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	EmailSendService emailSendService;

	@Override
	public void uploadDocuments(Integer loanApplicationId, List<MultipartFile> files,
			List<DocumentType> documentTypes) {

		// Get logged-in user from JWT
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmailOrMobileNumber(username, username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// find loan application by loanApplicationId
		LoanApplication application = loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new RuntimeException("Loan application not found"));

		if (!application.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized upload attempt");
		}

		// validate size
		if (files.size() != documentTypes.size()) {
			throw new RuntimeException("Files and DocumentTypes count must match");
		}

		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// save documents
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			DocumentType documentType = documentTypes.get(i);
			String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

			String filePath = uploadDir + uniqueFileName;

			try {
				file.transferTo(new File(filePath));
			} catch (IOException e) {
				throw new RuntimeException("File upload failed");
			}

			DocumentUpload document = new DocumentUpload();
			document.setApplication(application);
			document.setDocumentType(documentType);
			document.setDocumentUrl(filePath);
			document.setDocumentStatus(DocumentStatus.UPLOADED);
			document.setUploadedAt(LocalDateTime.now());

	        documentUploadRepository.save(document);
		}
		
		updateApplicationStatus(application);
	}
	
	
	/****************************Single Documents Upload*************************************************************/

	@Override
	public void uploadSingleDocumnet(int loanApplicationId, MultipartFile file, DocumentType documentType) {
		// TODO Auto-generated method stub
		
		try {
            //  Get logged-in user (JWT)
            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            User user = userRepository
                    .findByEmailOrMobileNumber(username, username)
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            //  Get loan application
            LoanApplication application = loanApplicationRepository
                    .findById(loanApplicationId)
                    .orElseThrow(() ->
                            new RuntimeException("Loan application not found"));

            //  Ownership check (CRITICAL)
            if (!application.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Unauthorized access");
            }

			/*
			 * // 4️⃣ Prevent duplicate document upload boolean alreadyUploaded =
			 * documentUploadRepository .existsByLoanApplicationIdAndDocumentType(
			 * applicationId, documentType);
			 * 
			 * if (alreadyUploaded) { throw new RuntimeException( documentType +
			 * " already uploaded"); }
			 */

            // Create directory if not exists
            Files.createDirectories(Paths.get(uploadDir));

            // Generate safe filename
			/*
			 * String fileName = loanApplicationId + "_" + documentType + "_" +
			 * UUID.randomUUID() + "_" + file.getOriginalFilename();
			 */
            
            String fileName = loanApplicationId + "_" +
                    //documentType + "_" +
                    //UUID.randomUUID() + "_" +
                    file.getOriginalFilename();


            Path filePath = Paths.get(uploadDir).resolve(fileName);

            //  Save file
            Files.copy(file.getInputStream(), filePath);

            //  Save document record
            DocumentUpload document = new DocumentUpload();
            document.setApplication(application);
            document.setDocumentType(documentType);
            document.setDocumentUrl(filePath.toString());
            document.setDocumentStatus(DocumentStatus.UPLOADED);
            document.setUploadedAt(LocalDateTime.now());

            documentUploadRepository.save(document);

            updateApplicationStatus(application);

        } catch (IOException e) {
            throw new RuntimeException("Document upload failed", e);
        }
	
		
	}
	
		
	@Override
	public void approveDocument(Integer documentId, User currentUser) {

		DocumentUpload document = documentUploadRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		System.out.println("CurrentUser ID: " + currentUser.getId());

		document.setDocumentStatus(DocumentStatus.VERIFIED);
		document.setVerifiedAt(LocalDateTime.now());
		document.setVerifiedBy(currentUser);
		document.setRemarks(null);
		
		documentUploadRepository.save(document);
		
		LoanApplication application = document.getApplication();
		updateApplicationStatus(application);
	}

	@Override
	public void rejectDocument(Integer documentId, String remarks, User currentUser) {

		DocumentUpload document = documentUploadRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		document.setDocumentStatus(DocumentStatus.REJECTED);
		document.setRemarks(remarks);
		document.setVerifiedAt(LocalDateTime.now());
		document.setVerifiedBy(currentUser);
		
		documentUploadRepository.save(document);
		
		LoanApplication application = document.getApplication();
		updateApplicationStatus(application);
	}

	@Override
	public void returnDocument(Integer documentId, String remarks, User currentUser) {

		DocumentUpload document = documentUploadRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		document.setDocumentStatus(DocumentStatus.RETURNED_FOR_CORRECTION);
		document.setRemarks(remarks);
		document.setVerifiedAt(LocalDateTime.now());
		document.setVerifiedBy(currentUser);
		
		documentUploadRepository.save(document);
		
		LoanApplication application = document.getApplication();
		updateApplicationStatus(application);
		
		
		//loanOffecer send mail to reupload the documents
		User user = application.getUser();
		
		Map<String, String>vars =Map.of(
                "userName", user.getFullName(),
                "applicationId", application.getId().toString(),
                "documentType", document.getDocumentType().name(),
                "remarks", document.getRemarks()
        );
		
        emailSendService.sendTemplateEmail(
                EmailTemplateName.DOCUMENT_RETURNED,
                user.getEmail(),
                application.getId(),
                user.getId(),
                vars);
		
	}

	 @Override
	    public List<DocumentResponseDto> getDocumentsByLoanApplicationId(Integer loanApplicationId) {

	        // Validate loan application
	        loanApplicationRepository.findById(loanApplicationId)
	                .orElseThrow(() -> new RuntimeException(
	                        "Loan Application not found with id: " + loanApplicationId));

	        return documentUploadRepository.findByApplication_Id(loanApplicationId)
	                .stream()
	                .map(doc -> modelMapper.map(doc, DocumentResponseDto.class))
	                .toList();
	    }
	 
	 public void updateApplicationStatus(LoanApplication application) {

		    List<DocumentUpload> documents =
		            documentUploadRepository.findByApplication_Id(application.getId());

		    if (documents.isEmpty()) {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_UPLOAD_PENDING);
		        loanApplicationRepository.save(application);
		        return;
		    }

		    Set<DocumentType> requiredTypes = EnumSet.allOf(DocumentType.class);

		    Set<DocumentType> uploadedTypes = documents.stream()
		            .map(DocumentUpload::getDocumentType)
		            .collect(Collectors.toSet());

		    boolean hasRejected = documents.stream()
		            .anyMatch(d -> d.getDocumentStatus() == DocumentStatus.REJECTED);

		    boolean hasReturned = documents.stream()
		            .anyMatch(d -> d.getDocumentStatus() == DocumentStatus.RETURNED_FOR_CORRECTION);

		    Set<DocumentType> verifiedTypes = documents.stream()
		            .filter(d -> d.getDocumentStatus() == DocumentStatus.VERIFIED)
		            .map(DocumentUpload::getDocumentType)
		            .collect(Collectors.toSet());

		    boolean allUploaded = uploadedTypes.containsAll(requiredTypes);
		    boolean allVerified = verifiedTypes.containsAll(requiredTypes);

		    //Priority-based decision
		    if (hasRejected) {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_REJECTED);
		    }
		    else if (hasReturned) {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_RETURNED_FOR_CORRECTION);
		    }
		    else if (!allUploaded) {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_UPLOAD_PENDING);
		    }
		    else if (allVerified) {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_APPROVED);
		    }
		    else {
		        application.setApplicationStatus(ApplicationStatus.DOCUMENT_VERIFICATION_PENDING);
		    }
		    loanApplicationRepository.save(application);
		}

	 }
