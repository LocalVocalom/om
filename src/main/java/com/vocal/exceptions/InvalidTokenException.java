package com.vocal.exceptions;

public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidTokenException(String msg) {
		super();
	}
	
	public InvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
