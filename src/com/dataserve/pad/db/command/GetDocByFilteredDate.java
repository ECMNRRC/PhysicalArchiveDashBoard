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

public class GetDocByFilteredDate extends CommandBase{
	
	public GetDocByFilteredDate(HttpServletRequest request) {
		super(request);
	}

	
	public String execute() throws Exception {
		String dateTo = request.getParameter("dateTo");
		String dateFrom = request.getParameter("dateFrom");

		try{
			
			Set<AuditDataModel> docs = AuditDataModel.getDocByFilteredDate(dateTo, dateFrom);
			System.out.println("getLink groups: "+ docs);
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
