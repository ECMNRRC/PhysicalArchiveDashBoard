package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.manager.TransferFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;

public class GetNationalCenterTransferdFiles extends CommandBase{

	

	public GetNationalCenterTransferdFiles(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		try {
			TransferFilesManager filesManager  = new TransferFilesManager();
			Set<DmsFiles> files = filesManager.getNationalCenterTransferdFiles(currentUserId);
			
			JSONArray arr = new JSONArray();
			for (DmsFiles file : files) {
				arr.add(file.getAsJson(callBacks.getLocale()));
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
