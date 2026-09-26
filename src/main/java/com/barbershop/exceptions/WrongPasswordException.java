package com.barbershop.exceptions;

public class WrongPasswordException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongPasswordException(String message) {
		super(message);
	}

}
