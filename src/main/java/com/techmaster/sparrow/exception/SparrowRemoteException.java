package com.techmaster.sparrow.exception;

public class SparrowRemoteException extends Exception {
	
	
	private static final long serialVersionUID = -7873556399183752402L;
	
	String message;
	
	public SparrowRemoteException(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
