package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.dataserve.pad.bean.AuditDataModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetBorrowingRequestsData extends CommandBase {

    public GetBorrowingRequestsData(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String execute() throws Exception {
        try {
            // Fetch borrowing request statistics from the AuditDataModel
            List<Map<String, Object>> borrowingRequestStats = AuditDataModel.getBorrowingRequestsData();

            // Create a JSON array to return the data
            JSONArray jsonArr = new JSONArray();

            // Loop through each row in the stats and create a JSON object
            for (Map<String, Object> row : borrowingRequestStats) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("Status", row.get("Status")); // Status (accepted, rejected, completed)
                jsonObj.put("CORR_STATUS_COUNT", row.get("CORR_STATUS_COUNT")); // Request count
                jsonArr.add(jsonObj);
            }

            // Return the JSON array as a string
            return jsonArr.toString();

        } catch (Exception e) {
            throw new Exception("Error getting borrowing request data", e);
        }
    }

    @Override
    protected Module getModule() {
        return null; // Implement if you have specific module logic
    }

    @Override
    protected ActionType getActionType() {
        return null; // Implement if you have specific action types
    }
}
