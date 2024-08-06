package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.util.ConfigManager;
import com.dataserve.pad.db.command.CommandBase;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetOperationToDep extends CommandBase{
	
	public GetOperationToDep(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {
        String dataObjParam = request.getParameter("dataObj");
        JSONObject dataObj = (dataObjParam != null && !dataObjParam.isEmpty()) ? JSONObject.parse(dataObjParam) : new JSONObject();

	    try {
	    	
            String departmentId = dataObj.containsKey("departmentId") ? (String) dataObj.get("departmentId") : "";

            if (departmentId.trim().isEmpty()) {
    			if (!isValuePresent(ConfigManager.getSuperUserName(), currentUserId)) {
//                if (!currentUserId.equalsIgnoreCase(ConfigManager.getSuperUserName())) {
                    GetUserDepartments getUserDepartments = new GetUserDepartments(request);
                    String userDepartmentsOutput = getUserDepartments.execute();

                    JSONArray userDepartmentsArray = JSONArray.parse(userDepartmentsOutput);
                    if (!userDepartmentsArray.isEmpty()) {
                        JSONObject firstDepartment = (JSONObject) userDepartmentsArray.get(0);
                        String firstDepartmentId = firstDepartment.get("id").toString();
                        dataObj.put("departmentId", firstDepartmentId);
                    }
                }
            }
            
			Set<AuditDataModel> docs = AuditDataModel.getOperationToDep(dataObj);
			JSONArray arr = new JSONArray();
			for (AuditDataModel lm : docs) {
				arr.add(lm.getOperationToDepJson());
			}
			return arr.toString();
    } catch (Exception e) {
        throw new Exception("Error getting Linked Document", e);
    }
}
	
	
	
	@Override
	protected Module getModule() {
		return null;
	}

	@Override
	protected ActionType getActionType() {
		return null;
	}

}
