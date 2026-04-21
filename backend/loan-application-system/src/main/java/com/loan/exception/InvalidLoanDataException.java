package com.loan.exception;

public class InvalidLoanDataException extends RuntimeException{
	
	public InvalidLoanDataException(String message) {
        super(message);
    }

}
