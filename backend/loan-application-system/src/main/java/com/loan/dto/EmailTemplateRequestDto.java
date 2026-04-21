package com.loan.dto;

import com.loan.constants.EmailTemplateName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateRequestDto {
	
	private EmailTemplateName name;
    private String subject;
    private String body;
    private Boolean isReminderEmail;

}
