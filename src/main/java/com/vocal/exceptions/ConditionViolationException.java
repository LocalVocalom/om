package com.vocal.exceptions;

public class ConditionViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConditionViolationException(String msg) {
		super(msg);
	}
	
	public ConditionViolationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
