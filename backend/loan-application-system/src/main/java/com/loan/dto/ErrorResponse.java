package com.loan.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	
	  private int status;
	    private String error;
	    private String message;
	    private String path;
	    private LocalDateTime timestamp;

	    public ErrorResponse(int status, String error, String message, String path) {
	        this.status = status;
	        this.error = error;
	        this.message = message;
	        this.path = path;
	        this.timestamp = LocalDateTime.now();
	    }

}
