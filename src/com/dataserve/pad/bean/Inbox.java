package com.dataserve.pad.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONObject;

public class Inbox implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean fActive;
	private boolean fDeleted;
	private Timestamp modifiedDate;
	private int replacementUser;
	private WfRoleType wfRoleType;
	private DepartmentBean department;
	private Committees committee ;
	private UserBean user;
	private UserBean modifiedBy;
	private SyslInboxType syslInboxType;
	private String inboxNameAr;
	private String inboxNameEn;
	

	public Inbox() {
	}

	public Inbox(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getFActive() {
		return this.fActive;
	}

	public void setFActive(boolean fActive) {
		this.fActive = fActive;
	}

	public boolean getFDeleted() {
		return this.fDeleted;
	}

	public void setFDeleted(boolean fDeleted) {
		this.fDeleted = fDeleted;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getReplacementUser() {
		return this.replacementUser;
	}

	public void setReplacementUser(int replacementUser) {
		this.replacementUser = replacementUser;
	}


	public WfRoleType getWfRoleType() {
		return wfRoleType;
	}

	public void setWfRoleType(WfRoleType wfRoleType) {
		this.wfRoleType = wfRoleType;
	}



	public DepartmentBean getDepartment() {
		return this.department;
	}

	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}

	public UserBean getUser() {
		return this.user;
	}

	public void setUser(UserBean user1) {
		this.user = user1;
	}

	public UserBean getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(UserBean user2) {
		this.modifiedBy = user2;
	}

	public SyslInboxType getSyslInboxType() {
		return this.syslInboxType;
	}

	public void setSyslInboxType(SyslInboxType syslInboxType) {
		this.syslInboxType = syslInboxType;
	}

	

	public String getInboxNameAr() {
		return inboxNameAr;
	}

	public void setInboxNameAr(String inboxNameAr) {
		this.inboxNameAr = inboxNameAr;
	}

	public String getInboxNameEn() {
		return inboxNameEn;
	}

	public void setInboxNameEn(String inboxNameEn) {
		this.inboxNameEn = inboxNameEn;
	}

	public Committees getCommittee() {
		return committee;
	}

	public void setCommittee(
			Committees committee) {
		this.committee = committee;
	}

	public JSONObject getAsJson(Locale locale) {
		JSONObject object = new JSONObject();
		object.put("id", this.getId());
		object.put("fActive", this.getFActive());
		if(this.getSyslInboxType() !=null){
			object.put("syslInboxType", this.getSyslInboxType().getAsJson(locale));
		}
		if(this.getDepartment() !=null) {
			object.put("department", this.getDepartment().getAsJson(locale));
		}
		if(this.getWfRoleType() !=null) {
			object.put("wfRoleType", this.getWfRoleType().getAsJson(locale));
		}
		if(this.getCommittee() !=null) {
			object.put("committee", this.getCommittee().getAsJson(locale));
		}
		return object;
	}

	

}