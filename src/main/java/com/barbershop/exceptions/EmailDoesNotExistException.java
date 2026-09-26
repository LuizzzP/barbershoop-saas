package com.barbershop.exceptions;

public class EmailDoesNotExistException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailDoesNotExistException(String message) {
		super(message);
	}
}
