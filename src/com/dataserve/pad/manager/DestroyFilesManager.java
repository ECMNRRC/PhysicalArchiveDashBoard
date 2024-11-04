package com.dataserve.pad.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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



	public List<Map<String, Object>> getAllReadyDestroyFiles(String currentUserId) throws Exception {
	    try {
	        DestroyFilesDAO destroyFilesDAO = new DestroyFilesDAO(dbConnection);
	        Set<DmsFiles> files = destroyFilesDAO.getAllReadyDestroyFiles();

	        // Aggregating count per department
	        Map<String, Long> departmentCounts = files.stream()
	            .collect(Collectors.groupingBy(
	                file -> file.getDepartment().getDeptArName(), // Group by department Arabic name
	                Collectors.counting() // Count documents per department
	            ));

	        // Convert the map to the required format
	        List<Map<String, Object>> result = new ArrayList<>();
	        for (Map.Entry<String, Long> entry : departmentCounts.entrySet()) {
	            Map<String, Object> deptData = new HashMap<>();
	            deptData.put("deptArName", entry.getKey());
	            deptData.put("documentCount", entry.getValue());
	            result.add(deptData);
	        }

	        return result;

	    } catch (Exception e) {
	        try {
	            dbConnection.rollBack();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error rolling back DB connection", ex);
	        }
	        throw new Exception("Error retrieving ready-to-destroy files", e);

	    } finally {
	        // Close connection
	        try {
	            dbConnection.releaseConnection();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error releasing DB connection", ex);
	        }
	    }
	}

	
	public List<Map<String, Object>> getAllDestroedFiles(String currentUserId,String departmentName) throws Exception {
	    try {
	        DestroyFilesDAO destroyFilesDAO = new DestroyFilesDAO(dbConnection);
	        return destroyFilesDAO.getAllDestroedFiles(departmentName);
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

