package com.dataserve.pad.bean;

import java.util.Set;

public class GroupsBean extends AbstractBean {
	
	private String LDAPGroupName;
	private int  Enabled ;
	private int IsActive;
	private Set<Integer> UsersIds;
	private Set<Integer> Permissions;
	public String getLDAPGroupName() {
		return LDAPGroupName;
	}
	public void setLDAPGroupName(String lDAPGroupName) {
		LDAPGroupName = lDAPGroupName;
	}
	public int getEnabled() {
		return Enabled;
	}
	public void setEnabled(int enabled) {
		Enabled = enabled;
	}
	public int getIsActive() {
		return IsActive;
	}
	public void setIsActive(int isActive) {
		IsActive = isActive;
	}
	public Set<Integer> getUsersIds() {
		return UsersIds;
	}
	public void setUsersIds(Set<Integer> usersIds) {
		UsersIds = usersIds;
	}
	public Set<Integer> getPermissions() {
		return Permissions;
	}
	public void setPermissions(Set<Integer> permissions) {
		Permissions = permissions;
	}
	
	
}
