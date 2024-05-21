package com.dataserve.pad.bean;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DepartmentBean extends AbstractBean  {
	private boolean enabled;
	private String code; 
	private String email;
	private int managerId;
	private int isArchiveCenter;
	private Set<Integer> classificationIds;
	private Set<Integer> usersIds;
	private Set<Integer> storageCenters;
	private List<UserBean> users;
	private Integer parentId;
	private Set<Integer> childrenIds;
	
	public DepartmentBean(int id) {
		this.setId(id); 
	}
	
	public DepartmentBean() {}

	public Set<Integer> getStorageCenters() {
		return storageCenters;
	}

	public void setStorageCenters(Set<Integer> storageCenters) {
		this.storageCenters = storageCenters;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getIsArchiveCenter() {
		return isArchiveCenter;
	}

	public void setIsArchiveCenter(int isArchiveCenter) {
		this.isArchiveCenter = isArchiveCenter;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Integer> getUsersIds() {
		return usersIds;
	}

	public void setUsersIds(Set<Integer> usersIds) {
		this.usersIds = usersIds;
	}

	public Set<Integer> getClassificationIds() {
		return classificationIds;
	}

	public void setClassificationIds(Set<Integer> classificationIds) {
		this.classificationIds = classificationIds;
	}
	public String getDeptEmail() {
		return email;
	}

	public void setDeptEmail(String email) {
		this.email = email;
	}
	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	public List<UserBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserBean> users) {
		this.users = users;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Set<Integer> getChildrenIds() {
		return childrenIds;
	}

	public void setChildrenIds(Set<Integer> childrenIds) {
		this.childrenIds = childrenIds;
	}

	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("deptId", this.getId());
		obj.put("deptArName", this.getNameAr());
		obj.put("deptEnName", this.getNameEn());
		JSONArray array = new JSONArray();
		if(this.getUsers() !=null && this.getUsers().size() > 0) {
			for (UserBean user : this.getUsers()) {
				array.add(user.getAsJson(locale));
			}
		}
		obj.put("users", array);
		return obj;
	}
}