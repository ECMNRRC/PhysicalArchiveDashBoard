package com.dataserve.pad.bean;

import java.util.LinkedHashSet;
import java.util.Set;

public class StorageCenterBean extends AbstractBean {
	private boolean isActive;
	private int type;
	private int departmentId;
	private Set<Integer> inventories;
	
	public StorageCenterBean() {
		inventories = new LinkedHashSet<Integer>();
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Set<Integer> getInventories() {
		return inventories;
	}
	public void setInventories(Set<Integer> inventories) {
		this.inventories = inventories;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
}
