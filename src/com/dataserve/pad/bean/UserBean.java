package com.dataserve.pad.bean;

import java.util.Locale;
import java.util.Set;

import com.ibm.json.java.JSONObject;

public class UserBean extends AbstractBean {
 
	private int id;
	private String UsernameLDAP;
	private String email;
	private boolean IsLogin;
	private boolean IsActive;
	private Integer departmentId;
	
	
	private Set<Integer> GroupsIds;
	private Set<Integer> PermissionsIds;
	private Set<Integer> StorageCenterIds;
	private DepartmentBean department;

	private String userSignature;
	public UserBean(int userId) {
		this.id= userId;
	}
	public UserBean() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsernameLDAP() {
		return UsernameLDAP;
	}
	public void setUsernameLDAP(String usernameLDAP) {
		UsernameLDAP = usernameLDAP;
	}
	public boolean getIsLogin() {
		return IsLogin;
	}
	public void setIsLogin(boolean isLogin) {
		IsLogin = isLogin;
	}
	public boolean getIsActive() {
		return IsActive;
	}
	public void setIsActive(boolean isActive) {
		IsActive = isActive;
	}
	public Set<Integer> getGroupsIds() {
		return GroupsIds;
	}
	public void setGroupsIds(Set<Integer> groupsIds) {
		GroupsIds = groupsIds;
	}
	public Set<Integer> getPermissionsIds() {
		return PermissionsIds;
	}
	public void setPermissionsIds(Set<Integer> permissionsIds) {
		PermissionsIds = permissionsIds;
	}
	public Set<Integer> getStorageCenterIds() {
		return StorageCenterIds;
	}
	public void setStorageCenterIds(Set<Integer> storageCenterIds) {
		StorageCenterIds = storageCenterIds;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
		 
	public DepartmentBean getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}
	public Object getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("userId", this.getId());
		obj.put("userNameLDAP", this.getUsernameLDAP());
		obj.put("userArName", this.getNameAr());
		obj.put("userEnName", this.getNameEn());
		obj.put("departmentId", this.getDepartmentId());
		
		return obj;
	}
	public String getUserSignature() {
		return userSignature;
	}
	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}
	
}
