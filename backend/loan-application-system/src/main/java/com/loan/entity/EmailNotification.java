package com.loan.entity;

import java.time.LocalDateTime;

import com.loan.constants.EmailTemplateName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String toEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_name", length = 50, nullable = false)
    private EmailTemplateName templateName;

    private String subject;

    @Column(length = 5000)
    private String body;

    private String status;        // SENT / FAILED
    private String errorMessage;

    private Integer applicationId;
    private Integer userId;

    private LocalDateTime sentAt;
}
