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
import com.dataserve.pad.util.ConfigManager;

public class GetDocByFilteredDate extends CommandBase {

    public GetDocByFilteredDate(HttpServletRequest request) {
        super(request);
    }

    public String execute() throws Exception {
        String dateTo = request.getParameter("dateTo");
        String dateFrom = request.getParameter("dateFrom");
        String currentUserId = request.getUserPrincipal().getName(); // Assumes the user ID is obtained this way
        JSONObject dataObj = new JSONObject();

        try {
            // Check if the current user is not the superuser
            if (!currentUserId.equalsIgnoreCase(ConfigManager.getSuperUserName())) {
                // Call GetUserDepartments class
                GetUserDepartments getUserDepartments = new GetUserDepartments(request);
                String userDepartmentsOutput = getUserDepartments.execute();
                System.out.println("GetUserDepartments output: " + userDepartmentsOutput);

                // Parse the output to get the department ID
                JSONArray userDepartmentsArray = JSONArray.parse(userDepartmentsOutput);
                if (!userDepartmentsArray.isEmpty()) {
                    JSONObject firstDepartment = (JSONObject) userDepartmentsArray.get(0);
                    String firstDepartmentId = firstDepartment.get("id").toString();
                    dataObj.put("departmentId", firstDepartmentId);
                }

                // Call GetClassificationsByUser class
                GetClassificationsByUser getClassificationsByUser = new GetClassificationsByUser(request);
                String classificationsOutput = getClassificationsByUser.execute();
                System.out.println("GetClassificationsByUser output: " + classificationsOutput);

                // Parse the output and collect all classification symbolic names
                JSONArray classificationsArray = JSONArray.parse(classificationsOutput);
                if (classificationsArray.size() == 1) {
                    JSONObject singleClassification = (JSONObject) classificationsArray.get(0);
                    dataObj.put("classificationId", singleClassification.get("sympolicName").toString());
                } else {
                    JSONArray classificationSymbols = new JSONArray();
                    for (Object obj : classificationsArray) {
                        JSONObject classification = (JSONObject) obj;
                        classificationSymbols.add(classification.get("sympolicName").toString());
                    }
                    dataObj.put("classificationId", classificationSymbols);
                }

                // Print the classificationId to verify
                System.out.println("Updated classificationId in dataObj: " + dataObj.get("classificationId"));
            }

            // Pass dataObj as a parameter to getDocByFilteredDate method
            Set<AuditDataModel> docs = AuditDataModel.getDocByFilteredDate(dateTo, dateFrom, dataObj);
            System.out.println("getLink groups: " + docs);
            JSONArray arr = new JSONArray();
            for (AuditDataModel lm : docs) {
                arr.add(lm.getDocByDateJson());
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
