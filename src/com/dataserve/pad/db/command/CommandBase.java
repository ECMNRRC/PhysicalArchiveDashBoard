package com.dataserve.pad.db.command;

import java.sql.Timestamp;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.AuditBean;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.PermissionModel;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.business.users.UserException;
import com.dataserve.pad.business.users.UserModel;
import com.dataserve.pad.util.ConfigManager;
import com.ibm.ecm.extension.PluginServiceCallbacks;

public abstract class CommandBase implements Command {
	protected HttpServletRequest request;
	protected PluginServiceCallbacks callBacks;
	protected String currentUserId;
	
	
	public CommandBase(HttpServletRequest request) {
		this.request = request;
		this.callBacks = (PluginServiceCallbacks)request.getAttribute("callBacks");
		this.currentUserId = (String)request.getAttribute("curretUserId");
	}
	
	@Override
	public abstract String execute() throws Exception;
	
	protected abstract Module getModule(); 
	protected abstract ActionType getActionType();
	
	public boolean checkAccess() throws Exception {
		if (ConfigManager.isAccessCheckDisabled()) {
			return true;
		}
		
		if (currentUserId.equalsIgnoreCase(ConfigManager.getSuperUserName())) {
			return true;
		}
		
		if (getModule() == null || getActionType() == null) {
			return true;
		}
		
		UserModel currentUser;
		try {
			currentUser = UserModel.getUserByLDAPName(currentUserId);
			Set<PermissionModel> permissions = currentUser.getUserPermissions();
			for (PermissionModel pm : permissions) {
				if (pm.getModule().equals(getModule()) && pm.getActionType().equals(getActionType())) {
					return true;
				}
			}
			return false;
		}catch (Exception e) {
			String message = "Error checking access for command '" + request.getParameter("method") + "'";
			if (e.getMessage().equals("User not found in database")) {
				message += " because user is not added to the system";
			}
			throw new Exception(message, e);
		}
	}

	public String getAccessDescription() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("Access type " + getActionType().getId() + "- " + getActionType().name() + ", ");
		sb.append("Module: " + getModule().getId() + "- " + getModule().name());
		return sb.toString();
	}
	
	public boolean isAuditable() {
		ActionType[] auditableActions = {
				ActionType.CREATE,
				ActionType.DELETE,
				ActionType.EDIT,
				ActionType.IMPORT,
				ActionType.PRINT_BARCODE,
				ActionType.REPRINT_BARCODE
		};
		
		for (ActionType ac : auditableActions) {
			if (ac.equals(getActionType())) {
				return true;
			}
		}
		
		return false;
	}

	public AuditBean getAuditBean() throws UserException {
		AuditBean bean = new AuditBean();
		bean.setActionTypeId(getActionType().getId());
		bean.setModuleId(getModule().getId());
		bean.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		bean.setUserId(UserModel.getUserByLDAPName(currentUserId).getUserId());
		return bean;
	}
}
