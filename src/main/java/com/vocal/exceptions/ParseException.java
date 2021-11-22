package com.vocal.exceptions;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseException(String msg) {
		super();
	}
	
	public ParseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
