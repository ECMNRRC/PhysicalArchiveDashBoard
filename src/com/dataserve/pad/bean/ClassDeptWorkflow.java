package com.dataserve.pad.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

import com.ibm.json.java.JSONObject;


public class ClassDeptWorkflow implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int modifiedBy;
	private Timestamp modifiedDate;
	private ClassificationBean classifiction;
	private DepartmentBean department;
	private WorkflowType workflowType;
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public ClassificationBean getClassifiction() {
		return this.classifiction;
	}

	public void setClassifiction(ClassificationBean classifiction) {
		this.classifiction = classifiction;
	}

	public DepartmentBean getDepartment() {
		return this.department;
	}

	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}
	
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}
	
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		
		if(this.getClassifiction() != null ) {
			obj.put("classifiction", this.getClassifiction().getAsJson(locale));			
		}
		
		if(this.getDepartment() != null ) {
			obj.put("department", this.getDepartment().getAsJson(locale));
			
		}
		return obj;
	}
}