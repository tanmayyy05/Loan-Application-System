package com.loan.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String action;

	@ManyToOne
	@JoinColumn(name = "performed_by")
	private User performedBy;

	@Column(name = "performed_at")
	private LocalDateTime performedAt;

	public AuditLog() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public User getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(User performedBy) {
		this.performedBy = performedBy;
	}

	public LocalDateTime getPerformedAt() {
		return performedAt;
	}

	public void setPerformedAt(LocalDateTime performedAt) {
		this.performedAt = performedAt;
	}
}
