package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.Locale;

import com.ibm.json.java.JSONObject;

public class WfUserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private int wfUserRolesId;
	private UserBean user;
	private WfRoleType userRoleType;
	private DepartmentBean department;
	
	public WfUserRole() {
	}
	
	public int getWfUserRolesId() {
		return this.wfUserRolesId;
	}

	public void setWfUserRolesId(int userRolesId) {
		this.wfUserRolesId = userRolesId;
	}

	public UserBean getUser() {
		return this.user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public WfRoleType getUserRoleType() {
		return this.userRoleType;
	}

	public void setUserRoleType(WfRoleType userRoleType) {
		this.userRoleType = userRoleType;
	}

	public DepartmentBean getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}
	
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("wfUserRolesId", this.getWfUserRolesId());
		if(this.getUser() !=null) {
			obj.put("user", this.getUser().getAsJson(locale));
		}
		if(this.getDepartment() !=null) {
			obj.put("department", this.getDepartment().getAsJson(locale));
		}
		
		return obj;
	}
}