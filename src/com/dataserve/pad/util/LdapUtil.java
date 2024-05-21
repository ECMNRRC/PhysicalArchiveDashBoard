package com.dataserve.pad.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.dataserve.pad.business.users.UserException;

public class LdapUtil {

    public static Attributes getUserInfo(String userName) throws UserException {
    	try {
    		InitialDirContext ctx = getContext();
	        NamingEnumeration<SearchResult> answer = ctx.search(
	        		ConfigManager.getLdapSearchDomain(), 
	        		ConfigManager.getLdapSearchAttribute() + "=" + userName, 
	        		getSearchControls());
	        if (answer.hasMore()) {
	            return answer.next().getAttributes();
	        } else {
	            System.err.println("user '" + userName + "' not found.");
	            return null;
	        }
    	} catch (UserException e) {
    		throw new UserException("Error getting ldap connection ldap", e);
    	}catch (Exception e) {
    		throw new UserException("Error looking up user from ldap", e);
    	}
    }

	private static InitialDirContext getContext() throws UserException {
		InitialDirContext ctx = null;
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, ConfigManager.getLdapUserName());
            env.put(Context.SECURITY_CREDENTIALS, ConfigManager.getLdapPassword());
            env.put(Context.PROVIDER_URL, ConfigManager.getLdapProviderURL());
            if (ConfigManager.isLdapSslEnabled()) {
            	env.put(Context.SECURITY_PROTOCOL, "ssl");
            }
            ctx = new InitialDirContext(env);
        } catch (NamingException nex) {
            throw new UserException("LDAP Connection: FAILED", nex);
        }
        return ctx;
    }

    private static SearchControls getSearchControls() {
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrIDs = {"distinguishedName", "sn", "givenname", "mail", "telephonenumber", "sAMAccountName"};
        cons.setReturningAttributes(attrIDs);
        return cons;
    }

}
