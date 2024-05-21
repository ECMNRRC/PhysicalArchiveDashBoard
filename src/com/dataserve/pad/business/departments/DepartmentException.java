package com.dataserve.pad.business.departments;

public class DepartmentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3474107642807472417L;

	public DepartmentException(String message, Throwable t) {
		super(message, t);
	}

	public DepartmentException(String message) {
		super(message);
	}
}
