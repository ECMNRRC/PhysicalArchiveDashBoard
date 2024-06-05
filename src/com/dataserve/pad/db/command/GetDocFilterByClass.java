//package com.dataserve.pad.db.command;
//
//import java.util.Set;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.dataserve.pad.bean.AuditDataModel;
//import com.dataserve.pad.business.classification.ClassificationException;
//import com.dataserve.pad.business.classification.ClassificationModel;
//import com.dataserve.pad.permissions.ActionType;
//import com.dataserve.pad.permissions.Module;
//import com.dataserve.pad.util.ConfigManager;
//import com.dataserve.pad.db.command.CommandBase;
//import com.ibm.json.java.JSONArray;
//import com.ibm.json.java.JSONObject;
//
//public class GetDocFilterByClass extends CommandBase{
//	
//	public GetDocFilterByClass(HttpServletRequest request) {
//		super(request);
//	}
//
//	
//	public String execute() throws Exception {
////	    JSONObject dataObj = new JSONObject();
////	    String dataObjParam = request.getParameter("dataObj");
//
////	    if (dataObjParam != null && !dataObjParam.isEmpty()) {
////	        // Attempt to parse the input string as JSON
////	        try {
////	            dataObj =  JSONObject.parse(request.getParameter("dataObj"));
////	        } catch (Exception e) {
////	            // Handle parsing error
////	            System.err.println("Error parsing dataObjParam: " + e.getMessage());
////	        }
////	    }
//		
//	    JSONObject dataObj =  JSONObject.parse(request.getParameter("dataObj"));
//
//
//	    System.out.println("************Before Obj *********");
//	    System.out.println("here is the dataObj: " + dataObj);
//	    System.out.println("************After Obj *********");
//
//	    System.out.println("inside GetAudit Class");
//	    try {
//	    	
//	        String departmentId = dataObj.containsKey("departmentId") ? (String) dataObj.get("departmentId") : "";
//
//	        // Replace this with the actual way to get the current user ID from your session or request
//	        String currentUserId = (String) request.getSession().getAttribute("currentUserId");
//
//	        // Check if departmentId is empty or null
//	        if (departmentId.trim().isEmpty()) {
//	            if (!currentUserId.equalsIgnoreCase(ConfigManager.getSuperUserName())) {
//	                // Call GetUserDepartments class
//	                GetUserDepartments getUserDepartments = new GetUserDepartments();
//	                String userDepartmentsOutput = getUserDepartments.execute();
//	                System.out.println("GetUserDepartments output: " + userDepartmentsOutput);
//
//	                // Optionally, you can use the output in further logic if needed
//	                // For example, you might want to add it to dataObj
//	                dataObj.put("userDepartments", JSONArray.parse(userDepartmentsOutput));
//	            }
//	        }
//
//	        Set<AuditDataModel> docs = AuditDataModel.getDocFilterByClass(dataObj);
//	        System.out.println("getLink groups: " + docs);
//	        JSONArray arr = new JSONArray();
//	        for (AuditDataModel lm : docs) {
//	            arr.add(lm.getAsJson());
//	        }
//	        return arr.toString();
//	    } catch (Exception e) {
//	        throw new Exception("Error getting Linked Document", e);
//	    }
//	}
//	
//	
//	
//	@Override
//	protected Module getModule() {
//		return null;
//	}
//
//	@Override
//	protected ActionType getActionType() {
//		return null;
//	}
//
//}


package com.dataserve.pad.db.command;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.util.ConfigManager;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetDocFilterByClass extends CommandBase {

    public GetDocFilterByClass(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String execute() throws Exception {
        String dataObjParam = request.getParameter("dataObj");
        JSONObject dataObj = (dataObjParam != null && !dataObjParam.isEmpty()) ? JSONObject.parse(dataObjParam) : new JSONObject();


        try {
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

            Set<AuditDataModel> docs = AuditDataModel.getDocFilterByClass(dataObj);
            JSONArray arr = new JSONArray();
            for (AuditDataModel lm : docs) {
                arr.add(lm.getAsJson());
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

