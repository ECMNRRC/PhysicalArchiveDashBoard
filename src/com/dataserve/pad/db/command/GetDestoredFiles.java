package com.dataserve.pad.db.command;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.manager.DestroyFilesManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.ibm.json.java.JSONArray;

public class GetDestoredFiles extends CommandBase{

	

	public GetDestoredFiles(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		try {
			DestroyFilesManager destroyFilesManager  = new DestroyFilesManager();
			Set<DmsFiles> files = destroyFilesManager.getAllDestoredFiles(currentUserId);
			
			JSONArray arr = new JSONArray();
			for (DmsFiles file : files) {
				arr.add(file.getAsJson(callBacks.getLocale()));
			}
			return arr.toString();
		} catch (Exception e) {
			throw new Exception("Error returning departments for user with username '" + currentUserId + "'", e);
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
