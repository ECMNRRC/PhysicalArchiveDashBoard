package com.dataserve.pad.db;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import java.sql.SQLException;

import com.dataserve.pad.bean.AuditDataBean;
import com.dataserve.pad.business.classification.ClassificationException;
import com.dataserve.pad.db.DatabaseException;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


public class AuditDataDAO extends AbstractDAO {
    public AuditDataDAO() throws DatabaseException {
        super();
    }


    public Set<AuditDataBean> featchAuditData() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            stmt = con.prepareStatement("SELECT * from DMS_AUDIT");
            rs = stmt.executeQuery();
            while (rs.next()) {
                AuditDataBean bean = new AuditDataBean();
                bean.setDocumentId(rs.getString("DOCUMENT_ID"));
                bean.setFileId(rs.getInt("FILE_ID"));
                bean.setAuditId(rs.getInt("DMS_AUDIT_ID"));
                bean.setOperationId(rs.getString("OPERATION_ID")); // Assuming this is a String
                bean.setUserId(rs.getString("USER_ID")); // Assuming USER_ID is a String
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
    
    public Set<AuditDataBean> fetchDocFilterByClassData() throws DatabaseException {
    	Set<AuditDataBean> beans = new LinkedHashSet<>();
    	try {
    		stmt = con.prepareStatement(
    			    "SELECT dbo.USERS.UserArname, dbo.USERS.UserEnName, " + 
    			    "dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, " + 
    			    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, dbo.CLASSIFICTIONS.CLASS_EN_NAME, " + 
    			    "COUNT(dbo.DMS_FILES.FILE_ID) AS FileCount " +  
    			    "FROM dbo.DMS_AUDIT " +  
    			    "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " +  
    			    "INNER JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +  
    			    "LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP " +  
    			    "LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME " +  
    			    "WHERE dbo.DMS_AUDIT.OPERATION_ID = 7 " + 
    			    "AND dbo.USERS.UserArname IS NOT NULL " +  
    			    "AND dbo.USERS.UserEnName IS NOT NULL " +  
    			    "AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +  
    			    "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +  
    			    "AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL " +  
    			    "AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL " +  
    			    "AND dbo.DMS_AUDIT.USER_ID IS NOT NULL " +  
    			    "GROUP BY dbo.USERS.UserArname, dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, " +
    			    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, dbo.CLASSIFICTIONS.CLASS_EN_NAME"
    			);
    		rs = stmt.executeQuery();
    		while (rs.next()) {
    		    String userArName = rs.getNString("UserArname");
    		    String userEnName = rs.getNString("UserEnName");
    		    String depNameAr = rs.getNString("DEPT_AR_NAME");
    		    String depNameEn = rs.getNString("DEPT_EN_NAME");
    		    String classNameAr = rs.getNString("CLASS_AR_NAME");
    		    String classNameEn = rs.getNString("CLASS_EN_NAME");
    		    int fileCount = rs.getInt("FileCount");

    		    // Print the raw values
    		    System.out.println("UserArName: " + userArName );


    		    // Now set them in the bean
    		    AuditDataBean bean = new AuditDataBean();
    		    bean.setUserArName(userArName);
    		    bean.setUserEnName(userEnName);
    		    bean.setDepNameAr(depNameAr);
    		    bean.setDepNameEn(depNameEn);
    		    bean.setClassNameAr(classNameAr);
    		    bean.setClassNameEn(classNameEn);
    		    bean.setFileCount(fileCount); // Make sure this is an int in your bean class
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


    public Set<AuditDataBean> fetchOperationToDepData() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            stmt = con.prepareStatement(
                "SELECT dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN, " + 
                "dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME " + 
                "FROM dbo.DMS_AUDIT " + 
                "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " + 
                "LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " + 
                "LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID " + 
                "WHERE dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " + 
                "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " + 
                "AND dbo.DMS_OPERATION.NAME_AR IS NOT NULL " + 
                "AND dbo.DMS_OPERATION.NAME_EN IS NOT NULL " 
            );
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nameAr = rs.getNString("NAME_AR");
                String nameEn = rs.getNString("NAME_EN");
                String depNameAr = rs.getNString("DEPT_AR_NAME");
                String depNameEn = rs.getNString("DEPT_EN_NAME");

                // Now set them in the bean
                AuditDataBean bean = new AuditDataBean();
                bean.setOperationNameAr(nameAr);
                bean.setOperationNameEn(nameEn);
                bean.setDepNameAr(depNameAr);
                bean.setDepNameEn(depNameEn);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation to department data from table DMS_AUDIT", e);
        } finally {
            // It's a good practice to close your resources in a finally block
            // safeClose(stmt, rs); // Assuming you have a method `safeClose` that closes both the statement and result set
            // closeConnection(con); // Close connection if it's not managed outside this method
        }
        return beans;
    }
    
    
    public Set<AuditDataBean> fetchOperationForClass() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            stmt = con.prepareStatement(
                "SELECT " + 
                "dbo.DEPARTMENTS.DEPT_AR_NAME, " +
                "dbo.DEPARTMENTS.DEPT_EN_NAME, " +
                "dbo.CLASSIFICTIONS.CLASS_AR_NAME, " +
                "dbo.CLASSIFICTIONS.CLASS_EN_NAME, " +
                "dbo.DMS_OPERATION.NAME_AR, " +
                "dbo.DMS_OPERATION.NAME_EN " +
                "FROM dbo.DMS_AUDIT " +
                "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_FILES.FILE_ID = dbo.DMS_AUDIT.FILE_ID " +
                "LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +
                "LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_OPERATION.OPERATION_ID = dbo.DMS_AUDIT.OPERATION_ID " +
                "LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP " +
                "LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME " +
                "WHERE dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +
                "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +
                "AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL " +
                "AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL " 
            );
            rs = stmt.executeQuery();
            while (rs.next()) {
                String depNameAr = rs.getNString("DEPT_AR_NAME");
                String depNameEn = rs.getNString("DEPT_EN_NAME");
                String classNameAr = rs.getNString("CLASS_AR_NAME");
                String classNameEn = rs.getNString("CLASS_EN_NAME");
                String operationNameAr = rs.getNString("NAME_AR");
                String operationNameEn = rs.getNString("NAME_EN");

                // Now set them in the bean
                AuditDataBean bean = new AuditDataBean();
                bean.setDepNameAr(depNameAr);
                bean.setDepNameEn(depNameEn);
                bean.setClassNameAr(classNameAr);
                bean.setClassNameEn(classNameEn);
                bean.setOperationNameAr(operationNameAr);
                bean.setOperationNameEn(operationNameEn);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation for class data from table DMS_AUDIT", e);
        } finally {
            // It's a good practice to close your resources in a finally block
            // safeClose(stmt, rs); // Assuming you have a method `safeClose` that closes both the statement and result set
            // closeConnection(con); // Close connection if it's not managed outside this method
        }
        return beans;
    }
    
    
    public Set<AuditDataBean> fetchOperationForUser() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            stmt = con.prepareStatement(
                "SELECT " + 
                "dbo.USERS.UserArname,dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, " +
                "dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN, COUNT(dbo.DMS_AUDIT.OPERATION_ID) AS OperationCount " +
                "FROM dbo.DMS_AUDIT " +
                "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " +
                "LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +
                "LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP " +
                "LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID " +
                "WHERE dbo.USERS.UserArname IS NOT NULL " +
                "AND dbo.USERS.UserEnName IS NOT NULL " +
                "AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +
                "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +
                "AND dbo.DMS_OPERATION.NAME_AR IS NOT NULL " +
                "AND dbo.DMS_OPERATION.NAME_EN IS NOT NULL " +
                "AND dbo.DMS_AUDIT.USER_ID IS NOT NULL " +
                "GROUP BY dbo.USERS.UserArname, dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, " +
                "dbo.DEPARTMENTS.DEPT_EN_NAME, dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN"
            );
            rs = stmt.executeQuery();
            while (rs.next()) {
                String userArname = rs.getNString("UserArname");
                String userEnName = rs.getNString("UserEnName");
                String deptArName = rs.getNString("DEPT_AR_NAME");
                String deptEnName = rs.getNString("DEPT_EN_NAME");
                String operationNameAr = rs.getNString("NAME_AR");
                String operationNameEn = rs.getNString("NAME_EN");
                int operationCount = rs.getInt("OperationCount");

                // Now set them in the bean
                AuditDataBean bean = new AuditDataBean();
                bean.setUserArName(userArname);
                bean.setUserEnName(userEnName);
                bean.setDepNameAr(deptArName);
                bean.setDepNameEn(deptEnName);
                bean.setOperationNameAr(operationNameAr);
                bean.setOperationNameEn(operationNameEn);
                bean.setOperationCount(operationCount);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation for user data from table DMS_AUDIT", e);
        } finally {
            // It's a good practice to close your resources in a finally block
            // safeClose(stmt, rs); // Assuming you have a method `safeClose` that closes both the statement and result set
            // closeConnection(con); // Close connection if it's not managed outside this method
        }
        return beans;
}
    
