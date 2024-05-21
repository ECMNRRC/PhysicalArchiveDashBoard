package com.dataserve.pad.business.audit;

public class AuditException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042889938322225085L;

	public AuditException(String arg0) {
		super(arg0);
	}

	public AuditException(Throwable arg0) {
		super(arg0);
	}

	public AuditException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
