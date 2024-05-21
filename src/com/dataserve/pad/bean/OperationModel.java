package com.dataserve.pad.bean;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.dataserve.pad.bean.OperationBean;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.db.AuditDataDAO; // Consider renaming this DAO if it specifically handles AuditData now.
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.OperationDAO;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class OperationModel {
    private OperationBean bean;
    private OperationModel parent;

    private Set<OperationModel> children = new LinkedHashSet<OperationModel>();

    public OperationModel(OperationBean bean) {
        this.bean = bean;
    }


    public OperationModel() throws DatabaseException {
    	OperationBean bean = new OperationBean();


        this.bean = bean;
    }

    public static Set<OperationModel> getAllOperations() throws ClassificationException {
        try {
            OperationDAO dao = new OperationDAO(); // Consider renaming this DAO if it specifically handles AuditData now.           
            Set<OperationBean> beans = dao.featchAllOperation();
            Set<OperationModel> lmSet = new LinkedHashSet<OperationModel>();
            for (OperationBean b : beans) {
                lmSet.add(new OperationModel(b));
            }
            return lmSet;
        } catch (DatabaseException e) {
            throw new ClassificationException("Error getting Linked Document with Main Doc ", e);
        }
    }
    


    public JSONObject getAsJson() throws ClassificationException {
        JSONObject obj = new JSONObject();
        obj.put("opId", getOperationId());
        obj.put("opNameAr", getOperationNameAr());
        obj.put("opNameEn", getOperationNameEn());
        return obj;
    }

    
 
    public void save() throws ClassificationException {
        try {
        	OperationDAO dao = new OperationDAO(); // Consider renaming this DAO if it specifically handles AuditData now.
//            dao.addLinkDocument(bean);
        } catch (DatabaseException e) {
            throw new ClassificationException("Error happened while trying to save Classification!", e);
        }
    }
    
    
    public String getOperationId() {
    	return bean.getOperationId();
    }
    public void setOperationId(String operationId) {
    	bean.setOperationId(operationId);
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
    










}      
