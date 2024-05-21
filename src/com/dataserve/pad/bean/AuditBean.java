package com.dataserve.pad.bean;

import java.sql.Timestamp;

public class AuditBean {
	private long id;
	private Timestamp transactionDate;
	private int userId;
	private int actionTypeId;
	private int moduleId;
	private String method;
	private String params;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getActionTypeId() {
		return actionTypeId;
	}
	
	public void setActionTypeId(int actionTypeId) {
		this.actionTypeId = actionTypeId;
	}
	
	public int getModuleId() {
		return moduleId;
	}
	
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
}
