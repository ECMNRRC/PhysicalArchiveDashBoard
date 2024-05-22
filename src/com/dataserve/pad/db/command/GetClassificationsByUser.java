package com.dataserve.pad.db.command;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.util.ConfigManager;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;
import com.dataserve.pad.business.users.UserModel;
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
			System.out.println("our currentUserId " + currentUserId);
			System.out.println("getSuperUserName: " + ConfigManager.getSuperUserName());
	
			if (ConfigManager.getSuperUserName().equalsIgnoreCase(currentUserId)) {
				System.out.println("we enter superUser Condition");
				classifications = ClassificationModel.getAllClassifications();
			} else {
				System.out.println("not entering superUser");
				UserModel user = UserModel.getUserByLDAPName(currentUserId);
				classifications = ClassificationModel.getClassificationsByUserId(user.getUserId());
			}
			
			JSONArray arr = new JSONArray();
			for (ClassificationModel c : classifications){
//				if (c.getParentID() == 0) {
//					arr.add(c.getFullStructure());
//				}
				arr.add(c.getFullStructure());

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
		return null;
	}

}
