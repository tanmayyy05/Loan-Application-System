package com.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoanApplicationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanApplicationSystemApplication.class, args);
		System.out.println("Loan Application System is running...");
	}

}
