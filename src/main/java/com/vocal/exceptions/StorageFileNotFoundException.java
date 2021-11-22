package com.vocal.exceptions;

public class StorageFileNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
		
	public StorageFileNotFoundException(String msg) {
		super(msg);
	}
	
	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
