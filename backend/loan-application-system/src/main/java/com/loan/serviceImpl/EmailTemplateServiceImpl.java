package com.loan.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.dto.EmailTemplateRequestDto;
import com.loan.entity.EmailTemplate;
import com.loan.repository.EmailTemplateRepository;
import com.loan.service.EmailTemplateService;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService{
	
	@Autowired
	EmailTemplateRepository emailTemplateRepository;

	@Override
	public void createTemplate(EmailTemplateRequestDto emailTemplateRequest) {
		
		Optional<EmailTemplate> optional = emailTemplateRepository.findByName(emailTemplateRequest.getName());
		if(optional.isPresent()) {
			throw new  RuntimeException("Template already exists");
		}
		
		EmailTemplate emailTemplate  = new EmailTemplate();
		emailTemplate.setName(emailTemplateRequest.getName());
		emailTemplate.setSubject(emailTemplateRequest.getSubject());
		emailTemplate.setBody(emailTemplateRequest.getBody());
		emailTemplate.setIsReminderEmail(emailTemplateRequest.getIsReminderEmail());
		emailTemplateRepository.save(emailTemplate);
		
	}

}
