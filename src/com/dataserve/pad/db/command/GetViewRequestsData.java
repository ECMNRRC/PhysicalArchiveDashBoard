package com.dataserve.pad.db.command;

import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONObject;
import com.ibm.json.java.JSONArray;

public class GetViewRequestsData extends CommandBase {
    
    public GetViewRequestsData(HttpServletRequest request) {
        super(request);
    }

    public String execute() throws Exception {
        try {
            List<Map<String, Object>> requestData = AuditDataModel.getViewRequestsData();
            JSONArray jsonArr = new JSONArray();
            
            // Convert each row to JSON
            for (Map<String, Object> row : requestData) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("ViewType", row.get("ViewType"));
                jsonObj.put("Status", row.get("Status"));
                jsonObj.put("ViewRequestTypeCount", row.get("ViewRequestTypeCount"));
                jsonObj.put("CorrStatusCount", row.get("CorrStatusCount"));
                jsonArr.add(jsonObj);
            }

            return jsonArr.toString(); // Return the data as JSON
        } catch (Exception e) {
            throw new Exception("Error getting view requests data", e);
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
