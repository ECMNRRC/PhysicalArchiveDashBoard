package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.dataserve.pad.manager.TransferFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetCalculatedMigratedDocuments extends CommandBase {

    public GetCalculatedMigratedDocuments(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String execute() throws Exception {
        try {

            TransferFilesManager transferFilesManager = new TransferFilesManager();
            List<Map<String, Object>> migrationStats = transferFilesManager.getMigratedDocumentsStatistics();

            JSONArray resultArray = new JSONArray();
            for (Map<String, Object> stat : migrationStats) {
                JSONObject statJson = new JSONObject();
                statJson.put("integrationSysAr", stat.get("integrationSysAr"));
                statJson.put("integrationSysEn", stat.get("integrationSysEn"));
                statJson.put("documentCount", stat.get("totalDocuments"));
                resultArray.add(statJson);
            }
            return resultArray.toString();

        } catch (Exception e) {
            throw new Exception("Error retrieving Archive Center Transfer Ready Files for user with username '" + currentUserId + "'", e);
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
