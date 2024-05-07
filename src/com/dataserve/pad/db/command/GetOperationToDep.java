package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.db.command.CommandBase;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetOperationToDep extends CommandBase{
	
	public GetOperationToDep(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {
	    JSONObject dataObj = new JSONObject();
	    String dataObjParam = request.getParameter("dataObj");
	    if (dataObjParam != null && !dataObjParam.isEmpty()) {
	        // Attempt to parse the input string as JSON
	        try {
	            dataObj =  JSONObject.parse(request.getParameter("dataObj"));
	        } catch (Exception e) {
	            // Handle parsing error
	            System.err.println("Error parsing dataObjParam: " + e.getMessage());
	        }
	    }
	    try {
			Set<AuditDataModel> docs = AuditDataModel.getOperationToDep(dataObj);
			System.out.println("getLink groups: "+ docs);
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
