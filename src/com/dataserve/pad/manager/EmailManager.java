//package com.dataserve.pad.manager;
//
//import com.dataserve.pad.db.ConnectionManager;
//import com.dataserve.pad.db.DatabaseException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.dataserve.pad.bean.User;
//import com.dataserve.pad.db.dao.UserDAO;
//import com.dataserve.pad.util.ConfigManager;
//import com.dataserve.pad.util.EmailUtil;
//
//public class EmailManager {
//	ConnectionManager dbConnection = new ConnectionManager();
//
//	public EmailManager() throws DatabaseException {
//		try {
//			dbConnection.initConn();
//		} catch (DatabaseException e) {
//			throw new DatabaseException("Error to open data base connection", e);
//		}
//	}
//
//	public String sendHelpRequestEmail(String mailTemplate, String currentUserId) throws Exception {
//		try {
////			UserDAO userDAO = new UserDAO(dbConnection);
////			User user = userDAO.fetUserByNameLDAP(currentUserId);
//							
//			String subject =  "Physical archiving help request";
//			List<String> to = new ArrayList<String>();
//			to.add(ConfigManager.getItSupportEmail());
//				
//			EmailUtil.sendMail(subject, to, mailTemplate);
//			
//			dbConnection.commit();
//			return "Succes";
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
//			throw new Exception("Error sendHelpRequestEmail", e);
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
//
//	}
//	
//	
//}
