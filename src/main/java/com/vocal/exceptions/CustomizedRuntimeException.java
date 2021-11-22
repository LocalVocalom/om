package com.vocal.exceptions;

public class CustomizedRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CustomizedRuntimeException(String msg) {
		super(msg);
	}
	
	public CustomizedRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
