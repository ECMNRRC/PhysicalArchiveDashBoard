package com.dataserve.pad.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.sql.SQLException;

import com.dataserve.pad.bean.AuditDataBean;
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
                bean.setOperationId(rs.getString("OPERATION_ID")); 
                bean.setUserId(rs.getString("USER_ID")); 
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching record from table DMS_AUDIT", e);
        } finally {
            
        }
        return beans;
    }
    

    
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
                .append("COUNT([dbo].[DMS_AUDIT].[DOCUMENT_CLASS]) AS FileCount ")
                .append("FROM [dbo].[DMS_AUDIT] ")
                .append("INNER JOIN [dbo].[USERS] ON [dbo].[DMS_AUDIT].[USER_ID] = [dbo].[USERS].[UsernameLDAP] ")
                .append("INNER JOIN [dbo].[DEPARTMENTS] ON [dbo].[DEPARTMENTS].[DEPT_ID] = [dbo].[USERS].[DEPARTMENT_ID] ")
                .append("INNER JOIN [dbo].[CLASSIFICTIONS] ON [dbo].[DMS_AUDIT].[DOCUMENT_CLASS] = [dbo].[CLASSIFICTIONS].[SYMPOLIC_NAME] ")
                .append("INNER JOIN [dbo].[DMS_FILES] ON [dbo].[DMS_AUDIT].[FILE_ID] = [dbo].[DMS_FILES].[FILE_ID] ")
                .append("WHERE [dbo].[DMS_AUDIT].[OPERATION_ID] = 7 ");

            if (dataObj != null) {
                String usernameLDAP = (String) dataObj.get("employeeId");
                Object symbolicNameObj = dataObj.get("classificationId");
                String deptId = (String) dataObj.get("departmentId");

                if (deptId != null && !deptId.trim().isEmpty()) {
                    queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = '").append(deptId).append("' ");
                }
                if (usernameLDAP != null && !usernameLDAP.trim().isEmpty()) {
                    queryBuilder.append("AND dbo.USERS.UsernameLDAP = '").append(usernameLDAP).append("' ");
                }
                if (symbolicNameObj != null) {
                    if (symbolicNameObj instanceof JSONArray) {
                        JSONArray symbolicNameArray = (JSONArray) symbolicNameObj;
                        if (!symbolicNameArray.isEmpty()) {
                            StringBuilder symbolicNameBuilder = new StringBuilder();
                            for (int i = 0; i < symbolicNameArray.size(); i++) {
                                if (i > 0) {
                                    symbolicNameBuilder.append(",");
                                }
                                symbolicNameBuilder.append("'").append(symbolicNameArray.get(i)).append("'");
                            }
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME IN (").append(symbolicNameBuilder.toString()).append(") ");
                        }
                    } else if (symbolicNameObj instanceof String) {
                        String symbolicName = (String) symbolicNameObj;
                        if (!symbolicName.trim().isEmpty()) {
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(symbolicName).append("' ");
                        }
                    }
                }
            }

            queryBuilder.append("GROUP BY ")
                .append("[dbo].[USERS].[UserArname], ")
                .append("[dbo].[USERS].[UserEnName], ")
                .append("[dbo].[DEPARTMENTS].[DEPT_AR_NAME], ")
                .append("[dbo].[DEPARTMENTS].[DEPT_EN_NAME], ")
                .append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME], ")
                .append("[dbo].[CLASSIFICTIONS].[CLASS_EN_NAME]");
            

            stmt = con.prepareStatement(queryBuilder.toString());
            rs = stmt.executeQuery();

            while (rs.next()) {
                String userArName = rs.getString("UserArname");
                String userEnName = rs.getString("UserEnName");
                String depNameAr = rs.getString("DEPT_AR_NAME");
                String depNameEn = rs.getString("DEPT_EN_NAME");
                String classNameAr = rs.getString("CLASS_AR_NAME");
                String classNameEn = rs.getString("CLASS_EN_NAME");
                int fileCount = rs.getInt("FileCount");

                AuditDataBean bean = new AuditDataBean();
                bean.setUserArName(userArName);
                bean.setUserEnName(userEnName != null && !userEnName.isEmpty() ? userEnName : userArName);
                bean.setDepNameAr(depNameAr);
                bean.setDepNameEn(depNameEn != null && !depNameEn.isEmpty() ? depNameEn : depNameAr);
                bean.setClassNameAr(classNameAr);
                bean.setClassNameEn(classNameEn != null && !classNameEn.isEmpty() ? classNameEn : classNameAr);
                bean.setFileCount(fileCount);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching record from table DMS_AUDIT", e);
        } finally {

        }
        return beans;
    }



    


    public Set<AuditDataBean> fetchOperationToDepData(JSONObject dataObj) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT dbo.DMS_OPERATION.NAME_AR, dbo.DMS_OPERATION.NAME_EN, ")
                        .append("dbo.DEPARTMENTS.DEPT_AR_NAME, dbo.DEPARTMENTS.DEPT_EN_NAME ")
                        .append("FROM dbo.DMS_AUDIT ")
                        .append("INNER JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                        .append("INNER JOIN dbo.DEPARTMENTS ON dbo.USERS.DEPARTMENT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                        .append("INNER JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID ");

            if (dataObj != null && !dataObj.isEmpty()) {
                queryBuilder.append("WHERE 1=1");

                String operationId = (String) dataObj.get("operationId");
                String departmentId = (String) dataObj.get("departmentId");

                if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(" ")) {
                    queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = ").append(departmentId).append(" ");
                    
                }

                if (operationId != null && !operationId.isEmpty() && !operationId.equals(" ")) {
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

                String operationNameEn = (nameEn != null && !nameEn.isEmpty()) ? nameEn : nameAr;
                String departmentNameEn = (depNameEn != null && !depNameEn.isEmpty()) ? depNameEn : depNameAr;

                AuditDataBean bean = new AuditDataBean();
                bean.setOperationNameAr(nameAr);
                bean.setOperationNameEn(operationNameEn);
                bean.setDepNameAr(depNameAr);
                bean.setDepNameEn(departmentNameEn);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation to department data from table DMS_AUDIT", e);
        } finally {

        }
        return beans;
    }

    public Set<AuditDataBean> fetchOperationForClass(JSONObject dataObjs) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ")
                .append("dbo.DEPARTMENTS.DEPT_AR_NAME, ")
                .append("dbo.DEPARTMENTS.DEPT_EN_NAME, ")
                .append("dbo.CLASSIFICTIONS.CLASS_AR_NAME, ")
                .append("dbo.CLASSIFICTIONS.CLASS_EN_NAME, ")
                .append("dbo.DMS_OPERATION.NAME_AR, ")
                .append("dbo.DMS_OPERATION.NAME_EN ")
                .append("FROM dbo.DMS_AUDIT ")
                .append("INNER JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID ")
                .append("INNER JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                .append("INNER JOIN dbo.DEPARTMENTS ON dbo.USERS.DEPARTMENT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                .append("INNER JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME ");

            if (dataObjs != null) {
                queryBuilder.append("WHERE 1 = 1 ");
                String departmentId = (String) dataObjs.get("departmentId");
                Object classificationIdObj = dataObjs.get("classificationId");
                String operationId = (String) dataObjs.get("operationId");

                if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(" ")) {
                    queryBuilder.append("AND dbo.USERS.DEPARTMENT_ID = '").append(departmentId).append("' ");
                }

                if (classificationIdObj != null) {
                    if (classificationIdObj instanceof JSONArray) {
                        JSONArray classificationIdArray = (JSONArray) classificationIdObj;
                        if (!classificationIdArray.isEmpty()) {
                            StringBuilder classificationIdBuilder = new StringBuilder();
                            for (int i = 0; i < classificationIdArray.size(); i++) {
                                if (i > 0) {
                                    classificationIdBuilder.append(",");
                                }
                                classificationIdBuilder.append("'").append(classificationIdArray.get(i)).append("'");
                            }
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME IN (").append(classificationIdBuilder.toString()).append(") ");
                        }
                    } else if (classificationIdObj instanceof String) {
                        String classificationId = (String) classificationIdObj;
                        if (!classificationId.trim().isEmpty()) {
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(classificationId).append("' ");
                        }
                    }
                }

                if (operationId != null && !operationId.isEmpty() && !operationId.equals(" ")) {
                    queryBuilder.append("AND dbo.DMS_AUDIT.OPERATION_ID = '").append(operationId).append("' ");
                }
            }

            stmt = con.prepareStatement(queryBuilder.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String depNameAr = rs.getNString("DEPT_AR_NAME");
                String depNameEn = rs.getNString("DEPT_EN_NAME");
                String classNameAr = rs.getNString("CLASS_AR_NAME");
                String classNameEn = rs.getNString("CLASS_EN_NAME");
                String operationNameAr = rs.getNString("NAME_AR");
                String operationNameEn = rs.getNString("NAME_EN");

                String departmentNameEn = (depNameEn != null && !depNameEn.isEmpty()) ? depNameEn : depNameAr;
                String clsNameEn = (classNameEn != null && !classNameEn.isEmpty()) ? classNameEn : classNameAr;
                String optNameEn = (operationNameEn != null && !operationNameEn.isEmpty()) ? operationNameEn : operationNameAr;

                AuditDataBean bean = new AuditDataBean();
                bean.setDepNameAr(depNameAr);
                bean.setDepNameEn(departmentNameEn);
                bean.setClassNameAr(classNameAr);
                bean.setClassNameEn(clsNameEn);
                bean.setOperationNameAr(operationNameAr);
                bean.setOperationNameEn(optNameEn);
                beans.add(bean);            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation for class data from table DMS_AUDIT", e);
        } finally {

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
                        .append("INNER JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                        .append("INNER JOIN dbo.DEPARTMENTS ON dbo.USERS.DEPARTMENT_ID = dbo.DEPARTMENTS.DEPT_ID ")
                        .append("INNER JOIN dbo.DMS_OPERATION ON dbo.DMS_AUDIT.OPERATION_ID = dbo.DMS_OPERATION.OPERATION_ID ");

            if (dataObj != null) {
                queryBuilder.append("WHERE 1 = 1 ");

                String departmentId = (String) dataObj.get("departmentId");
                String operationId = (String) dataObj.get("operationId");
                String usernameLDAP = (String) dataObj.get("employeeId");

                if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(" ")) {
                    queryBuilder.append("AND dbo.USERS.DEPARTMENT_ID = '").append(departmentId).append("' ");
                }

                if (operationId != null && !operationId.isEmpty() && !operationId.equals(" ")) {
                    queryBuilder.append("AND dbo.DMS_AUDIT.OPERATION_ID = '").append(operationId).append("' ");
                }

                if (usernameLDAP != null && !usernameLDAP.isEmpty() && !usernameLDAP.equals(" ")) {
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

                String userEnNameAdjusted = (userEnName != null && !userEnName.isEmpty()) ? userEnName : userArname;
                String deptEnNameAdjusted = (deptEnName != null && !deptEnName.isEmpty()) ? deptEnName : deptArName;
                String operationNameEnAdjusted = (operationNameEn != null && !operationNameEn.isEmpty()) ? operationNameEn : operationNameAr;

                AuditDataBean bean = new AuditDataBean();
                bean.setUserArName(userArname);
                bean.setUserEnName(userEnNameAdjusted);
                bean.setDepNameAr(deptArName);
                bean.setDepNameEn(deptEnNameAdjusted);
                bean.setOperationNameAr(operationNameAr);
                bean.setOperationNameEn(operationNameEnAdjusted);
                bean.setOperationCount(operationCount);
                beans.add(bean);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching operation for user data from table DMS_AUDIT", e);
        } finally {

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
            }
        }
        return beans;
    }
    
    
    public Map<String, Integer> fetchElectronicAndArchiveDocCounts() throws DatabaseException {
        Map<String, Integer> counts = new HashMap<>();
        try {
            // Get total documents count
            String totalQuery = "SELECT COUNT(*) AS TotalCount FROM dbo.DMS_FILES";
            stmt = con.prepareStatement(totalQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                counts.put("TotalCount", rs.getInt("TotalCount"));
            }
            rs.close();
            stmt.close();

            // Get archived documents count
            String archivedQuery = "SELECT COUNT(*) AS ArchivedCount FROM dbo.DMS_FILES WHERE FOLDER_ID IS NOT NULL";
            stmt = con.prepareStatement(archivedQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                counts.put("ArchivedCount", rs.getInt("ArchivedCount"));
            }
            rs.close();
            stmt.close();

            // Get electronic documents count
            String electronicQuery = "SELECT COUNT(*) AS ElectronicCount FROM dbo.DMS_FILES WHERE FOLDER_ID IS NULL";
            stmt = con.prepareStatement(electronicQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                counts.put("ElectronicCount", rs.getInt("ElectronicCount"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching document counts", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                // Handle exceptions
            }
        }
        return counts;
    }
    
    
    public Map<String, Integer> fetchKeyWordDocCounts() throws DatabaseException {
        Map<String, Integer> counts = new HashMap<>();
        try {
            String totalQuery = "SELECT COUNT(*) AS TotalCount FROM dbo.DMS_FILES";
            stmt = con.prepareStatement(totalQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                counts.put("TotalCount", rs.getInt("TotalCount"));
            }
            rs.close();
            stmt.close();
            String keywordQuery = "SELECT COUNT(*) as keywordDocCount FROM DMS_FILES "+
                    "INNER JOIN DMS_AUDIT ON DMS_AUDIT.FILE_ID = DMS_FILES.FILE_ID "+
                    "INNER JOIN DMS_PROPERTIES_AUDIT ON DMS_AUDIT.DMS_AUDIT_ID = DMS_PROPERTIES_AUDIT.DMS_AUDIT_ID "+
                    "WHERE DMS_AUDIT.OPERATION_ID IN (7, 4, 3) "+
                    "AND DMS_PROPERTIES_AUDIT.PROPERTY_NAME = (SELECT VALUE FROM CONFIG WHERE NAME = 'KEYWORDS_PROPERTY_NAME')";
            stmt = con.prepareStatement(keywordQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                counts.put("keywordDocCount", rs.getInt("keywordDocCount"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching document counts", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
        }
        return counts;
    }
    
    public List<Map<String, Object>> fetchViewRequestsData() throws DatabaseException {
        List<Map<String, Object>> results = new ArrayList();
        try {
            String query = "SELECT " +
                "SYSL_VIEW_REQUEST_TYPE.NAME_AR AS ViewType, " +
                "SYSL_CORR_STATUS.NAME_AR AS Status, " +
                "COUNT(VIEW_REQUEST_TYPE_ID) AS ViewRequestTypeCount, " +
                "COUNT(CORR_STATUS_ID) AS CorrStatusCount " +
                "FROM req.CORR_DETAILS " +
                "INNER JOIN req.SYSL_CORR_TYPE ON SYSL_CORR_TYPE.ID = CORR_DETAILS.CORR_TYPE_ID " +
                "INNER JOIN req.SYSL_CORR_STATUS ON SYSL_CORR_STATUS.ID = CORR_DETAILS.CORR_STATUS_ID " +
                "INNER JOIN req.SYSL_VIEW_REQUEST_TYPE ON SYSL_VIEW_REQUEST_TYPE.ID = CORR_DETAILS.VIEW_REQUEST_TYPE_ID " +
                "WHERE CORR_DETAILS.CORR_TYPE_ID = 1 " +
                "AND CORR_DETAILS.VIEW_REQUEST_TYPE_ID IN (1, 2) " +
                "AND CORR_DETAILS.CORR_STATUS_ID IN (3, 4, 5) " +
                "GROUP BY SYSL_VIEW_REQUEST_TYPE.NAME_AR, SYSL_CORR_STATUS.NAME_AR";

            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ViewType", rs.getString("ViewType"));
                row.put("Status", rs.getString("Status"));
                row.put("ViewRequestTypeCount", rs.getInt("ViewRequestTypeCount"));
                row.put("CorrStatusCount", rs.getInt("CorrStatusCount"));
                results.add(row);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching view requests data", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                // Handle exceptions
            }
        }
        return results;
    }

    
 // Fetch borrowing requests statistics
    public List<Map<String, Object>> fetchBorrowingRequestsData() throws DatabaseException {
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            // SQL query to fetch borrowing request stats
            String query = "SELECT SYSL_CORR_STATUS.NAME_AR AS Status, COUNT(CORR_STATUS_ID) AS CORR_STATUS_COUNT "
                    + "FROM req.CORR_DETAILS "
                    + "INNER JOIN req.SYSL_CORR_STATUS ON SYSL_CORR_STATUS.ID = CORR_DETAILS.CORR_STATUS_ID "
                    + "WHERE CORR_DETAILS.CORR_TYPE_ID = 2 "
                    + "AND CORR_DETAILS.CORR_STATUS_ID IN (3, 4, 5) "
                    + "GROUP BY SYSL_CORR_STATUS.NAME_AR";

            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            // Loop through the result set
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Status", rs.getString("Status"));
                row.put("CORR_STATUS_COUNT", rs.getInt("CORR_STATUS_COUNT"));
                results.add(row);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching borrowing requests data", e);
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                // Handle exception
            }
        }

        return results;
    }

    
    public Set<AuditDataBean> fetchDocByFilteredDate(String dateTo, String dateFrom, JSONObject dataObj) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();

        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ")
                        .append("dbo.DEPARTMENTS.DEPT_AR_NAME, ")
                        .append("dbo.DEPARTMENTS.DEPT_EN_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_AR_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_EN_NAME, ")
                        .append("DATEPART(week, dbo.DMS_AUDIT.DATE) AS WeekNumber, ")
                        .append("YEAR(dbo.DMS_AUDIT.DATE) AS Year, ")
                        .append("COUNT(*) AS ClassCount ")
                        .append("FROM dbo.DMS_AUDIT ")
                        .append("INNER JOIN dbo.USERS ON dbo.DMS_AUDIT.USER_ID = dbo.USERS.UsernameLDAP ")
                        .append("INNER JOIN dbo.DEPARTMENTS ON dbo.DEPARTMENTS.DEPT_ID = dbo.USERS.DEPARTMENT_ID ")
                        .append("INNER JOIN dbo.CLASSIFICTIONS ON dbo.DMS_AUDIT.DOCUMENT_CLASS = dbo.CLASSIFICTIONS.SYMPOLIC_NAME ")
                        .append("WHERE dbo.DMS_AUDIT.OPERATION_ID = 7 ")
                        .append("AND dbo.DMS_AUDIT.DATE BETWEEN ? AND ? ");

            if (dataObj != null) {
                String departmentId = (String) dataObj.get("departmentId");
                if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(" ")) {
                    queryBuilder.append("AND dbo.DEPARTMENTS.DEPT_ID = '").append(departmentId).append("' ");
                }

                Object classificationIdObj = dataObj.get("classificationId");
                if (classificationIdObj != null) {
                    if (classificationIdObj instanceof JSONArray) {
                        JSONArray classificationIdArray = (JSONArray) classificationIdObj;
                        if (!classificationIdArray.isEmpty()) {
                            StringBuilder classificationIdBuilder = new StringBuilder();
                            for (int i = 0; i < classificationIdArray.size(); i++) {
                                if (i > 0) {
                                    classificationIdBuilder.append(",");
                                }
                                classificationIdBuilder.append("'").append(classificationIdArray.get(i)).append("'");
                            }
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME IN (").append(classificationIdBuilder.toString()).append(") ");
                        }
                    } else if (classificationIdObj instanceof String) {
                        String classificationId = (String) classificationIdObj;
                        if (!classificationId.trim().isEmpty()) {
                            queryBuilder.append("AND dbo.CLASSIFICTIONS.SYMPOLIC_NAME = '").append(classificationId).append("' ");
                        }
                    }
                }
            }

            queryBuilder.append("GROUP BY ")
                        .append("dbo.DEPARTMENTS.DEPT_AR_NAME, ")
                        .append("dbo.DEPARTMENTS.DEPT_EN_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_AR_NAME, ")
                        .append("dbo.CLASSIFICTIONS.CLASS_EN_NAME, ")
                        .append("DATEPART(week, dbo.DMS_AUDIT.DATE), ")
                        .append("YEAR(dbo.DMS_AUDIT.DATE) ")
                        .append("ORDER BY ")
                        .append("YEAR(dbo.DMS_AUDIT.DATE), ")
                        .append("DATEPART(week, dbo.DMS_AUDIT.DATE)");


            stmt = con.prepareStatement(queryBuilder.toString());

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
            }
        }
        return beans;
    }



    
    public Set<AuditDataBean> fetchFilterData(JSONObject dataObj) throws DatabaseException {
        Set<AuditDataBean> beans = new LinkedHashSet<>();
        
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(
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
                "INNER JOIN " +
                    "USERS ON DMS_AUDIT.USER_ID = USERS.UsernameLDAP " +
                "INNER JOIN " +
                    "DEPARTMENTS ON DEPARTMENTS.DEPT_ID = USERS.DEPARTMENT_ID " +
                "INNER JOIN " +
                    "CLASSIFICTIONS ON DMS_AUDIT.DOCUMENT_CLASS = CLASSIFICTIONS.SYMPOLIC_NAME " +
                "INNER JOIN " +
                    "DMS_OPERATION ON DMS_AUDIT.OPERATION_ID = DMS_OPERATION.OPERATION_ID "
            );

            if (dataObj != null) {
                queryBuilder.append("WHERE 1=1 ");

                String departmentId = (String) dataObj.get("departmentId");
                if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(" ")) {
                    queryBuilder.append("AND DEPARTMENTS.DEPT_ID = '").append(departmentId).append("' ");
                }

                Object classificationIdObj = dataObj.get("classificationId");
                if (classificationIdObj != null) {
                    if (classificationIdObj instanceof JSONArray) {
                        JSONArray classificationIdArray = (JSONArray) classificationIdObj;
                        if (!classificationIdArray.isEmpty()) {
                            StringBuilder classificationIdBuilder = new StringBuilder();
                            for (int i = 0; i < classificationIdArray.size(); i++) {
                                if (i > 0) {
                                    classificationIdBuilder.append(",");
                                }
                                classificationIdBuilder.append("'").append(classificationIdArray.get(i)).append("'");
                            }
                            queryBuilder.append("AND CLASSIFICTIONS.SYMPOLIC_NAME IN (").append(classificationIdBuilder.toString()).append(") ");
                        }
                    } else if (classificationIdObj instanceof String) {
                        String classificationId = (String) classificationIdObj;
                        if (!classificationId.trim().isEmpty()) {
                            queryBuilder.append("AND CLASSIFICTIONS.SYMPOLIC_NAME = '").append(classificationId).append("' ");
                        }
                    }
                }
            }

            stmt = con.prepareStatement(queryBuilder.toString());
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
            }
        }
        return beans;
    }


    
}

       
