package com.loan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.constants.EmailTemplateName;
import com.loan.entity.EmailTemplate;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer>{
	
	Optional<EmailTemplate> findByName(EmailTemplateName name);

}
