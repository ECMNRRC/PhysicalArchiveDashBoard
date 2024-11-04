package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.dataserve.pad.manager.TransferFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetArchiveCenterTransferReadyFiles extends CommandBase {

    public GetArchiveCenterTransferReadyFiles(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String execute() throws Exception {
        try {
        	String departmentName = request.getParameter("departmentName"); // Get filter parameter
            TransferFilesManager transferFilesManager = new TransferFilesManager();
            List<Map<String, Object>> departmentCounts = transferFilesManager.getArchiveCenterTransferReadyFiles(currentUserId,departmentName);

            JSONArray arr = new JSONArray();
            for (Map<String, Object> deptData : departmentCounts) {
                JSONObject deptJson = new JSONObject();
                deptJson.put("deptArName", deptData.get("deptArName"));
                deptJson.put("documentCount", deptData.get("documentCount"));
                arr.add(deptJson);
            }
            return arr.toString();

        } catch (Exception e) {
            throw new Exception("Error retrieving Archive Center Transfer Ready Files for user with username '" + currentUserId + "'", e);
        }
    }

    @Override
    protected Module getModule() {
        return Module.FILE_TRANSFER_DESTRUCTION_ADMIN;
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ALL_ACTIONS;
    }
}
