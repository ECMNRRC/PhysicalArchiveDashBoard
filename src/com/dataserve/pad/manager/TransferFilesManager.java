package com.dataserve.pad.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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




	public Set<DmsFiles> getArchiveCenterTransferReadyFiles(String currentUserId) throws Exception {
		
		
		try {
//			UserDAO userDAO = new UserDAO(dbConnection);
//			User user = userDAO.fetUserByNameLDAP(currentUserId);
			
			TransferFilesDAO dao = new TransferFilesDAO(dbConnection);
			return dao.getArchiveCenterTransferReadyFiles();
			
		} catch (Exception e) {
			
			try {
				
				dbConnection.rollBack();
			} catch (DatabaseException ex) {
				throw new Exception("Error rollback DB connection", ex);
			}
			// }
			throw new Exception("Error get Archive Center Transfer Ready Files", e);

		} finally {
			// close connection
			try {
				dbConnection.releaseConnection();
			} catch (DatabaseException ex) {
				throw new Exception("Error releaseConnection DB connection", ex);
			}
		}	
	}
	
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
