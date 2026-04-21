package com.loan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.constants.DocumentStatus;
import com.loan.constants.DocumentType;
import com.loan.entity.DocumentUpload;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Integer> {

	List<DocumentUpload> findByApplication_Id(Integer applicationId);
	  
	boolean existsByApplication_IdAndDocumentStatusNot(Integer applicationId, DocumentStatus status);
	
	Optional<DocumentUpload>findByApplicationIdAndDocumentType(Integer applicationId, DocumentType documentType);
	
	long countByApplicationIdAndDocumentStatus(Integer applicationId, DocumentStatus status);
	
	


}
