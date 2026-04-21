package com.loan.service;

import java.util.Map;

import com.loan.constants.EmailTemplateName;

public interface EmailSendService {
	
	void sendTemplateEmail(EmailTemplateName templateName, String toEmail, Integer applicationId,
	        Integer userId, Map<String, String> variables);

}
