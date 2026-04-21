
  package com.loan.serviceImpl;
  
  import java.net.MalformedURLException;
import java.nio.file.Path; 
import java.nio.file.Paths;
import java.util.Optional;
  
  import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource; 
  import org.springframework.core.io.UrlResource;  
  import org.springframework.stereotype.Service;
  
  import com.loan.entity.DocumentUpload; 
  import com.loan.repository.DocumentUploadRepository; 
  import com.loan.service.DocumentDownloadService;
  
  @Service 
  public class DocumentDownloadServiceImpl implements DocumentDownloadService{
  
  @Autowired DocumentUploadRepository documentUploadRepository;
  
  @Value("${file.upload.dir}")
  private String uploadDir;
  
  @Override
  public Resource downloadDocument(Integer documentId) {
		DocumentUpload document = documentUploadRepository.findById(documentId).
				  orElseThrow(()->new RuntimeException("Document not found"));
		try {
            //Path filePath = Paths.get(document.getDocumentUrl());
			 Path filePath = Paths.get(uploadDir) .resolve(document.getDocumentUrl()) .normalize();
			 
			 System.out.println("Downloading file from: " + filePath.toAbsolutePath());

            Resource resource = new UrlResource(filePath.toUri());

           // if (!resource.exists() || !resource.isReadable()) 
            	if (!resource.exists()){
            	throw new RuntimeException("File not found: " + filePath);
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path", e);
        }
  }
  
}
  
  
 