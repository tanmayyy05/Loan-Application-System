package com.loan.entity;

import com.loan.constants.DocumentStatus;
import com.loan.constants.DocumentType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_upload")
public class DocumentUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "application_id", nullable = false)
	private LoanApplication application;

	@Enumerated(EnumType.STRING)
	@Column(name = "document_type")
	private DocumentType documentType;

	@Column(name = "document_url")
	private String documentUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "document_status")
	private DocumentStatus documentStatus;

	private String remarks;

	@Column(name = "uploaded_at")
	private LocalDateTime uploadedAt;

	@ManyToOne
	@JoinColumn(name = "verified_by")
	private User verifiedBy;

	@Column(name = "verified_at")
	private LocalDateTime verifiedAt;

	public DocumentUpload() {
	}

	public DocumentUpload(Integer id, LoanApplication application, DocumentType documentType, String documentUrl,
			DocumentStatus documentStatus, String remarks, LocalDateTime uploadedAt, User verifiedBy,
			LocalDateTime verifiedAt) {
		super();
		this.id = id;
		this.application = application;
		this.documentType = documentType;
		this.documentUrl = documentUrl;
		this.documentStatus = documentStatus;
		this.remarks = remarks;
		this.uploadedAt = uploadedAt;
		this.verifiedBy = verifiedBy;
		this.verifiedAt = verifiedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoanApplication getApplication() {
		return application;
	}

	public void setApplication(LoanApplication application) {
		this.application = application;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public User getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(User verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public LocalDateTime getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}
}
