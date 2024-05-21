package com.dataserve.pad.db.command;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.business.departments.DepartmentException;
import com.dataserve.pad.business.departments.DepartmentModel;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.permissions.ActionType;

public class GetDepartment extends CommandBase {

	public GetDepartment(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		String departmentIdStr = request.getParameter("departmentId");
		if (departmentIdStr == null || departmentIdStr.trim().equals("")) {
			throw new DepartmentException("Department Id must be provided");
		}
		try {
			int departmentId = Integer.parseInt(departmentIdStr);
			DepartmentModel dept = DepartmentModel.getDepartmentById(departmentId);
			return dept.getAsJson().toString();
		} catch (Exception e) {
			throw new DepartmentException("Error getting department by it's id.", e);
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
