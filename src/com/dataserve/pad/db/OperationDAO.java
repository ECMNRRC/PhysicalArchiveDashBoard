package com.dataserve.pad.db;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import java.sql.SQLException;

import com.dataserve.pad.bean.OperationBean;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.db.DatabaseException;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


public class OperationDAO extends AbstractDAO {
    public OperationDAO() throws DatabaseException {
        super();
    }


    public Set<OperationBean> featchAllOperation() throws DatabaseException {
        Set<OperationBean> beans = new LinkedHashSet<>();
        try {
            stmt = con.prepareStatement("select NAME_AR, NAME_EN, OPERATION_ID FROM dbo.DMS_OPERATION");
            rs = stmt.executeQuery();
            while (rs.next()) {
            	OperationBean bean = new OperationBean();
                bean.setOperationId(rs.getString("OPERATION_ID"));
                bean.setOperationNameAr(rs.getString("NAME_AR"));
                bean.setOperationNameEn(rs.getString("NAME_EN"));
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching record from table DMS_AUDIT", e);
        } finally {
            // safeClose();
            // releaseResources();
        }
        return beans; // Moved to ensure it always returns the collection, even if empty
    }
    

    
   

    
}

       
