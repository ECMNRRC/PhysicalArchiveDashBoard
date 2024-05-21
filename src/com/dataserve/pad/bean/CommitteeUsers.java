package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.Locale;

import com.ibm.json.java.JSONObject;

public class CommitteeUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private UserBean user;
	private Committees committee;
	private DepartmentBean department;
	
	public CommitteeUsers() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Committees getCommittee() {
		return committee;
	}

	public void setCommittee(Committees committee) {
		this.committee = committee;
	}

	public DepartmentBean getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}
	
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		if(this.getUser() !=null) {
			obj.put("user", this.getUser().getAsJson(locale));
		}
		if(this.getDepartment() !=null) {
			obj.put("department", this.getDepartment().getAsJson(locale));
		}
		
		return obj;
	}

}