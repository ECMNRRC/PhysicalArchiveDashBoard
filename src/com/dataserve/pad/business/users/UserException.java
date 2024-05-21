package com.dataserve.pad.business.users;

public class UserException extends Exception {
	private static final long serialVersionUID = 203741948553466793L;

	public UserException(String message, Throwable t) {
		super(message, t);
	}

	public UserException(String message) {
		super(message);
	}
}
