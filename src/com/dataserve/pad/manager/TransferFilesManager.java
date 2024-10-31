package com.dataserve.pad.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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




    public List<Map<String, Object>> getArchiveCenterTransferReadyFiles(String currentUserId) throws Exception {
        try {
            TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
            Set<DmsFiles> files = dao.getArchiveCenterTransferReadyFiles();

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

	
//	public Map<String, Integer> getArchiveCenterTransferReadyFileCounts(String currentUserId) throws Exception {
//	    Map<String, Integer> departmentCounts = new HashMap<>();
//	    
//	    try {
//	        // Retrieve eligible DmsFiles from the DAO
//	        TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
//	        Set<DmsFiles> eligibleFiles = dao.getArchiveCenterTransferReadyFiles();
//
//	        // Count documents by department
//	        for (DmsFiles file : eligibleFiles) {
//	            String deptName = file.getDepartment().getDeptArName();
//	            departmentCounts.put(deptName, departmentCounts.getOrDefault(deptName, 0) + 1);
//	        }
//
//	        return departmentCounts;
//
//	    } catch (Exception e) {
//	        try {
//	            dbConnection.rollBack();
//	        } catch (DatabaseException ex) {
//	            throw new Exception("Error rolling back DB connection", ex);
//	        }
//	        throw new Exception("Error getting Archive Center Transfer Ready File Counts", e);
//
//	    } finally {
//	        try {
//	            dbConnection.releaseConnection();
//	        } catch (DatabaseException ex) {
//	            throw new Exception("Error releasing DB connection", ex);
//	        }
//	    }   
//	}

//	public Set<DmsFiles> getArchiveCenterTransferdFiles(String currentUserId) throws Exception {
//		
//		
//		try {
////			UserDAO userDAO = new UserDAO(dbConnection);
////			User user = userDAO.fetUserByNameLDAP(currentUserId);
//			
//			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
//			return dao.getArchiveCenterTransferdFiles();
//			
//		} catch (Exception e) {
//			
//			try {
//				
//				dbConnection.rollBack();
//			} catch (DatabaseException ex) {
//				throw new Exception("Error rollback DB connection", ex);
//			}
//			// }
//			throw new Exception("Error get Archive Center Transfer Ready Files", e);
//
//		} finally {
//			// close connection
//			try {
//				dbConnection.releaseConnection();
//			} catch (DatabaseException ex) {
//				throw new Exception("Error releaseConnection DB connection", ex);
//			}
//
//		}
//	}
	
	public List<Map<String, Object>> getArchiveCenterTransferdFiles(String currentUserId) throws Exception {
	    try {
	        TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
	        return dao.getArchiveCenterTransferdFiles();
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



	public Set<DmsFiles> getNationalCenterTransferReadyFiles(String currentUserId) throws Exception {
		
		
		try {
//			UserDAO userDAO = new UserDAO(dbConnection);
//			User user = userDAO.fetUserByNameLDAP(currentUserId);
			
			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
			return dao.getNationalCenterTransferReadyFiles();
			
		} catch (Exception e) {
			
			try {
				
				dbConnection.rollBack();
			} catch (DatabaseException ex) {
				throw new Exception("Error rollback DB connection", ex);
			}
			// }
			throw new Exception("Error getNationalCenterTransferReadyFiles", e);

		} finally {
			// close connection
			try {
				dbConnection.releaseConnection();
			} catch (DatabaseException ex) {
				throw new Exception("Error releaseConnection DB connection", ex);
			}
		}	
	}

	
	
	

//	public Set<DmsFiles> getNationalCenterTransferdFiles(String currentUserId) throws Exception {
//			
//			
//			try {
//	//			UserDAO userDAO = new UserDAO(dbConnection);
//	//			User user = userDAO.fetUserByNameLDAP(currentUserId);
//				
//				TransferFilesDAO filesDAO = new TransferFilesDAO(dbConnection);
//				return filesDAO.getNationalCenterTransferdFiles();
//				
//			} catch (Exception e) {
//				
//				try {
//					
//					dbConnection.rollBack();
//				} catch (DatabaseException ex) {
//					throw new Exception("Error rollback DB connection", ex);
//				}
//				// }
//				throw new Exception("Error getNationalCenterTransferdFiles", e);
//	
//			} finally {
//				// close connection
//				try {
//					dbConnection.releaseConnection();
//				} catch (DatabaseException ex) {
//					throw new Exception("Error releaseConnection DB connection", ex);
//				}
//	
//			}
//		
//		}
	
	public List<Map<String, Object>> getNationalCenterTransferdFiles(String currentUserId) throws Exception {
	    try {
	        TransferFilesDAO filesDAO = new TransferFilesDAO(dbConnection);
	        return filesDAO.getNationalCenterTransferdFiles();
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