    public Set<AuditDataBean> fetchDocByData() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();

        try {
        	stmt = con.prepareStatement(
        		    "SELECT " +
        		    "dbo.DEPARTMENTS.DEPT_AR_NAME, " +
        		    "dbo.DEPARTMENTS.DEPT_EN_NAME, " +
        		    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, " +
        		    "dbo.CLASSIFICTIONS.CLASS_EN_NAME, " +
        		    "DATEPART(week, dbo.DMS_AUDIT.DATE) AS WeekNumber, " +
        		    "YEAR(dbo.DMS_AUDIT.DATE) AS Year, " +
        		    "COUNT(*) AS ClassCount " +
        		    "FROM dbo.DMS_AUDIT " +
        		    "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " +
        		    "LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +
        		    "LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME " +
        		    "WHERE dbo.DMS_AUDIT.OPERATION_ID = 7 " +
        		    "AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +
        		    "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +
        		    "AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL " +
        		    "AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL " +
        		    "GROUP BY " +
        		    "dbo.DEPARTMENTS.DEPT_AR_NAME, " +
        		    "dbo.DEPARTMENTS.DEPT_EN_NAME, " +
        		    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, " +
        		    "dbo.CLASSIFICTIONS.CLASS_EN_NAME, " +
        		    "DATEPART(week, dbo.DMS_AUDIT.DATE), " +
        		    "YEAR(dbo.DMS_AUDIT.DATE) " +
        		    "ORDER BY " +
        		    "YEAR(dbo.DMS_AUDIT.DATE), " +
        		    "DATEPART(week, dbo.DMS_AUDIT.DATE)"
        		);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String deptArName = rs.getNString("DEPT_AR_NAME");
                String deptEnName = rs.getNString("DEPT_EN_NAME");
                String classArName = rs.getNString("CLASS_AR_NAME");
                String classEnName = rs.getNString("CLASS_EN_NAME");
                int weekNumber = rs.getInt("WeekNumber");
                int year = rs.getInt("Year");
                int classCount = rs.getInt("ClassCount");

                AuditDataBean bean = new AuditDataBean();
                bean.setDepNameAr(deptArName);
                bean.setDepNameEn(deptEnName);
                bean.setClassNameAr(classArName);
                bean.setClassNameEn(classEnName);
                bean.setWeekNumber(weekNumber);
                bean.setYear(year);
                bean.setClassCount(classCount);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching document data", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                // Log this error or handle it as you see fit
            }
        }
        return beans;
    }
    
    
    public Set<AuditDataBean> fetchDocByFilteredDate(String dateTo, String dateFrom) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        
        try {
            stmt = con.prepareStatement(
                    "SELECT " +
                            "dbo.DEPARTMENTS.DEPT_AR_NAME, " +
                            "dbo.DEPARTMENTS.DEPT_EN_NAME, " +
                            "dbo.CLASSIFICTIONS.CLASS_AR_NAME, " +
                            "dbo.CLASSIFICTIONS.CLASS_EN_NAME, " +
                            "DATEPART(week, dbo.DMS_AUDIT.DATE) AS WeekNumber, " +
                            "YEAR(dbo.DMS_AUDIT.DATE) AS Year, " +
                            "COUNT(*) AS ClassCount " +
                            "FROM dbo.DMS_AUDIT " +
                            "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " +
                            "LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +
                            "LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME " +
                            "WHERE dbo.DMS_AUDIT.OPERATION_ID = 7 " +
                            "AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +
                            "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +
                            "AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL " +
                            "AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL " +
                            "AND dbo.DMS_AUDIT.DATE BETWEEN ? AND ? " +
                            "GROUP BY " +
                            "dbo.DEPARTMENTS.DEPT_AR_NAME, " +
                            "dbo.DEPARTMENTS.DEPT_EN_NAME, " +
                            "dbo.CLASSIFICTIONS.CLASS_AR_NAME, " +
                            "dbo.CLASSIFICTIONS.CLASS_EN_NAME, " +
                            "DATEPART(week, dbo.DMS_AUDIT.DATE), " +
                            "YEAR(dbo.DMS_AUDIT.DATE) " +
                            "ORDER BY " +
                            "YEAR(dbo.DMS_AUDIT.DATE), " +
                            "DATEPART(week, dbo.DMS_AUDIT.DATE)"
                    );

            // Set the parameters for the date range
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                String deptArName = rs.getNString("DEPT_AR_NAME");
                String deptEnName = rs.getNString("DEPT_EN_NAME");
                String classArName = rs.getNString("CLASS_AR_NAME");
                String classEnName = rs.getNString("CLASS_EN_NAME");
                int weekNumber = rs.getInt("WeekNumber");
                int year = rs.getInt("Year");
                int classCount = rs.getInt("ClassCount");
                
                AuditDataBean bean = new AuditDataBean();
                bean.setDepNameAr(deptArName);
                bean.setDepNameEn(deptEnName);
                bean.setClassNameAr(classArName);
                bean.setClassNameEn(classEnName);
                bean.setWeekNumber(weekNumber);
                bean.setYear(year);
                bean.setClassCount(classCount);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching document data", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                // Log this error or handle it as you see fit
            }
        }
        return beans;
    }
    
}

       
