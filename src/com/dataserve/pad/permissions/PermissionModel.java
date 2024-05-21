package com.dataserve.pad.permissions;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.dataserve.pad.bean.PermissionBean;
import com.dataserve.pad.db.PermissionDAO;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class PermissionModel {
	private PermissionBean bean;

	public PermissionModel(PermissionBean bean) {
		this.bean = bean;
	}
	
	public Module getModule() {
		return Module.getModuleById(bean.getModuleId());
	}
	
	public ActionType getActionType() {
		return ActionType.getTypeById(bean.getTypeId());
	}
	
	public int getModuleId() {
		return bean.getModuleId();
	}
	
	public void setModuleId(int moduleId) {
		bean.setModuleId(moduleId);
	}
	
	public int getActionTypeId() {
		return bean.getTypeId();
	}
	
	public void setActionTypeId(int typeId) {
		bean.setTypeId(typeId);
	}
	
	public Set<Integer> getGroupIds() {
		return bean.getGroupIds();
	}
	
	public void setGroupIds(Set<Integer> groupIds) {
		bean.setGroupIds(groupIds);
	}
	
	public int getId() {
		return bean.getId();
	}
	
	public void setId(int id) {
		bean.setId(id);
	}
	
	public String getNameAr() {
		return bean.getNameAr();
	}
	
	public void setNameAr(String nameAr) {
		bean.setNameAr(nameAr);
	}
	
	public String getNameEn() {
		return bean.getNameEn();
	}
	
	public void setNameEn(String nameEn) {
		bean.setNameEn(nameEn);
	}
	
	public boolean isEnabled() {
		return bean.isEnabled();
	}
	
	public void setEnabled(boolean enabled) {
		bean.setEnabled(enabled);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}
	
	public String getDescription() {
		return bean.getDescription();
	}
	public int getTypeId() {
		return bean.getTypeId();
	}
	public JSONObject getAsJson() {
		JSONObject obj = new JSONObject();
		obj.put("id", bean.getId());
		obj.put("nameAr", bean.getNameAr());
		obj.put("nameEn", bean.getNameEn());
		obj.put("description", bean.getDescription());
		
		JSONObject module = new JSONObject();
		module.put("id", bean.getModuleId());
		module.put("nameAr", Module.getModuleById(bean.getModuleId()).getNameAr());
		module.put("nameEn", Module.getModuleById(bean.getModuleId()).getNameEn());
		obj.put("module", module);
		
		JSONObject type = new JSONObject();
		type.put("id", bean.getTypeId());
		type.put("nameAr", ActionType.getTypeById(bean.getTypeId()).getNameAr());
		type.put("nameEn", ActionType.getTypeById(bean.getTypeId()).getNameEn());
		obj.put("type", type);
		
		JSONArray groupIdsArr = new JSONArray();
		groupIdsArr.addAll(bean.getGroupIds());
		obj.put("groupIds", groupIdsArr);
		return obj;
	}
	
	public static Set<PermissionModel> getAllPermissions() throws PermissionException {
		Set<PermissionModel> permissions = new LinkedHashSet<PermissionModel>();
		try {
			PermissionDAO dao = new PermissionDAO();
			Set<PermissionBean> beans = dao.fetchAllPermissions();
			for (PermissionBean b : beans) {
				permissions.add(new PermissionModel(b));
			}
		} catch (Exception e) {
			throw new PermissionException("Error getting all permissions", e);
		}
		return permissions;
	}
	
	public static PermissionModel getPermissionById(int id) throws PermissionException {
		try {
			PermissionDAO dao = new PermissionDAO();
			PermissionBean bean = dao.fetchPermission(id);
			return new PermissionModel(bean);
		} catch (Exception e) {
			throw new PermissionException("Error getting permission with id '" + id + "'");
		}
	}

	public void save() throws PermissionException {
		try {
			PermissionDAO dao = new PermissionDAO();
			if (bean.getId() == 0) {
				dao.addPermission(bean);
			} else {
				dao.updatePermission(bean);
			}
		} catch (Exception e) {
			throw new PermissionException("Error saving permission with id '" + bean.getId() + "'");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PermissionModel)) {
			return false;
		}
		PermissionModel pm = (PermissionModel)obj;
		return (pm.getModule().equals(getModule()) && pm.getActionType().equals(getActionType()));
	}
	
	@Override
	public int hashCode() {
		String moduleName = "";
		if (getModule() != null) {
			moduleName = getModule().name();
		}
		
		String actionType = "";
		if (getActionType() != null) {
			actionType = getActionType().name();
		}
	    return Objects.hashCode(getId() + moduleName + actionType);
	}

}
