package com.loan.entity;

import com.loan.constants.EmailTemplateName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "email_template", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class EmailTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EmailTemplateName name;

	private String subject;

	@Column(length = 5000)
	private String body;

	@Column(name = "is_reminder_email")
	private Boolean isReminderEmail;

}
