package com.dataserve.pad.util;

import java.util.Properties;
import java.util.Set;

import com.dataserve.pad.bean.ConfigBean;
import com.dataserve.pad.db.dao.ConfigDAO;

public class ConfigManager {
	private static Properties props;
	private static String transferCustomProperty;



	static {
		props = new Properties();
		loadProps();
	}
	
	public static void loadProps() {
		try {
			ConfigDAO dao = new ConfigDAO();
			Set<ConfigBean> beans = dao.fetchAllConfigs();
			for (ConfigBean b : beans) {
				if(b.IsEncrypted() == true) {
					props.put(b.getName(), EncryptionUtil.decrypt(b.getValue()));
				}else {
					props.put(b.getName(), b.getValue());
				}
			}
			System.out.println("Configurations have been loaded successfully!");
		} catch(Exception e){
			System.err.println("Failed to initiate config due to the following error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static String getLdapUserName() {
		return props.getProperty("LDAP_USER_NAME");
	}


	public static String getLdapProviderURL() {
		return props.getProperty("LDAP_PROVIDER_URL");
	}

	public static String getLdapSearchDomain() {
		return props.getProperty("LDAP_SEARCH_DOMAIN");
	}
	
	public static String getLdapSearchAttribute() {
		return props.getProperty("LDAP_SEARCH_ATTRIBUTE");
	}
	
	public static boolean isLdapValidationEnabled() {
		return "1".equals( props.getProperty("LDAP_VALIDATION_ENABLED"));
	}

	public static boolean isLdapSslEnabled() {
		return "1".equals(props.getProperty("LDAP_ENABLE_SSL"));
	}
	
	public static String getDataSourceName() {
		return props.getProperty("ARCHIVE_DATASOURCE_NAME");
	}

	public static String getSuperUserName() {
		return props.getProperty("SECURITY_SUPER_USER_NAME");
	}
	
	public static boolean isAccessCheckDisabled() {
		return "1".equals(props.getProperty("SECURITY_DISABLE_BACKEND_ACCESS_CHECK"));
	}
	
	public static String getKeywordsPropertyName() {
		return props.getProperty("KEYWORDS_PROPERTY_NAME");
	}
	
	public static boolean useTransferCreateDate() {
		return "1".equals(props.getProperty("USE_TRANSFER_CREATE_DATE"));
	}
	
	public static String getTransferCustomProperty() {
		return transferCustomProperty;
	}
	
	public static boolean useDestructionCreateDate() {
		return "1".equals(props.getProperty("USE_DESTRUCTION_CREATE_DATE"));
	}
	
	public static String getDestructionCustomProperty() {
		return props.getProperty("DESTRUCTION_CUSTOM_DATE_PROPERTY");
	}
}
