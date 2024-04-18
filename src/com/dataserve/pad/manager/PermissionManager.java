package com.dataserve.pad.manager;

import java.util.Set;

import com.dataserve.pad.bean.PermissionBean;
import com.dataserve.pad.bean.User;
import com.dataserve.pad.db.ConnectionManager;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.dao.UserDAO;

public class PermissionManager {
	ConnectionManager dbConnection = new ConnectionManager();
	
	
	public PermissionManager() throws DatabaseException {
		try {
			dbConnection.initConn();
		} catch (DatabaseException e) {
			throw new DatabaseException("Error to open data base connection", e);
		}
	}


	public Set<PermissionBean>  getUserPermissions(String currentUserId) throws Exception {
		try{
			UserDAO userDAO = new UserDAO(dbConnection);
			User user = userDAO.fetUserByNameLDAP(currentUserId);
			Set<PermissionBean> permisssions = userDAO.fetchUserPermission(user.getUserId());
			return permisssions;

			
		}catch (Exception e) {
				
				try {
					
					dbConnection.rollBack();
				} catch (DatabaseException ex) {
					throw new Exception("Error rollback DB connection", ex);
				}
				// }
				throw new Exception("Error getAllDestroyFiles", e);

			} finally {
				// close connection
				try {
					dbConnection.releaseConnection();
				} catch (DatabaseException ex) {
					throw new Exception("Error releaseConnection DB connection", ex);
				}

			}
	}

}
