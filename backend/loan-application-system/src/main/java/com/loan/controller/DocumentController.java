package com.loan.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loan.annotation.CurrentUser;
import com.loan.constants.DocumentType;
import com.loan.dto.ApiResponse;
import com.loan.dto.DocumentActionRequestDto;
import com.loan.dto.DocumentResponseDto;
import com.loan.entity.User;
import com.loan.repository.LoanApplicationRepository;
import com.loan.service.DocumentDownloadService;
import com.loan.service.DocumentUploadService;
import com.loan.service.SingleDocumentUploadService;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

	@Autowired
	private DocumentUploadService documentUploadService;

	@Autowired
	SingleDocumentUploadService singleDocumentUploadService;
	
	 @Autowired
	    private LoanApplicationRepository loanApplicationRepository;


	
	  @Autowired
	  DocumentDownloadService documentDownloadService;
	 
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> uploadDocuments(@RequestParam Integer loanApplicationId,
			@RequestParam("files") List<MultipartFile> files,
			@RequestParam("documentTypes") List<DocumentType> documentTypes) {
		documentUploadService.uploadDocuments(loanApplicationId, files, documentTypes);
		return ResponseEntity.ok(new ApiResponse("Documents uploaded successfully"));
	}

	@PostMapping("/uploadSingleDocumnet")
	public ResponseEntity<ApiResponse> uploadSingleDocument(@RequestParam Integer loanApplicationId,
			@RequestParam("file") MultipartFile file, @RequestParam DocumentType documentType) {
		// documentUploadService.uploadSingleDocumnet(loanApplicationId, file,
		// documentType);
		singleDocumentUploadService.uploadSingleDocumnet(loanApplicationId, file, documentType);
		return ResponseEntity.ok(new ApiResponse("Single Document uploaded successfully"));
	}

	// Document Download
	  @GetMapping("/download/{documentId}") 
	  public ResponseEntity<Resource>downloadDocument(@PathVariable Integer documentId) { 
	  Resource resource = documentDownloadService.downloadDocument(documentId); 
	  try {
		System.out.println(resource.getFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	/*
	 * return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION,
	 * "inline; filename=\"" + resource.getFilename() + "\"")
	 * .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	 */
	  return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	  }
	 

	
	@PutMapping("/approve")
	public ResponseEntity<ApiResponse> approveDocument(@CurrentUser User currentUser,@RequestBody DocumentActionRequestDto request) {

		documentUploadService.approveDocument(request.getDocumentId(),currentUser);

		return ResponseEntity.ok(new ApiResponse("Document approved"));
	}

	@PutMapping("/return")
	public ResponseEntity<ApiResponse> returnDocument(@CurrentUser User currentUser,@RequestBody DocumentActionRequestDto request) {

		documentUploadService.returnDocument(request.getDocumentId(), request.getRemarks(),currentUser);

		return ResponseEntity.ok(new ApiResponse("Document returned for correction"));
	}

	@PutMapping("/reject")
	public ResponseEntity<ApiResponse> rejectDocument(@CurrentUser User currentUser,@RequestBody DocumentActionRequestDto request) {

		documentUploadService.rejectDocument(request.getDocumentId(), request.getRemarks(),currentUser);

		return ResponseEntity.ok(new ApiResponse("Document rejected"));
	}

	 @GetMapping("/{loanApplicationId}")
	    public ResponseEntity<List<DocumentResponseDto>> getDocuments(
	            @PathVariable Integer loanApplicationId) {

	        return ResponseEntity.ok(
	        		documentUploadService.getDocumentsByLoanApplicationId(loanApplicationId)
	        );
	    }
}
