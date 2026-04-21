package com.loan.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
	}
}
