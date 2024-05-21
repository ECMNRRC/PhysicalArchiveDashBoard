package com.dataserve.pad.bean;

import java.util.Set;

public class GroupBean extends AbstractBean {
	private String nameAr;
	private String nameEn;
	private String ldapName;
	private boolean isEnabled;
	private boolean isActive;
	
	private Set<Integer> userIds;
	private Set<Integer> permissionIds;
	
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getLdapName() {
		return ldapName;
	}
	public void setLdapName(String ldapName) {
		this.ldapName = ldapName;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Set<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(Set<Integer> userIds) {
		this.userIds = userIds;
	}
	public Set<Integer> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(Set<Integer> permissionIds) {
		this.permissionIds = permissionIds;
	}
	
}
