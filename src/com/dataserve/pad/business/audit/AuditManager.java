package com.dataserve.pad.business.audit;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.dataserve.pad.bean.AuditBean;

import com.dataserve.pad.db.AuditDao;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class AuditManager {

	
	
	public void logAudit(AuditBean bean) throws AuditException {
		try {
			AuditDao dao = new AuditDao();
			dao.addAuditLog(bean);
		} catch (Exception e) {
			throw new AuditException("Error logging audit for action", e);
		}
	}
	
	
}
