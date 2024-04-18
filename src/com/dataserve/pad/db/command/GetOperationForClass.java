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

public class GetOperationForClass extends CommandBase{
	
	public GetOperationForClass(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {
		
		try{
			Set<AuditDataModel> docs = AuditDataModel.getOperationForClass();
			System.out.println("getLink groups: "+ docs);
			JSONArray arr = new JSONArray();
			for (AuditDataModel lm : docs) {
				arr.add(lm.getOperationForClassJson());
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
