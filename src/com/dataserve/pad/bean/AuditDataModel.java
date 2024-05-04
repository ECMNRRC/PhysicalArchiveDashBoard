package com.dataserve.pad.bean;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.dataserve.pad.bean.AuditDataBean;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.db.AuditDataDAO; // Consider renaming this DAO if it specifically handles AuditData now.
import com.dataserve.pad.db.DatabaseException;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class AuditDataModel {
    private AuditDataBean bean;
    private AuditDataModel parent;

    private Set<AuditDataModel> children = new LinkedHashSet<AuditDataModel>();

    public AuditDataModel(AuditDataBean bean) {
        this.bean = bean;
    }


    public AuditDataModel() throws DatabaseException {
        AuditDataBean bean = new AuditDataBean();


        this.bean = bean;
    }

    public static Set<AuditDataModel> getDocFilterByClass() throws ClassificationException {
        try {
            AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
            Set<AuditDataBean> beans = dao.fetchDocFilterByClassData();
            Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
            for (AuditDataBean b : beans) {
                lmSet.add(new AuditDataModel(b));
            }
            return lmSet;
        } catch (DatabaseException e) {
            throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
        }
    }
    
	public static Set<AuditDataModel> getOperationToDep() throws ClassificationException {
        try {
            AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
            Set<AuditDataBean> beans = dao.fetchOperationToDepData();
            Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
            for (AuditDataBean b : beans) {
                lmSet.add(new AuditDataModel(b));
            }
            return lmSet;
        } catch (DatabaseException e) {
            throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
        }
	}
    
    public static Set<AuditDataModel> getAuditData() throws ClassificationException {
        try {
            AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
            Set<AuditDataBean> beans = dao.featchAuditData();
            Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
            for (AuditDataBean b : beans) {
                lmSet.add(new AuditDataModel(b));
            }
            return lmSet;
        } catch (DatabaseException e) {
            throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
        }
    } 
    
    public static Set<AuditDataModel> getOperationForClass() throws ClassificationException {
    	try {
    		AuditDataDAO dao = new AuditDataDAO();           
    		Set<AuditDataBean> beans = dao.fetchOperationForClass();
    		Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
    		for (AuditDataBean b : beans) {
    			lmSet.add(new AuditDataModel(b));
    		}
    		return lmSet;
    	} catch (DatabaseException e) {
    		throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
    	}
    }
    
    public static Set<AuditDataModel> getOperationForUser() throws ClassificationException {
    	try {
    		AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
    		Set<AuditDataBean> beans = dao.fetchOperationForUser();
    		Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
    		for (AuditDataBean b : beans) {
    			lmSet.add(new AuditDataModel(b));
    		}
    		return lmSet;
    	} catch (DatabaseException e) {
    		throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
    	}
    }
    
    public static Set<AuditDataModel> getDocByDate() throws ClassificationException {
    	try {
    		AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
    		Set<AuditDataBean> beans = dao.fetchDocByData();
    		Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
    		for (AuditDataBean b : beans) {
    			lmSet.add(new AuditDataModel(b));
    		}
    		return lmSet;
    	} catch (DatabaseException e) {
    		throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
    	}
    }
    
    public static Set<AuditDataModel> getDocByFilteredDate(String dateTo, String DateFrom) throws ClassificationException {
    	try {
    		AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
    		Set<AuditDataBean> beans = dao.fetchDocByFilteredDate(dateTo, DateFrom);
    		Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
    		for (AuditDataBean b : beans) {
    			lmSet.add(new AuditDataModel(b));
    		}
    		return lmSet;
    	} catch (DatabaseException e) {
    		throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
    	}
    }
    
    public static Set<AuditDataModel> getFilterData() throws ClassificationException {
    	try {
    		AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
    		Set<AuditDataBean> beans = dao.fetchFilterData();
    		Set<AuditDataModel> lmSet = new LinkedHashSet<AuditDataModel>();
    		for (AuditDataBean b : beans) {
    			lmSet.add(new AuditDataModel(b));
    		}
    		return lmSet;
    	} catch (DatabaseException e) {
    		throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
    	}
    }

    public JSONObject getAsJson() throws ClassificationException {
        JSONObject obj = new JSONObject();
        obj.put("userNameAr", getUserArName());
        obj.put("userNameEn", getUserEnName());
        obj.put("depNameAr", getDepNameAr());
        obj.put("depNameEn", getDepNameEn());
        obj.put("classNameAr", getClassNameAr());
        obj.put("classNameEn", getClasNameEn());
        obj.put("fileCount", getFileCount());
        return obj;
    }

    
    public JSONObject getOperationToDepJson() throws ClassificationException {
        JSONObject obj = new JSONObject();
        obj.put("depNameAr", getDepNameAr());
        obj.put("depNameEn", getDepNameEn());
        obj.put("operationNameAr", getOperationNameAr());
        obj.put("operationNameEn", getOperationNameEn());
        return obj;
    }
    
    public JSONObject getOperationForClassJson() throws ClassificationException {
    	JSONObject obj = new JSONObject();
    	obj.put("depNameAr", getDepNameAr());
    	obj.put("depNameEn", getDepNameEn());
        obj.put("classNameAr", getClassNameAr());
        obj.put("classNameEn", getClasNameEn());
    	obj.put("operationNameAr", getOperationNameAr());
    	obj.put("operationNameEn", getOperationNameEn());
    	return obj;
    }
    
    public JSONObject getOperationForUserJson() throws ClassificationException {
    	JSONObject obj = new JSONObject();
        obj.put("userNameAr", getUserArName());
        obj.put("userNameEn", getUserEnName());
        obj.put("depNameAr", getDepNameAr());
        obj.put("depNameEn", getDepNameEn());
        obj.put("operationNameAr", getOperationNameAr());
        obj.put("operationNameEn", getOperationNameEn());
        obj.put("operationCount", getOperationCount());
    	return obj;
    }
    
    public JSONObject getDocByDateJson() throws ClassificationException {
    	JSONObject obj = new JSONObject();
        obj.put("depNameAr", getDepNameAr());
        obj.put("depNameEn", getDepNameEn());
        obj.put("classNameAr", getClassNameAr());
        obj.put("classNameEn", getClasNameEn());
        obj.put("weekNum", getWeekNumber());
        obj.put("year", getYear());
        obj.put("classCount", getClassCount());
    	return obj;
    }
    
    public JSONObject getFilterDataJson() throws ClassificationException {
    	JSONObject obj = new JSONObject();
    	obj.put("depNameAr", getDepNameAr());
    	obj.put("depNameEn", getDepNameEn());
    	obj.put("classNameAr", getClassNameAr());
    	obj.put("classNameEn", getClasNameEn());
        obj.put("userNameAr", getUserArName());
        obj.put("userNameEn", getUserEnName());
        obj.put("operationNameAr", getOperationNameAr());
        obj.put("operationNameEn", getOperationNameEn());
    	return obj;
    }
    
    
    public void save() throws ClassificationException {
        try {
        	AuditDataDAO dao = new AuditDataDAO(); // Consider renaming this DAO if it specifically handles AuditData now.
//            dao.addLinkDocument(bean);
        } catch (DatabaseException e) {
            throw new ClassificationException("Error happened while trying to save Classification!", e);
        }
    }
    
    
    public String getDocumentId() {
    	return bean.getDocumentId();
    }
    public void setDocumentId(String documentId) {
    	bean.setDocumentId(documentId);
    }
    
    public int getFileId() {
    	return bean.getFileId();
    }
    public void setFileId(int fileId) {
    	bean.setFileId(fileId);
    }

    public int getAuditId() {
    	return bean.getAuditId();
    }
    public void setAuditId(int auditId) {
    	bean.setAuditId(auditId);;
    }
    public String getUserId() {
    	return bean.getUserId();
    }
    public void setUserId(String userId) {
    	bean.setUserId(userId);;
    }
    public String getOperationId() {
    	return bean.getOperationId();
    }
    public void setOperationId(String operationId) {
    	bean.setOperationId(operationId);
    }
    
    public String getUserArName() {
    	return bean.getUserArName();
    }
    public void setUserArName(String userArName) {
    	bean.setUserArName(userArName);
    }
    
    public String getUserEnName() {
    	return bean.getUserEnName();
    }
    public void setUserEnName(String userEnName) {
    	bean.setUserEnName(userEnName);
    }
    
    public String getDepNameEn() {
    	return bean.getDepNameEn();
    }
    public void setDepNameEn(String depNameEn) {
    	bean.setDepNameEn(depNameEn);
    }
    
    public String getDepNameAr() {
    	return bean.getDepNameAr();
    }
    public void setDepNameAr(String depNameAr) {
    	bean.setDepNameAr(depNameAr);
    }
    
    public String getClassNameAr() {
    	return bean.getClassNameAr();
    }
    public void setClassNameAr(String classNameAr) {
    	bean.setClassNameAr(classNameAr);
    }
    
    public String getClasNameEn() {
    	return bean.getClassNameEn();
    }
    public void setClasNameEn(String classNameEn) {
    	bean.setClassNameEn(classNameEn);
    }
    
    public String getOperationNameEn() {
    	return bean.getOperationNameEn();
    }
    public void setOperationNameEn(String operationNameEn) {
    	bean.setOperationNameEn(operationNameEn);
    }
    
    public String getOperationNameAr() {
    	return bean.getOperationNameAr();
    }
    public void setOperationNameAr(String operationNameAr) {
    	bean.setOperationNameAr(operationNameAr);
    }
    
    public Integer getFileCount() {
    	return bean.getFileCount();
    }
    public void setFileCount(Integer fileCount) {
    	bean.setFileCount(fileCount);
    }
    
    public Integer getOperationCount() {
    	return bean.getOperationCount();
    }
    public void setOperationCount(Integer operationCount) {
    	bean.setOperationCount(operationCount);
    }

    public Integer getWeekNumber() {
    	return bean.getWeekNumber();
    }
    public void setWeekNumber(Integer weekNumber) {
    	bean.setWeekNumber(weekNumber);
    }
    
    public Integer getYear() {
    	return bean.getYear();
    }
    public void setYear(Integer year) {
    	bean.setYear(year);
    }
    
    public Integer getClassCount() {
    	return bean.getClassCount();
    }
    public void setClassCount(Integer classCount) {
    	bean.setClassCount(classCount);
    }
    
    public String getClasDate() {
    	return bean.getClasDate();
    }
    public void setClasDate(String clasDate) {
    	bean.setClasDate(clasDate);
    }








}      
