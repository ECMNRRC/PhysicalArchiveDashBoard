package com.dataserve.pad.util;

import java.util.Properties;
import java.util.Set;

import com.dataserve.pad.bean.ConfigBean;
import com.dataserve.pad.db.ConnectionManager;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.dao.ConfigDAO;

public class ConfigManager {
	private static Properties props;
	

//	## Mail Server Props 
//	## Mail Sender config
	private static  String emailName;
	private static  String emailPassword;
	private static  String emailProvider;
	private static  int emailPort;
	private static String itSupportEmail;
	
//	# Use 1 to read email from DB userLDAPName , 2 to read email for  DB UserEmail Column 
	private static String userEmailSource;
//	# used when read email for  DB UserEmail Column 
	private static String emailDomain ;
	
	// Security
	private static String disableBackendAccessCheck;
	private static String superUserName;
	

	
	static {
		props = new Properties();
		loadProps();
	}
	
	public static void loadProps() {
		try {
			ConnectionManager dbConnection= new ConnectionManager();
			dbConnection.initConn();
			ConfigDAO dao = new ConfigDAO(dbConnection);
			Set<ConfigBean> beans = dao.fetchAllConfigs();
			for (ConfigBean b : beans) {
				props.put(b.getName(), b.getValue());
			}
			
			if (props != null) {

				
				emailName  =  props.getProperty("EMAIL");
				emailPassword =  props.getProperty("EMAIL_PASSWORD");
				emailProvider = props.getProperty("EMAIL_PROVIDER");
				emailPort = Integer.parseInt(props.getProperty("EMAIL_PORT"));
				itSupportEmail = props.getProperty("IT_SUPPORT_EMAIL");
				userEmailSource =  props.getProperty("USER_EMAIL_SOURCE"); 
				emailDomain  = props.getProperty("EMAIL_DOMAIN");
				
				
				disableBackendAccessCheck = props.getProperty("SECURITY_DISABLE_BACKEND_ACCESS_CHECK");
				superUserName = props.getProperty("SECURITY_SUPER_USER_NAME");
				
				
				
				
//				System.out.println("Configurations have been loaded successfully!");
			} else {
				System.err.println("Failed to open the configuration file");
			}
		} catch (SecurityException e) {
			System.err.println("Failed to access resources folder due to the following error: " + e.getMessage());
			e.printStackTrace();
		} catch(DatabaseException e){
			try {
				throw new DatabaseException("Error to open data base connection", e);
			} catch (DatabaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch(Exception e){
			System.err.println("Failed to initiate config due to the following error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
	}

	
	public static String getSuperUserName() {
		return superUserName;
	}
	
	public static boolean isAccessCheckDisabled() {
		return "1".equals(disableBackendAccessCheck);
	}

	public static String getEmailName() {
		return emailName;
	}


	public static String getEmailPassword() {
		return emailPassword;
	}


	public static String getEmailProvider() {
		return emailProvider;
	}


	public static int getEmailPort() {
		return emailPort;
	}


	public static String getUserEmailSource() {
		return userEmailSource;
	}


	public static String getEmailDomain() {
		return emailDomain;
	}


	public static String getItSupportEmail() {
		return itSupportEmail;
	}
	
	
}
