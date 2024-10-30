package com.dataserve.pad.manager;

import java.util.List;
import java.util.Locale;
import java.util.Map;
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
	
	public List<Map<String, Object>> getAllDestroedFiles(String currentUserId) throws Exception {
	    try {
	        DestroyFilesDAO destroyFilesDAO = new DestroyFilesDAO(dbConnection);
	        return destroyFilesDAO.getAllDestroedFiles();
	    } catch (Exception e) {
	        try {
	            dbConnection.rollBack();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error rolling back DB connection", ex);
	        }
	        throw new Exception("Error getAllDestroedFiles", e);
	    } finally {
	        try {
	            dbConnection.releaseConnection();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error releasing DB connection", ex);
	        }
	    }
	}

	
}

