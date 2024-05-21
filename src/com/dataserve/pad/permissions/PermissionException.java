package com.dataserve.pad.permissions;

public class PermissionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3474107642807472417L;

	public PermissionException(String message, Throwable t) {
		super(message, t);
	}

	public PermissionException(String message) {
		super(message);
	}
}
