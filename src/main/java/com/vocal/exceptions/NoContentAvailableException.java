package com.vocal.exceptions;

public class NoContentAvailableException  extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoContentAvailableException(String msg) {
		super(msg);
	}
	
	public NoContentAvailableException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
