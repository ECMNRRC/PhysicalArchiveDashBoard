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
    
//    public Set<AuditDataBean> fetchDocFilterByClassData(JSONObject dataObj) throws DatabaseException {
//    	Set<AuditDataBean> beans = new LinkedHashSet<>();
//    	try {
//    		if (dataObj == null && dataObj.isEmpty()) {
//        		stmt = con.prepareStatement(
//        			    "SELECT dbo.USERS.UserArname, dbo.USERS.UserEnName, " + 
//        			    "dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, " + 
//        			    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, dbo.CLASSIFICTIONS.CLASS_EN_NAME, " + 
//        			    "COUNT(dbo.DMS_FILES.FILE_ID) AS FileCount " +  
//        			    "FROM dbo.DMS_AUDIT " +  
//        			    "LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID " +  
//        			    "INNER JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID " +  
//        			    "LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP " +  
//        			    "LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME " +  
//        			    "WHERE dbo.DMS_AUDIT.OPERATION_ID = 7 " + 
//        			    "AND dbo.USERS.UserArname IS NOT NULL " +  
//        			    "AND dbo.USERS.UserEnName IS NOT NULL " +  
//        			    "AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL " +  
//        			    "AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL " +  
//        			    "AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL " +  
//        			    "AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL " +  
//        			    "AND dbo.DMS_AUDIT.USER_ID IS NOT NULL " +  
//        			    "GROUP BY dbo.USERS.UserArname, dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, " +
//        			    "dbo.CLASSIFICTIONS.CLASS_AR_NAME, dbo.CLASSIFICTIONS.CLASS_EN_NAME"
//        			);
//			}else {
//			        StringBuilder queryBuilder = new StringBuilder();
//			        queryBuilder.append("SELECT ")
//			                    .append("[dbo].[USERS].[UserArname], ")
//			                    .append("[dbo].[USERS].[UserEnName], ")
//			                    .append("[dbo].[DEPARTMENTS].[DEPT_AR_NAME], ")
//			                    .append("[dbo].[DEPARTMENTS].[DEPT_EN_NAME], ")
//			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME], ")
//			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_EN_NAME], ")
//			                    .append("COUNT([dbo].[DMS_FILES].[FILE_ID]) AS FileCount ")
//			                    .append("FROM [dbo].[DMS_AUDIT] ")
//			                    .append("LEFT JOIN [dbo].[DMS_FILES] ON [dbo].[DMS_AUDIT].[FILE_ID] = [dbo].[DMS_FILES].[FILE_ID] ")
//			                    .append("INNER JOIN [dbo].[DEPARTMENTS] ON [dbo].[DMS_FILES].[DEPT_ID] = [dbo].[DEPARTMENTS].[DEPT_ID] ")
//			                    .append("LEFT JOIN [dbo].[USERS] ON [dbo].[DMS_AUDIT].[USER_ID] = [dbo].[USERS].[UsernameLDAP] ")
//			                    .append("LEFT JOIN [dbo].[CLASSIFICTIONS] ON [dbo].[DMS_AUDIT].[DOCUMENT_CLASS] = [dbo].[CLASSIFICTIONS].[SYMPOLIC_NAME] ")
//			                    .append("WHERE ")
//			                    .append("[dbo].[DMS_AUDIT].[OPERATION_ID] = 7 ");
//
//			        // Include optional conditions based on input parameters
//			        if (dataObj.get(key) != null) {
//			            queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = ").append(deptId).append(" ");
//			        }
//			        if (usernameLDAP != null && !usernameLDAP.isEmpty()) {
//			            queryBuilder.append("AND dbo.USERS.UsernameLDAP = '").append(usernameLDAP).append("' ");
//			        }
//			        if (symbolicName != null && !symbolicName.isEmpty()) {
//			            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(symbolicName).append("' ");
//			        }
//			        
//			        queryBuilder.append("AND [dbo].[USERS].[UserArname] IS NOT NULL ")
//			                    .append("AND [dbo].[USERS].[UserEnName] IS NOT NULL ")
//			                    .append("AND [dbo].[DEPARTMENTS].[DEPT_AR_NAME] IS NOT NULL ")
//			                    .append("AND [dbo].[DEPARTMENTS].[DEPT_EN_NAME] IS NOT NULL ")
//			                    .append("AND [dbo].[CLASSIFICTIONS].[CLASS_AR_NAME] IS NOT NULL ")
//			                    .append("AND [dbo].[CLASSIFICTIONS].[CLASS_EN_NAME] IS NOT NULL ")
//			                    .append("AND [dbo].[DMS_AUDIT].[USER_ID] IS NOT NULL ")
//			                    .append("GROUP BY ")
//			                    .append("[dbo].[USERS].[UserArname], ")
//			                    .append("[dbo].[USERS].[UserEnName], ")
//			                    .append("[dbo].[DEPARTMENTS].[DEPT_AR_NAME], ")
//			                    .append("[dbo].[DEPARTMENTS].[DEPT_EN_NAME], ")
//			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME], ")
//			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_EN_NAME]");
//
//			        stmt = con.prepareStatement(queryBuilder.toString());
//
//			        rs = stmt.executeQuery();
//				
//			}
//
//    		rs = stmt.executeQuery();
//    		while (rs.next()) {
//    		    String userArName = rs.getNString("UserArname");
//    		    String userEnName = rs.getNString("UserEnName");
//    		    String depNameAr = rs.getNString("DEPT_AR_NAME");
//    		    String depNameEn = rs.getNString("DEPT_EN_NAME");
//    		    String classNameAr = rs.getNString("CLASS_AR_NAME");
//    		    String classNameEn = rs.getNString("CLASS_EN_NAME");
//    		    int fileCount = rs.getInt("FileCount");
//
//    		    // Print the raw values
//    		    System.out.println("UserArName: " + userArName );
//
//
//    		    // Now set them in the bean
//    		    AuditDataBean bean = new AuditDataBean();
//    		    bean.setUserArName(userArName);
//    		    bean.setUserEnName(userEnName);
//    		    bean.setDepNameAr(depNameAr);
//    		    bean.setDepNameEn(depNameEn);
//    		    bean.setClassNameAr(classNameAr);
//    		    bean.setClassNameEn(classNameEn);
//    		    bean.setFileCount(fileCount); // Make sure this is an int in your bean class
//    		    beans.add(bean);
//    		}
//    	} catch (SQLException e) {
//    		throw new DatabaseException("Error fetching record from table DMS_AUDIT", e);
//    	} finally {
//    		// safeClose();
//    		// releaseResources();
//    	}
//    	return beans; // Moved to ensure it always returns the collection, even if empty
//    }
    
    public Set<AuditDataBean> fetchDocFilterByClassData(JSONObject dataObj) throws DatabaseException {
    	Set<AuditDataBean> beans = new LinkedHashSet<>();
    	try {
			
			        StringBuilder queryBuilder = new StringBuilder();
			        queryBuilder.append("SELECT ")
			                    .append("[dbo].[USERS].[UserArname], ")
			                    .append("[dbo].[USERS].[UserEnName], ")
			                    .append("[dbo].[DEPARTMENTS].[DEPT_AR_NAME], ")
			                    .append("[dbo].[DEPARTMENTS].[DEPT_EN_NAME], ")
			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME], ")
			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_EN_NAME], ")
			                    .append("COUNT([dbo].[DMS_FILES].[FILE_ID]) AS FileCount ")
			                    .append("FROM [dbo].[DMS_AUDIT] ")
			                    .append("LEFT JOIN [dbo].[DMS_FILES] ON [dbo].[DMS_AUDIT].[FILE_ID] = [dbo].[DMS_FILES].[FILE_ID] ")
			                    .append("INNER JOIN [dbo].[DEPARTMENTS] ON [dbo].[DMS_FILES].[DEPT_ID] = [dbo].[DEPARTMENTS].[DEPT_ID] ")
			                    .append("LEFT JOIN [dbo].[USERS] ON [dbo].[DMS_AUDIT].[USER_ID] = [dbo].[USERS].[UsernameLDAP] ")
			                    .append("LEFT JOIN [dbo].[CLASSIFICTIONS] ON [dbo].[DMS_AUDIT].[DOCUMENT_CLASS] = [dbo].[CLASSIFICTIONS].[SYMPOLIC_NAME] ")
			                    .append("WHERE ")
			                    .append("[dbo].[DMS_AUDIT].[OPERATION_ID] = 7 ");
						        if (dataObj != null) {
						            String deptId = (String) dataObj.get("departmentId");
						            String usernameLDAP = (String) dataObj.get("employeeId");
						            String symbolicName = (String) dataObj.get("classificationId");
			
						            if (deptId != null) {
						                queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = ").append(deptId).append(" ");
						            }
						            if (usernameLDAP != null && !usernameLDAP.isEmpty()) {
						                queryBuilder.append("AND dbo.USERS.UsernameLDAP = '").append(usernameLDAP).append("' ");
						            }
						            if (symbolicName != null && !symbolicName.isEmpty()) {
						                queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(symbolicName).append("' ");
						            }
						        }
			        
			        queryBuilder.append("AND [dbo].[USERS].[UserArname] IS NOT NULL ")
			                    .append("AND [dbo].[USERS].[UserEnName] IS NOT NULL ")
			                    .append("AND [dbo].[DEPARTMENTS].[DEPT_AR_NAME] IS NOT NULL ")
			                    .append("AND [dbo].[DEPARTMENTS].[DEPT_EN_NAME] IS NOT NULL ")
			                    .append("AND [dbo].[CLASSIFICTIONS].[CLASS_AR_NAME] IS NOT NULL ")
			                    .append("AND [dbo].[CLASSIFICTIONS].[CLASS_EN_NAME] IS NOT NULL ")
			                    .append("AND [dbo].[DMS_AUDIT].[USER_ID] IS NOT NULL ")
			                    .append("GROUP BY ")
			                    .append("[dbo].[USERS].[UserArname], ")
			                    .append("[dbo].[USERS].[UserEnName], ")
			                    .append("[dbo].[DEPARTMENTS].[DEPT_AR_NAME], ")
			                    .append("[dbo].[DEPARTMENTS].[DEPT_EN_NAME], ")
			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME], ")
			                    .append("[dbo].[CLASSIFICTIONS].[CLASS_EN_NAME]");

			        stmt = con.prepareStatement(queryBuilder.toString());

			        rs = stmt.executeQuery();
				
			

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
    


    public Set<AuditDataBean> fetchOperationToDepData(JSONObject dataObj) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN, ")
                        .append("dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME ")
                        .append("FROM dbo.DMS_AUDIT ")
                        .append("LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID ")
                        .append("LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                        .append("LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID ")
                        .append("WHERE dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL ")
                        .append("AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL ")
                        .append("AND dbo.DMS_OPERATION.NAME_AR IS NOT NULL ")
                        .append("AND dbo.DMS_OPERATION.NAME_EN IS NOT NULL ");

            // Append optional conditions based on input parameters
            if (dataObj != null) {
                String deptId = (String) dataObj.get("departmentId");
                String operationId = (String) dataObj.get("operationId");

                if (deptId != null && !deptId.isEmpty()) {
                    queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = ").append(deptId).append(" ");
                }

                if (operationId != null && !operationId.isEmpty()) {
                    queryBuilder.append("AND dbo.DMS_AUDIT.OPERATION_ID = '").append(operationId).append("' ");
                }
            }

            stmt = con.prepareStatement(queryBuilder.toString());
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

    
    public Set<AuditDataBean> fetchOperationForClass(JSONObject dataObjs) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
        	System.out.println("dataObj drba fe talt rsma :" + dataObjs);
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ")
                        .append("dbo.DEPARTMENTS.DEPT_AR_NAME, ")
                        .append("dbo.DEPARTMENTS.DEPT_EN_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_AR_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_EN_NAME, ")
                        .append("dbo.DMS_OPERATION.NAME_AR, ")
                        .append("dbo.DMS_OPERATION.NAME_EN ")
                        .append("FROM dbo.DMS_AUDIT ")
                        .append("LEFT JOIN dbo.DMS_FILES ON dbo.DMS_FILES.FILE_ID = dbo.DMS_AUDIT.FILE_ID ")
                        .append("LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                        .append("LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_OPERATION.OPERATION_ID = dbo.DMS_AUDIT.OPERATION_ID ")
                        .append("LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                        .append("LEFT JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME ")
                        .append("WHERE dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL ")
                        .append("AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL ")
                        .append("AND dbo.CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL ")
                        .append("AND dbo.CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL ");

            if (dataObjs != null) {
                String departmentId = (String) dataObjs.get("departmentId");
                String classificationId = (String) dataObjs.get("classificationId");
                String operationId = (String) dataObjs.get("operationId");

                if (departmentId != null) {
                    queryBuilder.append("AND dbo.DMS_FILES.DEPT_ID = '").append(departmentId).append("' ");
                }
                if (classificationId != null && !classificationId.isEmpty()) {
                    queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(classificationId).append("' ");
                }
                if (operationId != null && !operationId.isEmpty()) {
                    queryBuilder.append("AND dbo.DMS_AUDIT.OPERATION_ID = '").append(operationId).append("' ");
                }

            }
            System.out.println("here is the queryBuilder>> "+queryBuilder.toString());
            stmt = con.prepareStatement(queryBuilder.toString());
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

    
    
    public Set<AuditDataBean> fetchOperationForUser(JSONObject dataObj) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ")
                        .append("dbo.USERS.UserArname, dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME, ")
                        .append("dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN, COUNT(dbo.DMS_AUDIT.OPERATION_ID) AS OperationCount ")
                        .append("FROM dbo.DMS_AUDIT ")
                        .append("LEFT JOIN dbo.DMS_FILES ON dbo.DMS_AUDIT.FILE_ID = dbo.DMS_FILES.FILE_ID ")
                        .append("LEFT JOIN dbo.DEPARTMENTS ON dbo.DMS_FILES.DEPT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                        .append("LEFT JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                        .append("LEFT JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID ")
                        .append("WHERE dbo.USERS.UserArname IS NOT NULL ")
                        .append("AND dbo.USERS.UserEnName IS NOT NULL ")
                        .append("AND dbo.DEPARTMENTS.DEPT_AR_NAME IS NOT NULL ")
                        .append("AND dbo.DEPARTMENTS.DEPT_EN_NAME IS NOT NULL ")
                        .append("AND dbo.DMS_OPERATION.NAME_AR IS NOT NULL ")
                        .append("AND dbo.DMS_OPERATION.NAME_EN IS NOT NULL ")
                        .append("AND dbo.DMS_AUDIT.USER_ID IS NOT NULL ");

            if (dataObj != null) {
                String departmentId = (String) dataObj.get("departmentId");
                String operationId = (String) dataObj.get("operationId");
	            String usernameLDAP = (String) dataObj.get("employeeId");


                if (departmentId != null) {
                    queryBuilder.append("AND dbo.DMS_FILES.DEPT_ID = '").append(departmentId).append("' ");
                }

                if (operationId != null && !operationId.isEmpty()) {
                    queryBuilder.append("AND dbo.DMS_AUDIT.OPERATION_ID = '").append(operationId).append("' ");
                }
	            if (usernameLDAP != null && !usernameLDAP.isEmpty()) {
	                queryBuilder.append("AND dbo.USERS.UsernameLDAP = '").append(usernameLDAP).append("' ");
	            }

            }

            queryBuilder.append("GROUP BY dbo.USERS.UserArname, dbo.USERS.UserEnName, dbo.DEPARTMENTS.DEPT_AR_NAME, ")
                        .append("dbo.DEPARTMENTS.DEPT_EN_NAME, dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN");

            stmt = con.prepareStatement(queryBuilder.toString());
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
    
    public Set<AuditDataBean> fetchFilterData() throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        
        try {
            stmt = con.prepareStatement(
                "SELECT DISTINCT " +
                    "DEPARTMENTS.DEPT_AR_NAME, " +
                    "DEPARTMENTS.DEPT_EN_NAME, " +
                    "DEPARTMENTS.DEPT_ID, " +
                    "CLASSIFICTIONS.CLASS_AR_NAME, " +
                    "CLASSIFICTIONS.CLASS_EN_NAME, " +
                    "CLASSIFICTIONS.SYMPOLIC_NAME, " +
                    "USERS.UserArname, " +
                    "USERS.UserEnName, " +
                    "USERS.UsernameLDAP, " +
                    "DMS_OPERATION.NAME_AR, " +
                    "DMS_OPERATION.NAME_EN, " +
                    "DMS_OPERATION.OPERATION_ID " +
                "FROM " +
                    "DMS_AUDIT " +
                "LEFT JOIN " +
                    "DMS_FILES ON DMS_AUDIT.FILE_ID = DMS_FILES.FILE_ID " +
                "LEFT JOIN " +
                    "DEPARTMENTS ON DMS_FILES.DEPT_ID = DEPARTMENTS.DEPT_ID " +
                "LEFT JOIN " +
                    "CLASSIFICTIONS ON DMS_AUDIT.DOCUMENT_CLASS = CLASSIFICTIONS.SYMPOLIC_NAME " +
                "LEFT JOIN " +
                    "USERS ON DMS_AUDIT.USER_ID = USERS.UsernameLDAP " +
                "LEFT JOIN " +
                    "DMS_OPERATION ON DMS_AUDIT.OPERATION_ID = DMS_OPERATION.OPERATION_ID " +
                "WHERE " +
                    "DEPARTMENTS.DEPT_AR_NAME IS NOT NULL AND " +
                    "DEPARTMENTS.DEPT_EN_NAME IS NOT NULL AND " +
                    "DEPARTMENTS.DEPT_ID IS NOT NULL AND " +
                    "CLASSIFICTIONS.CLASS_AR_NAME IS NOT NULL AND " +
                    "CLASSIFICTIONS.CLASS_EN_NAME IS NOT NULL AND " +
                    "CLASSIFICTIONS.SYMPOLIC_NAME IS NOT NULL AND " +
                    "USERS.UserArname IS NOT NULL AND " +
                    "USERS.UserEnName IS NOT NULL AND " +
                    "USERS.UsernameLDAP IS NOT NULL AND " +
                    "DMS_OPERATION.NAME_AR IS NOT NULL AND " +
                    "DMS_OPERATION.NAME_EN IS NOT NULL AND " +
                    "DMS_OPERATION.OPERATION_ID IS NOT NULL"
            );

            rs = stmt.executeQuery();
            while (rs.next()) {
                AuditDataBean bean = new AuditDataBean();
                bean.setDepNameAr(rs.getString("DEPT_AR_NAME"));
                bean.setDepNameEn(rs.getString("DEPT_EN_NAME"));
                bean.setDepId(rs.getString("DEPT_ID"));
                bean.setClassNameAr(rs.getString("CLASS_AR_NAME"));
                bean.setClassNameEn(rs.getString("CLASS_EN_NAME"));
                bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
                bean.setUserArName(rs.getString("UserArname"));
                bean.setUserEnName(rs.getString("UserEnName"));
                bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
                bean.setOperationNameAr(rs.getString("NAME_AR"));
                bean.setOperationNameEn(rs.getString("NAME_EN"));
                bean.setOperationId(rs.getString("OPERATION_ID"));
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching filtered data", e);
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

       
