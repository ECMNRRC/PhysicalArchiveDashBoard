package com.dataserve.pad.util;

public class EmailException extends Exception {

	public EmailException(String message, Throwable t) {
		super(message, t);
	}

	public EmailException(String message) {
		super(message);
	}

}
