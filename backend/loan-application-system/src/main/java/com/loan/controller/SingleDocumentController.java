/*
 * package com.loan.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import com.loan.constants.DocumentType; import
 * com.loan.service.SingleDocumentUploadService;
 * 
 * @RestController public class SingleDocumentController {
 * 
 * 
 * @Autowired SingleDocumentUploadService singleDocumentUploadService;
 * 
 * @PostMapping("/uploadSingleDocumnet") public ResponseEntity<String>
 * uploadSingleDocument(
 * 
 * @RequestParam int applicationId,
 * 
 * @RequestParam DocumentType documentType,
 * 
 * @RequestParam MultipartFile file) {
 * 
 * singleDocumentUploadService.uploadSingleDocumnet(applicationId, documentType,
 * file);
 * 
 * return ResponseEntity.ok("Document uploaded successfully"); }
 * 
 * 
 * 
 * }
 */