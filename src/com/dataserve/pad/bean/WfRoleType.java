package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;



public class WfRoleType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int wfRoleTypeId;
	private String roleTypeAr;
	private String roleTypeEn;
	private List<WfUserRole> userRoles;
	private boolean isSystemData ;

	public WfRoleType() {
	}


	
	public WfRoleType(int wfRoleTypeId) {
		super();
		this.wfRoleTypeId = wfRoleTypeId;
	}



	public int getWfRoleTypeId() {
		return this.wfRoleTypeId;
	}

	public void setWfRoleTypeId(int wfRoleTypeId) {
		this.wfRoleTypeId = wfRoleTypeId;
	}


	public String getRoleTypeAr() {
		return this.roleTypeAr;
	}

	public void setRoleTypeAr(String roleTypeAr) {
		this.roleTypeAr = roleTypeAr;
	}


	public String getRoleTypeEn() {
		return this.roleTypeEn;
	}

	public void setRoleTypeEn(String roleTypeEn) {
		this.roleTypeEn = roleTypeEn;
	}



	public List<WfUserRole> getUserRoles() {
		return userRoles;
	}



	public void setUserRoles(List<WfUserRole> userRoles) {
		this.userRoles = userRoles;
	}



	public boolean isSystemData() {
		return isSystemData;
	}



	public void setSystemData(boolean isSystemData) {
		this.isSystemData = isSystemData;
	}
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("wfRoleTypeId", this.getWfRoleTypeId());
		obj.put("roleTypeAr", this.getRoleTypeAr());
		obj.put("roleTypeEn", this.getRoleTypeEn());
		obj.put("isSystemData", this.isSystemData());
		JSONArray array = new JSONArray();
		if(this.getUserRoles() !=null && this.getUserRoles().size() > 0) {
			
			for (WfUserRole wfUserRole : userRoles) {
				array.add(wfUserRole.getAsJson(locale));
			}
			
		}
		obj.put("userRoles", array);
		return obj;
	}

}