package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.business.users.UserModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.util.ConfigManager;
import com.ibm.json.java.JSONArray;

public class GetClassificationsByUser extends CommandBase {

	public GetClassificationsByUser(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String execute() throws Exception {
		try {
			if (currentUserId == null) {
				currentUserId = request.getParameter("userId");
			}
			
			Set<ClassificationModel> classifications;

	
			if (isValuePresent(ConfigManager.getSuperUserName(), currentUserId)) {
				classifications = ClassificationModel.getAllClassifications();
			} else {
				UserModel user = UserModel.getUserByLDAPName(currentUserId);
				classifications = ClassificationModel.getClassificationsByUserId(user.getUserId());
			}
			
			JSONArray arr = new JSONArray();
			for (ClassificationModel c : classifications){

				arr.add(c.getAsJson());

			}
			return arr.toString();
		} catch (Exception e) {
			throw new ClassificationException("Error getting classification for user with id '" + currentUserId + "'", e);
		}
	}
	
	
	@Override
	protected Module getModule() {
		return Module.CLASSIFICATION;
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.VIEW;
	}

}
