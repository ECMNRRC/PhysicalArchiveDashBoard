package com.dataserve.pad.manager;

import java.util.Locale;
import java.util.Set;


import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.db.ConnectionManager;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.DestroyFilesDAO;
;

public class DestroyFilesManager {
	ConnectionManager dbConnection = new ConnectionManager();
	
	

	
	public DestroyFilesManager() throws DatabaseException {
		try {
			dbConnection.initConn();
		} catch (DatabaseException e) {
			throw new DatabaseException("Error to open data base connection", e);
		}
	}




	public Set<DmsFiles> getAllReadyDestroyFiles(String currentUserId) throws Exception {
		
		
		try {
			
			DestroyFilesDAO destroyFilesDAO = new DestroyFilesDAO(dbConnection);
			return destroyFilesDAO.getAllReadyDestroyFiles();
			
		} catch (Exception e) {
			
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
	
	public Set<DmsFiles> getAllDestoredFiles(String currentUserId) throws Exception {
		
		
		try {
//			UserDAO userDAO = new UserDAO(dbConnection);
//			User user = userDAO.fetUserByNameLDAP(currentUserId);
			
			DestroyFilesDAO destroyFilesDAO = new DestroyFilesDAO(dbConnection);
			return destroyFilesDAO.getAllDestroedFiles();
			
		} catch (Exception e) {
			
			try {
				
				dbConnection.rollBack();
			} catch (DatabaseException ex) {
				throw new Exception("Error rollback DB connection", ex);
			}
			// }
			throw new Exception("Error get All Destroed Files", e);

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
