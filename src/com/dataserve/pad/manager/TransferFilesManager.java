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
import com.dataserve.pad.db.TransferFilesDAO;


public class TransferFilesManager {
	ConnectionManager dbConnection = new ConnectionManager();
	
	

	
	public TransferFilesManager() throws DatabaseException {
		try {
			dbConnection.initConn();
		} catch (DatabaseException e) {
			throw new DatabaseException("Error to open data base connection", e);
		}
	}




    public List<Map<String, Object>> getArchiveCenterTransferReadyFiles(String currentUserId,String departmentName) throws Exception {
        try {
            TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
            Set<DmsFiles> files = dao.getArchiveCenterTransferReadyFiles(departmentName);

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
            throw new Exception("Error retrieving Archive Center Transfer Ready Files", e);

        } finally {
            // Close connection
            try {
                dbConnection.releaseConnection();
            } catch (DatabaseException ex) {
                throw new Exception("Error releasing DB connection", ex);
            }
        }
    }
    
    

    
    
    public List<Map<String, Object>> getMigratedDocumentsStatistics() throws Exception {
        try {
            TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
            return dao.fetchMigratedDocumentsStatistics();

        } catch (Exception e) {
            try {
                dbConnection.rollBack();
            } catch (DatabaseException ex) {
                throw new Exception("Error rolling back DB connection", ex);
            }
            throw new Exception("Error retrieving Archive Center Transfer Ready Files", e);

        } finally {
            // Close connection
            try {
                dbConnection.releaseConnection();
            } catch (DatabaseException ex) {
                throw new Exception("Error releasing DB connection", ex);
            }
        }
    }
    
    


	public List<Map<String, Object>> getArchiveCenterTransferdFiles(String currentUserId, String departmentName) throws Exception {
		try {
			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
			return dao.getArchiveCenterTransferdFiles(departmentName);
		} catch (Exception e) {
			try {
				dbConnection.rollBack();
			} catch (DatabaseException ex) {
				throw new Exception("Error rollback DB connection", ex);
			}
			throw new Exception("Error get Archive Center Transfer Ready Files", e);
		} finally {
			try {
				dbConnection.releaseConnection();
			} catch (DatabaseException ex) {
				throw new Exception("Error releasing DB connection", ex);
			}
		}
	}
	
	public List<Map<String, Object>> getArchiveDocDepartment(String currentUserId, String departmentName) throws Exception {
		try {
			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
			return dao.getArchiveDocDepartment(departmentName);
		} catch (Exception e) {
			try {
				dbConnection.rollBack();
			} catch (DatabaseException ex) {
				throw new Exception("Error rollback DB connection", ex);
			}
			throw new Exception("Error get Archive Center Transfer Ready Files", e);
		} finally {
			try {
				dbConnection.releaseConnection();
			} catch (DatabaseException ex) {
				throw new Exception("Error releasing DB connection", ex);
			}
		}
	}
	



	public List<Map<String, Object>> getNationalCenterTransferReadyFiles(String currentUserId,String departmentName) throws Exception {
		try {
			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
			Set<DmsFiles> files = dao.getNationalCenterTransferReadyFiles(departmentName);
	
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
			throw new Exception("Error retrieving national center transfer ready files", e);
	
		} finally {
			try {
				dbConnection.releaseConnection();
			} catch (DatabaseException ex) {
				throw new Exception("Error releasing DB connection", ex);
			}
		}
	}
	


	public List<Map<String, Object>> getNationalCenterTransferdFiles(String currentUserId,String departmentName) throws Exception {
	    try {
	        TransferFilesDAO filesDAO = new TransferFilesDAO(dbConnection);
	        return filesDAO.getNationalCenterTransferdFiles(departmentName);
	    } catch (Exception e) {
	        try {
	            dbConnection.rollBack();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error rolling back DB connection", ex);
	        }
	        throw new Exception("Error getNationalCenterTransferdFiles", e);
	    } finally {
	        try {
	            dbConnection.releaseConnection();
	        } catch (DatabaseException ex) {
	            throw new Exception("Error releasing DB connection", ex);
	        }
	    }
	}

}
