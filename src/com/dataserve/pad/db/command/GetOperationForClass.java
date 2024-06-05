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

public class GetOperationForClass extends CommandBase{
	
	public GetOperationForClass(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {
        String dataObjParam = request.getParameter("dataObj");
        JSONObject dataObj = (dataObjParam != null && !dataObjParam.isEmpty()) ? JSONObject.parse(dataObjParam) : new JSONObject();

		try{
			
            String departmentId = dataObj.containsKey("departmentId") ? (String) dataObj.get("departmentId") : "";


            if (departmentId.trim().isEmpty()) {
                if (!currentUserId.equalsIgnoreCase(ConfigManager.getSuperUserName())) {
                    GetUserDepartments getUserDepartments = new GetUserDepartments(request);
                    String userDepartmentsOutput = getUserDepartments.execute();

                    JSONArray userDepartmentsArray = JSONArray.parse(userDepartmentsOutput);
                    if (!userDepartmentsArray.isEmpty()) {
                        JSONObject firstDepartment = (JSONObject) userDepartmentsArray.get(0);
                        String firstDepartmentId = firstDepartment.get("id").toString();
                        dataObj.put("departmentId", firstDepartmentId);
                    }

                    GetClassificationsByUser getClassificationsByUser = new GetClassificationsByUser(request);
                    String classificationsOutput = getClassificationsByUser.execute();

                    JSONArray classificationsArray = JSONArray.parse(classificationsOutput);
                    JSONArray classificationSymbols = new JSONArray();
                    for (Object obj : classificationsArray) {
                        JSONObject classification = (JSONObject) obj;
                        classificationSymbols.add(classification.get("sympolicName").toString());
                    }
                    dataObj.put("classificationId", classificationSymbols);

                }
            }
            
			Set<AuditDataModel> docs = AuditDataModel.getOperationForClass(dataObj);
			JSONArray arr = new JSONArray();
			for (AuditDataModel lm : docs) {
				arr.add(lm.getOperationForClassJson());
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
