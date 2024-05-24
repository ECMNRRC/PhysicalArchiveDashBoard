package com.dataserve.pad.business.users;

import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.permissions.PermissionException;
import com.dataserve.pad.permissions.PermissionModel;
import com.dataserve.pad.permissions.PermissionsManager;
import com.dataserve.pad.bean.UserBean;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.UserDAO;
import com.ibm.json.java.JSONObject;
public class UserModel {	
	private UserBean bean;
	
	public UserModel(UserBean bean) {
		this.bean = bean;
	}
	
	public UserModel(int id, String nameAr, String nameEn, String usernameLDAP, String email, boolean login, boolean IsActive, Integer departmentId) throws UserException {
		UserBean bean = new UserBean();
		bean.setId(id);
		bean.setNameAr(nameAr);
		bean.setNameEn(nameEn);
		bean.setUsernameLDAP(usernameLDAP);
		bean.setEmail(email);
		bean.setIsLogin(login);
		bean.setIsActive(IsActive);
		bean.setDepartmentId(departmentId);
		this.bean = bean;
	}
	
	public static Set<UserModel> getAllUsers() throws UserException {
		Set<UserModel> users;
		try {
			users = new LinkedHashSet<UserModel>();
			UserDAO dao = new UserDAO();
			Set<UserBean> userBeans = dao.fetchAllUsers();
			for (UserBean b : userBeans) {
				UserModel um = new UserModel(b);
				users.add(um);
			}
		} catch (DatabaseException e) {
			throw new UserException("Error getting orphaned boxes from database.");
		}
		return users;
	}
	
	public static UserModel getUser(int userId) throws UserException {
		try {
			UserDAO dao = new UserDAO();			
			UserBean userBean = dao.fetchUser(userId);
			UserModel userModel = new UserModel(userBean);
			return userModel;
		} catch (DatabaseException e) {
			throw new UserException("Error getting orphaned users from database.");
		}
	}
	
	public static Set<UserModel> getUsers(String userIds) throws UserException {
		Set<UserModel> users = new LinkedHashSet<UserModel>();
		try {
			UserDAO dao = new UserDAO();
			Set<UserBean> userBeans = dao.fetchUsers(userIds);
			for (UserBean b : userBeans) {
				users.add(new UserModel(b));
			}
			return users;
		} catch (Exception e) {
			throw new UserException("Error getting User Models for ids '" + userIds + "'", e);
		}
	}
	
	public static Set<UserModel> getUsersByDepId(String DepId) throws UserException {
		Set<UserModel> users = new LinkedHashSet<UserModel>();
		try {
			UserDAO dao = new UserDAO();
			Set<UserBean> userBeans = dao.fetchUsersByDepId(DepId);
			for (UserBean b : userBeans) {
				users.add(new UserModel(b));
			}
			return users;
		} catch (Exception e) {
			throw new UserException("Error getting User Models for ids '" + DepId + "'", e);
		}
	}
	
	public static UserModel getUserByLDAPName(String userNameLDAP) throws UserException {
		try {
			UserDAO dao = new UserDAO();
			UserBean ub = dao.fetUserByNameLDAP(userNameLDAP);
			if (ub != null) {
				return new UserModel(ub);
			}			
		} catch (Exception e) {
			throw new UserException("Error getting user model", e);
		}
		throw new UserException("User not found in database");
	}
	
	public void save() throws UserException {
		try {
			UserDAO dao = new UserDAO();
			if (bean.getId() == 0) {
				dao.addUser(bean);
			} else {
				dao.updateUser(bean);
			}
		} catch (DatabaseException e) {
			throw new UserException("Error saving '" + bean.getId() + "' to database", e);
		}		
	}

	public JSONObject getAsJson() {
		JSONObject obj = new JSONObject();
		obj.put("id", bean.getId());
		obj.put("nameAr", bean.getNameAr());
		obj.put("nameEn", bean.getNameEn());
		obj.put("UsernameLDAP", bean.getUsernameLDAP());
		obj.put("email", bean.getEmail());
		obj.put("IsLogin", bean.getIsLogin());
		obj.put("IsActive", bean.getIsActive());
		obj.put("departmentId", bean.getDepartmentId());
		return obj;
	}
	
	public Set<PermissionModel> getUserPermissions() throws PermissionException {
		Set<PermissionModel> userPermissions = new LinkedHashSet<PermissionModel>();
		try {
			UserDAO dao = new UserDAO();
			Set<Integer> permissionIds = dao.fetchUserPermissionIds(bean);
			for (Integer id : permissionIds) {
				userPermissions.add(PermissionsManager.getPermissionById(id));
			}
			return userPermissions;
		} catch (Exception e) {
			throw new PermissionException("Error getting user permissions!", e);
		}
	}
	
	public String getUserNameLDAP() {
		return bean.getUsernameLDAP();
	}
	
	public String getNameAr() {
		return bean.getNameAr();
	}
	
	public String getNameEn() {
		return bean.getNameEn();
	}

	public int getUserId() {
		return bean.getId();
	}
	
	public Integer getDepartmentId() {
		return bean.getDepartmentId();
	}
	
	public Set<Integer> getUserGroupIds() {
		return bean.getGroupsIds();
	}

	public void updateUserSignature() throws UserException{
		try {
			UserDAO dao = new UserDAO();
			dao.updateUserSignature(bean);
		} catch (DatabaseException e) {
			throw new UserException("Error saving user signature ", e);
		}	
		
	}
}
