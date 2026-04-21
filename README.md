# Loan Application and Management System

A full-stack web application designed to automate and manage the complete loan lifecycle, from application submission to approval, disbursement, EMI tracking, and closure.

## Project Overview

The Loan Application System replaces traditional manual loan processing with a secure, automated, and scalable digital platform.

The system supports multiple user roles:

* User (Loan Applicant)
* Loan Officer
* Administrator

It ensures efficiency, transparency, and accuracy in loan processing.

---

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Security (JWT Authentication)
* Spring Data JPA / Hibernate

### Frontend

* Angular

### Database

* MySQL

### Tools

* Maven
* Git and GitHub
* Postman
* Swagger

---

## Key Features

### Authentication and Security

* JWT-based authentication
* Role-based access control
* Password encryption using BCrypt

---

### Loan Application

* Users can apply for different types of loans
* Automated eligibility checks:

  * Age between 21 and 60
  * CIBIL score validation
  * FOIR ≤ 50%
* Prevents multiple active loans

---

### Document Management

* Upload required documents such as ID proof, income proof, and bank statements
* Loan officers can verify, reject, or request re-upload

---

### Loan Approval and Disbursement

* Loan officers review applications
* Approve or reject with justification
* Automatic generation of sanction letter
* Loan disbursement tracking

---

### EMI Calculation and Repayment

EMI is calculated using the standard formula:

EMI = [P × R × (1 + R)^N] / [(1 + R)^N − 1]

Where:

* P = Principal amount
* R = Monthly interest rate
* N = Loan tenure in months

Features include:

* EMI schedule generation
* Monthly repayment tracking
* Partial and full payment support
* Loan foreclosure option

---

### Penalty System

* 2% penalty applied on overdue EMIs
* Automated using scheduled background jobs

---

### Dashboards

User Dashboard:

* Loan status
* EMI details
* Payment history

Loan Officer Dashboard:

* Pending applications
* Document verification

Admin Dashboard:

* System overview
* Loan statistics
* User management

---

## System Architecture

### Layered Architecture

* Controller Layer
* Service Layer
* Repository Layer
* Security Layer

### Three-Tier Architecture

* Presentation Layer (Angular)
* Application Layer (Spring Boot)
* Data Layer (MySQL)

---

## Database Design

Main entities include:

* User
* LoanApplication
* LoanAccount
* EMI_Schedule
* Repayment
* DocumentUpload
* SanctionLetter
* AuditLog

---

## How to Run the Project

### Backend

```bash
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
npm install
ng serve
```

---
## Future Enhancements

* Mobile application support
* AI-based loan risk analysis
* Integration with credit bureau services

---

## Author

Tanmay Wagh

---
