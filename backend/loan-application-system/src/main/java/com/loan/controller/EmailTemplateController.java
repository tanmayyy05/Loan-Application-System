package com.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.dto.ApiResponse;
import com.loan.dto.EmailTemplateRequestDto;
import com.loan.service.EmailTemplateService;

@RestController
@RequestMapping("/api/email-templates")
public class EmailTemplateController {
	@Autowired
	EmailTemplateService emailTemplateService;
	
	 @PostMapping("/createTemplate")
	public ResponseEntity<ApiResponse> createTemplate(@RequestBody EmailTemplateRequestDto emailTemplateRequest){
		emailTemplateService.createTemplate(emailTemplateRequest);
		
		return ResponseEntity.ok(new ApiResponse("Email template saved"));
		
	}

}
