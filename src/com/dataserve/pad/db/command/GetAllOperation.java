package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.bean.OperationModel;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.db.command.CommandBase;
import com.ibm.json.java.JSONArray;

public class GetAllOperation extends CommandBase{
	
	public GetAllOperation(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {


		try{
			
			Set<OperationModel> docs = OperationModel.getAllOperations();
			System.out.println("getLink groups: "+ docs);
			JSONArray arr = new JSONArray();
			for (OperationModel lm : docs) {
				arr.add(lm.getAsJson());
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
