package com.techmaster.sparrow.exception;

public class SparrowRunTimeException extends RuntimeException {
	
	private static final long serialVersionUID = -3020480503908180626L;
	
	String message = "";
	
	public SparrowRunTimeException(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
