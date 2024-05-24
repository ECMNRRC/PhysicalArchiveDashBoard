package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.business.departments.DepartmentException;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.business.users.UserException;
import com.dataserve.pad.business.users.UserModel;
import com.ibm.json.java.JSONArray;

public class GetUsersByDepartmentId extends CommandBase {

	public GetUsersByDepartmentId(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		String depIdsStr = request.getParameter("depId");

		if (depIdsStr == null) {
			throw new DepartmentException("User Id(s) must be provided");
		}
		
		if (depIdsStr.trim().equals("")) {
			return new JSONArray().toString();
		}
		
		try {
			Set<UserModel> userModels =  UserModel.getUsersByDepId(depIdsStr);
			JSONArray arr = new JSONArray();
			for (UserModel um : userModels) {
				arr.add(um.getAsJson());
			}
			return arr.toString();
		} catch (Exception e) {
			throw new UserException("Error getting user with id '" + depIdsStr + "'", e);
		}
	}

	@Override
	protected Module getModule() {
		return Module.USER;
	}

	@Override
	protected ActionType getActionType() {
		return null;
	}

}
