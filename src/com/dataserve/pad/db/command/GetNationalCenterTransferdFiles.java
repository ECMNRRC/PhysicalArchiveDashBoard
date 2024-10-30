package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.manager.TransferFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class GetNationalCenterTransferdFiles extends CommandBase{

	

	public GetNationalCenterTransferdFiles(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		try {
	        TransferFilesManager filesManager = new TransferFilesManager();
	        List<Map<String, Object>> departmentCounts = filesManager.getNationalCenterTransferdFiles(currentUserId);

	        JSONArray arr = new JSONArray();
	        for (Map<String, Object> deptData : departmentCounts) {
	            JSONObject deptJson = new JSONObject();
	            deptJson.put("deptArName", deptData.get("deptArName"));
	            deptJson.put("documentCount", deptData.get("documentCount"));
	            arr.add(deptJson); 
	        }
	        return arr.toString();
	    } catch (Exception e) {
	        throw new Exception("Error GetNationalCenterTransferdFiles for user with username '" + currentUserId + "'", e);
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
