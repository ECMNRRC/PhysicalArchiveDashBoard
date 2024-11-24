package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.manager.TransferFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetConfidentialDocClassification extends CommandBase {

    public GetConfidentialDocClassification(HttpServletRequest request) {
        super(request);
    }

    public String execute() throws Exception {
        try {

			TransferFilesManager transferFilesManager = new TransferFilesManager();
            List<Map<String, Object>> classificationCounts = transferFilesManager.getConfidentialDocClassification(currentUserId);

            JSONArray arr = new JSONArray();
            for (Map<String, Object> classificationData : classificationCounts) {
                JSONObject classificationJson = new JSONObject();
                classificationJson.put("propertyValue", classificationData.get("propertyValue"));
                classificationJson.put("documentCount", classificationData.get("documentCount"));
                arr.add(classificationJson);
            }
            return arr.toString();
        } catch (Exception e) {
            throw new Exception("Error retrieving confidential document classification for user '" + currentUserId + "'", e);
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
