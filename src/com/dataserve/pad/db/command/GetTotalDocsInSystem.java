package com.dataserve.pad.db.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONObject;

public class GetTotalDocsInSystem extends CommandBase{
	
	public GetTotalDocsInSystem(HttpServletRequest request) {
		super(request);
	}

	
    public String execute() throws Exception {
        try {
            Map<String, Integer> counts = AuditDataModel.getTotalDocsInSystem();
            JSONObject json = new JSONObject();
            json.put("TotalCount", counts.get("TotalCount"));
            return json.toString();
        } catch (Exception e) {
            throw new Exception("Error getting document counts", e);
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
