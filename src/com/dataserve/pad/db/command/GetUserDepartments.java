package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.util.ConfigManager;
import com.dataserve.pad.business.departments.DepartmentModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.business.users.UserException;
import com.dataserve.pad.business.users.UserModel;
import com.ibm.json.java.JSONArray;

public class GetUserDepartments extends CommandBase {

	public GetUserDepartments(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws UserException {
		if (currentUserId == null || currentUserId.trim().equals("")) {
			currentUserId = request.getParameter("userId");
		}
		if (currentUserId == null || currentUserId.trim().equals("")) {
			throw new UserException("User id must be provided");
		}
		try {
			JSONArray arr = new JSONArray();
			if (isValuePresent(ConfigManager.getSuperUserName(), currentUserId)) {
				Set<DepartmentModel> departments = DepartmentModel.getAllDepartmentsAsTree();
				for (DepartmentModel dm : departments) {
					arr.add(dm.getAsJson());
				}
			} else {
				UserModel um = UserModel.getUserByLDAPName(currentUserId);
				Set<DepartmentModel> departments = DepartmentModel.getDepartmentStructureById(um.getDepartmentId());
		    	for (DepartmentModel dm : departments) {
			        arr.add(dm.getAsJson());
		    	}
			}
			return arr.toString();
		} catch (Exception e) {
			throw new UserException("Error returning departments for user with username '" + currentUserId + "'", e);
		}
	}

	@Override
	protected Module getModule() {
		return Module.DEPARTMENT;
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.VIEW;
	}

}
