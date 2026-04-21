package com.loan.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.loan.constants.EmailTemplateName;
import com.loan.entity.EmailNotification;
import com.loan.entity.EmailTemplate;
import com.loan.repository.EmailNotificationRepository;
import com.loan.repository.EmailTemplateRepository;
import com.loan.service.EmailSendService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendServiceImpl implements  EmailSendService{
	
	@Value("${spring.mail.username}")
	private String mailUsername;

   @Autowired
   JavaMailSender javaMailSender;

	@Autowired
	EmailTemplateRepository emailTemplateRepository;
	
	@Autowired
	EmailNotificationRepository emailNotificationRepository;
	
	@Override
	public void sendTemplateEmail(EmailTemplateName emailTemplateName, String toEmail, Integer applicationId, Integer userId,
			Map<String, String> variables) {
		
		 EmailTemplate template = emailTemplateRepository.findByName(emailTemplateName)
	           .orElseThrow(() -> new RuntimeException("Email template not found: " + emailTemplateName));
		 
		 String subject = render(template.getSubject(), variables);
	        String body = render(template.getBody(), variables);

	        EmailNotification emailNotification = new EmailNotification();
	        emailNotification.setToEmail(toEmail);
	        emailNotification.setTemplateName(emailTemplateName);
	        emailNotification.setSubject(subject);
	        emailNotification.setBody(body);
	        emailNotification.setApplicationId(applicationId);
	        emailNotification.setUserId(userId);
	        emailNotification.setSentAt(LocalDateTime.now());
	        
	        try {
	        	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper =
	                    new MimeMessageHelper(mimeMessage, false, "UTF-8");

	            helper.setFrom(mailUsername);      // ✅ PROFESSIONAL
	            helper.setTo(toEmail);
	            helper.setSubject(subject);
	            helper.setText(body, true);   // false = plain text

	            javaMailSender.send(mimeMessage);
	            emailNotification.setStatus("SENT");

	        } catch (Exception e) {
	        	emailNotification.setStatus("FAILED");
	        	emailNotification.setErrorMessage(e.getMessage());
	        }
	        
	        emailNotificationRepository.save(emailNotification);
	}

	private String render(String text, Map<String, String> variables) {
	
		String result = text;
        for (var entry : variables.entrySet()) {
            result = result.replace(
                "{{" + entry.getKey() + "}}",
                entry.getValue()
            );
        }
        return result;
	}
	
	

}
