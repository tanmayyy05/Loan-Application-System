package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.EmailNotification;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Integer>{

}
