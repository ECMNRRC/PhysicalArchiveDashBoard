package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dataserve.pad.bean.Classifiction;
import com.dataserve.pad.bean.DMSTransferStatus;
import com.dataserve.pad.bean.Department;
import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.util.ConfigManager;
import com.dataserve.pad.util.DateUtil;

public class TransferFilesDAO {
	ConnectionManager dbConnection = null;

	public TransferFilesDAO(ConnectionManager dbConnection) {
		super();
		this.dbConnection = dbConnection;
	}
	
//	public Set<DmsFiles> getArchiveCenterTransferReadyFiles() throws DatabaseException {
//	    PreparedStatement stmt = null;
//	    ResultSet rs = null;
//	    try {
//	        String SQL = " SELECT  "       
//	                + " DMS_FILES.DOCUMENT_ID, DMS_FILES.FILE_ID, DMS_FILES.FOLDER_ID, DMS_FILES.DOCUMENT_CLASS, DMS_FILES.DEPT_ID, "
//	                + "DMS_FILES.USER_ID,  DMS_FILES.NO_PAGES, " 
//	                
//	                + " DMS_FILES.CREATED_DATE AS CREATE_DATE , DMS_FILES.MODIFIED_DATE , DMS_FILES.DOCUMENT_NAME , "
//	                
//	                + " DMS_FILES.ARCHIVE_CENTER_TRANSFER_EXTENSION_MONTHS , DMS_FILES.ARCHIVE_CENTER_TRANSFER_EXTENSION_REASON ,  "
//	                
//	                + " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME,  "
//	                + " USERS.UserArname, USERS.UserEnName,  "
//	                + " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " 
//	                + " CLASS_DEPT.SAVE_PERIOD,  " 
//	                + " DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID , DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN ,"
//	                + " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO"
//	                + " FROM "   
//	                + " dbo.DMS_FILES "
//	                + " INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
//	                + " LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES.FOLDER_ID"
//	                + " LEFT JOIN dbo.BOX on BOX.BOX_ID = FOLDER.BOX_ID"
//	                + " INNER JOIN dbo.CLASS_DEPT on CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " 
//	                + " and CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
//	                + " INNER JOIN dbo.DEPARTMENTS on DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
//	                + " INNER JOIN dbo.USERS on USERS.USER_ID = DMS_FILES.USER_ID "
//	                + " LEFT JOIN dbo.DMS_TRANSFER_STATUS on  DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES.TRANSFER_STATUS_ID "
//	                + " WHERE  "
//	                + "  ( DMS_FILES.TRANSFER_STATUS_ID is null or DMS_FILES.TRANSFER_STATUS_ID  in ( 1, 5 , 6, 7)  )"
//	                + " AND"
//	                + " DMS_FILES.ARCHIVE_CENTER_STATMENT_ID IS NULL " 
//	                + " ORDER BY DEPARTMENTS.DEPT_ID ";
//	                
//	        stmt = dbConnection.getCon().prepareStatement(SQL);
//	        rs = stmt.executeQuery();
//	        
//	        Set<DmsFiles> dmsFiles = new HashSet<>();
//	        int totalCount = 0;  // Initialize total document counter
//	        
//	        while (rs.next()) {
//	            DmsFiles file = null;
//	            boolean canTransferFile = false;
//	            int savePeriod = rs.getInt("SAVE_PERIOD");
//	            int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();
//	            
//	            // Filter based on TransferCreateDate setting
//	            if (ConfigManager.useTransferCreateDate()) {
//	                int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
//	                if (currentHijriYear >= modifiedYearHijri + savePeriod) {
//	                    canTransferFile = true;        
//	                }
//	            } else {
//	                int customHijrYear = getCustomPopertyDateHijriYear(rs.getInt("FILE_ID"), ConfigManager.getTransferCustomProperty());
//	                if (customHijrYear > 0 && (currentHijriYear >= customHijrYear + savePeriod)) {
//	                    canTransferFile = true;
//	                }
//	            }
//	            
//	            // Only add file if it can be transferred
//	            if (canTransferFile) {
//	                file = new DmsFiles();
//	                file.setFileId(rs.getInt("FILE_ID"));
//	                file.setDocumentId(rs.getString("DOCUMENT_ID"));
//	                file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
//	                file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
//	                file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
//	                file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
//	                file.setFolerNo(rs.getInt("FOLDER_NO"));
//	                file.setBoxNo(rs.getInt("BOX_NO"));
//	                file.setNoPages(rs.getInt("NO_PAGES"));
//	                LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
//	                file.setCreateYearHijri(muslimDate.getYear() + "");
//	                file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear() + "");
//	                
//	                file.setArchiveCenterTransferExtensionMonths(rs.getInt("ARCHIVE_CENTER_TRANSFER_EXTENSION_MONTHS"));
//	                file.setArchiveCenterTransferExtensionReason(rs.getString("ARCHIVE_CENTER_TRANSFER_EXTENSION_REASON"));
//	                
//	                Classifiction classifiction = new Classifiction();
//	                classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
//	                classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
//	                classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
//	                
//	                file.setClassifiction(classifiction);
//	                
//	                Department department = new Department();
//	                department.setDeptArName(rs.getString("DEPT_AR_NAME"));
//	                department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
//	                
//	                file.setDepartment(department);
//	                
//	                DMSTransferStatus transferStatus = new DMSTransferStatus();
//	                transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
//	                transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
//	                transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
//	                file.setTransferStatus(transferStatus);
//	                
//	                dmsFiles.add(file);  // Add file to set
//	                totalCount++;  // Increment total count for transferred documents
//	            }
//	        }
//	        
//	        System.out.println("Total documents ready for transfer: " + totalCount);  // Log total count
//	        return dmsFiles;
//	        
//	    } catch (SQLException e) {
//	        throw new DatabaseException("Error getArchiveCenterTransferReadyFiles", e);
//	    } finally {
//	        try {
//	            if (rs != null) {
//	                rs.close();
//	            }
//	            if (stmt != null) {
//	                stmt.close();
//	            }
//	        } catch (SQLException unexpected) {
//	            throw new DatabaseException("Error closing resources", unexpected);
//	        }
//	    }
//	}

	public Set<DmsFiles> getArchiveCenterTransferReadyFiles(String departmentName) throws DatabaseException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        StringBuilder SQL = new StringBuilder(
	            "SELECT "       
	            + " DMS_FILES.DOCUMENT_ID, DMS_FILES.FILE_ID, DMS_FILES.FOLDER_ID, DMS_FILES.DOCUMENT_CLASS, DMS_FILES.DEPT_ID, "
	            + " DMS_FILES.USER_ID, DMS_FILES.NO_PAGES, " 
	            + " DMS_FILES.CREATED_DATE AS CREATE_DATE, DMS_FILES.MODIFIED_DATE, DMS_FILES.DOCUMENT_NAME, "
	            + " DMS_FILES.ARCHIVE_CENTER_TRANSFER_EXTENSION_MONTHS, DMS_FILES.ARCHIVE_CENTER_TRANSFER_EXTENSION_REASON, "
	            + " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME, "
	            + " USERS.UserArname, USERS.UserEnName, "
	            + " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " 
	            + " CLASS_DEPT.SAVE_PERIOD, "
	            + " DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN, "
	            + " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO "
	            + " FROM dbo.DMS_FILES "
	            + " INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
	            + " LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES.FOLDER_ID "
	            + " LEFT JOIN dbo.BOX ON BOX.BOX_ID = FOLDER.BOX_ID "
	            + " INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID "
	            + " AND CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
	            + " INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
	            + " INNER JOIN dbo.USERS ON USERS.USER_ID = DMS_FILES.USER_ID "
	            + " LEFT JOIN dbo.DMS_TRANSFER_STATUS ON DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES.TRANSFER_STATUS_ID "
	            + " WHERE (DMS_FILES.TRANSFER_STATUS_ID IS NULL OR DMS_FILES.TRANSFER_STATUS_ID IN (1, 5, 6, 7)) "
	            + " AND DMS_FILES.ARCHIVE_CENTER_STATMENT_ID IS NULL "
	        );

	        // Conditionally add department filter
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            SQL.append(" AND DEPARTMENTS.DEPT_ID = ? ");
	        }

	        SQL.append(" ORDER BY DEPARTMENTS.DEPT_ID");

	        stmt = dbConnection.getCon().prepareStatement(SQL.toString());

	        // Bind departmentName parameter if filter is used
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            stmt.setString(1, departmentName);
	        }

	        rs = stmt.executeQuery();
	        Set<DmsFiles> dmsFiles = new HashSet<>();
	        int totalCount = 0;

	        while (rs.next()) {
	            DmsFiles file = null;
	            boolean canTransferFile = false;
	            int savePeriod = rs.getInt("SAVE_PERIOD");
	            int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();

	            // Filter based on TransferCreateDate setting
	            if (ConfigManager.useTransferCreateDate()) {
	                int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
	                if (currentHijriYear >= modifiedYearHijri + savePeriod) {
	                    canTransferFile = true;        
	                }
	            } else {
	                int customHijrYear = getCustomPopertyDateHijriYear(rs.getInt("FILE_ID"), ConfigManager.getTransferCustomProperty());
	                if (customHijrYear > 0 && (currentHijriYear >= customHijrYear + savePeriod)) {
	                    canTransferFile = true;
	                }
	            }

	            // Only add file if it can be transferred
	            if (canTransferFile) {
	                file = new DmsFiles();
	                file.setFileId(rs.getInt("FILE_ID"));
	                file.setDocumentId(rs.getString("DOCUMENT_ID"));
	                file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
	                file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
	                file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
	                file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
	                file.setFolerNo(rs.getInt("FOLDER_NO"));
	                file.setBoxNo(rs.getInt("BOX_NO"));
	                file.setNoPages(rs.getInt("NO_PAGES"));
	                LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
	                file.setCreateYearHijri(muslimDate.getYear() + "");
	                file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear() + "");

	                file.setArchiveCenterTransferExtensionMonths(rs.getInt("ARCHIVE_CENTER_TRANSFER_EXTENSION_MONTHS"));
	                file.setArchiveCenterTransferExtensionReason(rs.getString("ARCHIVE_CENTER_TRANSFER_EXTENSION_REASON"));

	                Classifiction classifiction = new Classifiction();
	                classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
	                classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
	                classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));

	                file.setClassifiction(classifiction);

	                Department department = new Department();
	                department.setDeptArName(rs.getString("DEPT_AR_NAME"));
	                department.setDeptEnName(rs.getString("DEPT_EN_NAME"));

	                file.setDepartment(department);

	                DMSTransferStatus transferStatus = new DMSTransferStatus();
	                transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
	                transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
	                transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
	                file.setTransferStatus(transferStatus);

	                dmsFiles.add(file);  // Add file to set
	                totalCount++;  // Increment total count for transferred documents
	            }
	        }

	        System.out.println("Total documents ready for transfer: " + totalCount);  // Log total count
	        return dmsFiles;

	    } catch (SQLException e) {
	        throw new DatabaseException("Error getArchiveCenterTransferReadyFiles", e);
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	        } catch (SQLException unexpected) {
	            throw new DatabaseException("Error closing resources", unexpected);
	        }
	    }
	}
	public List<Map<String, Object>> fetchMigratedDocumentsStatistics() throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String SQL = 
                "SELECT " +
                "   ISL.INTEGRATION_SYS_AR AS integrationSysAr, " +
                "   ISL.INTEGRATION_SYS_EN AS integrationSysEn, " +
                "   COUNT(DF.DOCUMENT_ID) AS totalDocuments " +
                "FROM " +
                "   dbo.DMS_FILES DF " +
                "INNER JOIN " +
                "   dbo.INTGRATION_SYSTEMS_LK ISL ON ISL.INTEGRATION_SYS_ID = DF.INTEGRATION_SYS_ID " +
                "GROUP BY " +
                "   ISL.INTEGRATION_SYS_AR, ISL.INTEGRATION_SYS_EN " +
                "ORDER BY " +
                "   totalDocuments DESC";

            stmt = dbConnection.getCon().prepareStatement(SQL);
            rs = stmt.executeQuery();

            List<Map<String, Object>> statistics = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("integrationSysAr", rs.getString("integrationSysAr"));
                stat.put("integrationSysEn", rs.getString("integrationSysEn"));
                stat.put("totalDocuments", rs.getInt("totalDocuments"));
                statistics.add(stat);
            }
            return statistics;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching migrated documents statistics", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                throw new DatabaseException("Error closing database resources", ex);
            }
        }
    }
	
//	public Set<DmsFiles> getArchiveCenterTransferReadyFiles() throws DatabaseException {
//	    PreparedStatement stmt = null;
//	    ResultSet rs = null;
//	    Set<DmsFiles> dmsFiles = new HashSet<>();
//	    
//	    try {
//	        String SQL = " SELECT DMS_FILES.DOCUMENT_ID, DMS_FILES.FILE_ID, DMS_FILES.MODIFIED_DATE, CLASS_DEPT.SAVE_PERIOD, " +
//	                     " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME, CLASSIFICTIONS.SYMPOLIC_NAME, " +
//	                     " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID, " +
//	                     " DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN " +
//	                     " FROM dbo.DMS_FILES " +
//	                     " INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS " +
//	                     " INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " +
//	                     " AND CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID " +
//	                     " INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID " +
//	                     " LEFT JOIN dbo.DMS_TRANSFER_STATUS ON DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES.TRANSFER_STATUS_ID " +
//	                     " WHERE (DMS_FILES.TRANSFER_STATUS_ID IS NULL OR DMS_FILES.TRANSFER_STATUS_ID IN (1, 5, 6, 7)) " +
//	                     " AND DMS_FILES.ARCHIVE_CENTER_STATMENT_ID IS NULL";
//
//	        stmt = dbConnection.getCon().prepareStatement(SQL);
//	        rs = stmt.executeQuery();
//	        int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();
//
//	        while (rs.next()) {
//	            int fileId = rs.getInt("FILE_ID");
//	            int savePeriod = rs.getInt("SAVE_PERIOD");
//	            boolean canTransferFile = false;
//
//	            if (ConfigManager.useTransferCreateDate()) {
//	                int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
//	                if (currentHijriYear >= modifiedYearHijri + savePeriod) {
//	                    canTransferFile = true;
//	                }
//	            } else {
//	                int customHijriYear = getCustomPopertyDateHijriYear(fileId, ConfigManager.getTransferCustomProperty());
//	                if (customHijriYear > 0 && currentHijriYear >= customHijriYear + savePeriod) {
//	                    canTransferFile = true;
//	                }
//	            }
//
//	            if (canTransferFile) {
//	                DmsFiles file = new DmsFiles();
//	                file.setFileId(fileId);
//	                file.setDocumentId(rs.getString("DOCUMENT_ID"));
//	                file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
//	                file.setSavePeriod(savePeriod);
//
//	                // Populate and set Department
//	                Department department = new Department();
//	                department.setDeptArName(rs.getString("DEPT_AR_NAME"));
//	                department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
//	                file.setDepartment(department);
//
//	                // Populate and set Classification
//	                Classifiction classifiction = new Classifiction();
//	                classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
//	                classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
//	                classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
//	                file.setClassifiction(classifiction);
//
//	                // Populate and set Transfer Status
//	                DMSTransferStatus transferStatus = new DMSTransferStatus();
//	                transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
//	                transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
//	                transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
//	                file.setTransferStatus(transferStatus);
//
//	                dmsFiles.add(file);
//
//	                System.out.println("Eligible Document: Dept=" + department.getDeptArName() + ", File ID=" + fileId + ", Save Period=" + savePeriod);
//	            }
//	        }
//
//	        return dmsFiles;
//
//	    } catch (SQLException e) {
//	        throw new DatabaseException("Error in getArchiveCenterTransferReadyFiles", e);
//	    } finally {
//	        try {
//	            if (rs != null) rs.close();
//	            if (stmt != null) stmt.close();
//	        } catch (SQLException unexpected) {
//	            throw new DatabaseException("Error closing resources", unexpected);
//	        }
//	    }
//	}






	private int getCustomPopertyDateHijriYear(int fileId, String propertyName) throws DatabaseException{
		if(ConfigManager.getTransferCustomProperty() == null || ConfigManager.getTransferCustomProperty().equalsIgnoreCase("")){
			return 0;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String SQL = " SELECT DMS_PROPERTIES_AUDIT.PROPERTY_NAME ,  DMS_PROPERTIES_AUDIT.PROPERTY_VALUE  "
					+ "	FROM      "
					+ "	dbo.DMS_PROPERTIES_AUDIT   "
					+ "	WHERE    "
					+ "	DMS_PROPERTIES_AUDIT.DMS_AUDIT_ID = ( select max(DMS_AUDIT.DMS_AUDIT_ID) as DMS_AUDIT_ID   "
					+ "	from dbo.DMS_AUDIT   "
					+ "	INNER JOIN dbo.DMS_PROPERTIES_AUDIT on DMS_AUDIT.DMS_AUDIT_ID = DMS_PROPERTIES_AUDIT.DMS_AUDIT_ID  "
					+ "	where file_id = ? )  "
					+ "	AND  "
					+ "	DMS_PROPERTIES_AUDIT.PROPERTY_NAME = ?  " ;
					
					
			stmt = dbConnection.getCon().prepareStatement(SQL);
			stmt.setInt(1, fileId);
			stmt.setString(2, propertyName);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				String datePropValu = rs.getString("PROPERTY_VALUE");
				if( datePropValu !=null && !datePropValu.equalsIgnoreCase("") && !datePropValu.equalsIgnoreCase("null") ){
					int customYearHijri = DateUtil.toMuslim(java.sql.Date.valueOf(datePropValu.substring(0, 10)).toLocalDate()).getYear();
					return customYearHijri;
				}else{
					return 0;
				}
			}
			
			return 0;
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getCustomTransferFieldDateHijriYear", e);
		} finally {
        	try {
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}
				
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error getCustomTransferFieldDateHijriYear", unexpected);
			}
        }
			
	}


//	public Set<DmsFiles> getArchiveCenterTransferdFiles() throws DatabaseException{
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try{
//			String SQL = " SELECT  "       
//					+ " DMS_FILES.DOCUMENT_ID, DMS_FILES.FILE_ID, DMS_FILES.FOLDER_ID, DMS_FILES.DOCUMENT_CLASS, DMS_FILES.DEPT_ID, "
//					+ " DMS_FILES.USER_ID,  DMS_FILES.NO_PAGES, "
//					
//					+ "	DMS_FILES.CREATED_DATE AS CREATE_DATE , DMS_FILES.MODIFIED_DATE , DMS_FILES.DOCUMENT_NAME , "
//
//					+ " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME,  "
//					+ " USERS.UserArname, USERS.UserEnName,  "
//					+ " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " 
//					+ " CLASS_DEPT.SAVE_PERIOD,  " 
////					+ " STORAGE_CENTER_TYPE.TYPE_AR, STORAGE_CENTER_TYPE.TYPE_EN,"
//					+ " DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID , DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN ,"
//					+ " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO"
//					+ "	FROM "   
//					+ " dbo.DMS_FILES "
//					+ " INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
//					+ " LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES.FOLDER_ID"
//					+ " LEFT JOIN dbo.BOX on BOX.BOX_ID = FOLDER.BOX_ID"
////					+ " INNER JOIN dbo.STORAGE_CENTER_TYPE on STORAGE_CENTER_TYPE.STORAGE_CENTER_TYPE_ID = CLASSIFICTIONS.SAVE_TYPE "
//					+ " INNER JOIN dbo.CLASS_DEPT on CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " 
//					+ " and CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
//					+ " INNER JOIN dbo.DEPARTMENTS on DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
//					+ " INNER JOIN dbo.USERS on USERS.USER_ID = DMS_FILES.USER_ID "
//					+ " LEFT JOIN dbo.DMS_TRANSFER_STATUS on  DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES.TRANSFER_STATUS_ID "
//					+ " WHERE  "
//					+ " DMS_FILES.TRANSFER_STATUS_ID  =2 "
//					+ " ORDER BY DEPARTMENTS.DEPT_ID ";
//			stmt = dbConnection.getCon().prepareStatement(SQL);
//			rs = stmt.executeQuery();
//			Set<DmsFiles> dmsFiles = new HashSet<DmsFiles>();
//			while(rs.next()){
//				DmsFiles file = new DmsFiles();
//				file.setFileId(rs.getInt("FILE_ID"));
//				file.setDocumentId(rs.getString("DOCUMENT_ID"));
//				file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
//				file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
//				file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
//				file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
//				file.setFolerNo(rs.getInt("FOLDER_NO"));
//				file.setBoxNo(rs.getInt("BOX_NO"));
//				file.setNoPages(rs.getInt("NO_PAGES"));
//				LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
//				file.setCreateYearHijri(muslimDate.getYear()+"");
//				file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear()+"");
//
//				Classifiction classifiction = new Classifiction();
//				classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
//				classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
//				classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
//				file.setClassifiction(classifiction);
//				
//				Department department =  new Department();
//				department.setDeptArName(rs.getString("DEPT_AR_NAME"));
//				department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
//				file.setDepartment(department);
//					
//				DMSTransferStatus transferStatus = new DMSTransferStatus();
//				transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
//				transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
//				transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
//				file.setTransferStatus(transferStatus);
//		
//				
//				dmsFiles.add(file);
//				
//			}
//			
//			return dmsFiles;
//		} catch (SQLException e) {
//			throw new DatabaseException("Error getArchiveCenterTransferdFiles", e);
//		} finally {
//        	try {
//				if (rs != null) {
//					rs.close();
//				}
//
//				if (stmt != null) {
//					stmt.close();
//				}
//				
//			} catch (SQLException unexpected) {
//				throw new DatabaseException("Error getArchiveCenterTransferdFiles", unexpected);
//			}
//        }
//	}
	
	// public List<Map<String, Object>> getArchiveCenterTransferdFiles() throws DatabaseException {
	//     PreparedStatement stmt = null;
	//     ResultSet rs = null;
	//     try {
	//         String SQL = "SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, "
	//                    + "COUNT(DMS_FILES.DOCUMENT_ID) AS documentCount "
	//                    + "FROM dbo.DMS_FILES "
	//                    + "INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = DMS_FILES.DEPT_ID "
	//                    + "WHERE DMS_FILES.TRANSFER_STATUS_ID = 2 "
	//                    + "GROUP BY DEPARTMENTS.DEPT_AR_NAME "
	//                    + "ORDER BY DEPARTMENTS.DEPT_AR_NAME";
	        
	//         stmt = dbConnection.getCon().prepareStatement(SQL);
	//         rs = stmt.executeQuery();

	//         List<Map<String, Object>> departmentCounts = new ArrayList<>();
	        
	//         while (rs.next()) {
	//             Map<String, Object> departmentData = new HashMap<>();
	//             departmentData.put("deptArName", rs.getString("deptArName"));
	//             departmentData.put("documentCount", rs.getInt("documentCount"));
	//             departmentCounts.add(departmentData);
	//         }
	        
	//         return departmentCounts;
	//     } catch (SQLException e) {
	//         throw new DatabaseException("Error getArchiveCenterTransferdFiles", e);
	//     } finally {
	//         try {
	//             if (rs != null) rs.close();
	//             if (stmt != null) stmt.close();
	//         } catch (SQLException unexpected) {
	//             throw new DatabaseException("Error closing resources", unexpected);
	//         }
	//     }
	// }

	public List<Map<String, Object>> getArchiveCenterTransferdFiles(String departmentName) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuilder SQL = new StringBuilder(
				"SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, " +
				"COUNT(DMS_FILES.DOCUMENT_ID) AS documentCount " +
				"FROM dbo.DMS_FILES " +
				"INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = DMS_FILES.DEPT_ID " +
				"WHERE DMS_FILES.TRANSFER_STATUS_ID = 2 "
			);
	
			// Add filtering clause if departmentName is provided
			if (departmentName != null && !departmentName.trim().isEmpty()) {
				SQL.append("AND DEPARTMENTS.DEPT_ID = ? ");
			}
	
			SQL.append("GROUP BY DEPARTMENTS.DEPT_AR_NAME ORDER BY DEPARTMENTS.DEPT_AR_NAME");
			
			stmt = dbConnection.getCon().prepareStatement(SQL.toString());
			
			// Set departmentName parameter if it's used
			if (departmentName != null && !departmentName.trim().isEmpty()) {
				stmt.setString(1, departmentName);
			}
			
			rs = stmt.executeQuery();
			List<Map<String, Object>> departmentCounts = new ArrayList<>();
			
			while (rs.next()) {
				Map<String, Object> departmentData = new HashMap<>();
				departmentData.put("deptArName", rs.getString("deptArName"));
				departmentData.put("documentCount", rs.getInt("documentCount"));
				departmentCounts.add(departmentData);
			}
			
			return departmentCounts;
		} catch (SQLException e) {
			throw new DatabaseException("Error getArchiveCenterTransferdFiles", e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error closing resources", unexpected);
			}
		}
	}
	
	

	public List<Map<String, Object>> getArchiveDocDepartment(String departmentName) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuilder SQL = new StringBuilder(
				"SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, " +
				"COUNT(DMS_FILES.DOCUMENT_ID) AS documentCount " +
				"FROM dbo.DMS_FILES " +
				"INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = DMS_FILES.DEPT_ID "
			);
	
			// Add filtering clause if departmentName is provided
			if (departmentName != null && !departmentName.trim().isEmpty()) {
				SQL.append("AND DEPARTMENTS.DEPT_ID = ? ");
			}
	
			SQL.append("GROUP BY DEPARTMENTS.DEPT_AR_NAME ORDER BY DEPARTMENTS.DEPT_AR_NAME");
			
			stmt = dbConnection.getCon().prepareStatement(SQL.toString());
			
			// Set departmentName parameter if it's used
			if (departmentName != null && !departmentName.trim().isEmpty()) {
				stmt.setString(1, departmentName);
			}
			
			rs = stmt.executeQuery();
			List<Map<String, Object>> departmentCounts = new ArrayList<>();
			
			while (rs.next()) {
				Map<String, Object> departmentData = new HashMap<>();
				departmentData.put("deptArName", rs.getString("deptArName"));
				departmentData.put("documentCount", rs.getInt("documentCount"));
				departmentCounts.add(departmentData);
			}
			
			return departmentCounts;
		} catch (SQLException e) {
			throw new DatabaseException("Error getArchiveCenterTransferdFiles", e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error closing resources", unexpected);
			}
		}
	}

	public List<Map<String, Object>> getArchiveDocClassification(String departmentName) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT ")
				.append("[dbo].[CLASSIFICTIONS].[CLASS_AR_NAME] AS classArName , ")
				.append("COUNT([dbo].[DMS_FILES].[FILE_ID]) AS documentCount ")
				.append("FROM [dbo].[DMS_FILES] ")
				.append("LEFT JOIN [dbo].[CLASSIFICTIONS] ON [dbo].[DMS_FILES].[DOCUMENT_CLASS] = [dbo].[CLASSIFICTIONS].[SYMPOLIC_NAME] ")
				.append("WHERE 1 = 1 "); // Always true condition for easier appending
	
		
			queryBuilder.append("GROUP BY [dbo].[CLASSIFICTIONS].[CLASS_AR_NAME]");
			
			stmt = dbConnection.getCon().prepareStatement(queryBuilder.toString());
			
			
			rs = stmt.executeQuery();
			List<Map<String, Object>> departmentCounts = new ArrayList<>();
			
			while (rs.next()) {
				Map<String, Object> departmentData = new HashMap<>();
				departmentData.put("classArName", rs.getString("classArName"));
				departmentData.put("documentCount", rs.getInt("documentCount"));
				departmentCounts.add(departmentData);
			}
			
			return departmentCounts;
		} catch (SQLException e) {
			throw new DatabaseException("Error getArchiveDocClassification", e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error closing resources", unexpected);
			}
		}
	}
	
	
	public List<Map<String, Object>> fetchConfidentialDocClassification() throws DatabaseException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        StringBuilder SQL = new StringBuilder(
	            "SELECT " +
	            "DMS_PROPERTIES_AUDIT.PROPERTY_VALUE AS propertyValue, " +
	            "COUNT(DISTINCT DMS_FILES.DOCUMENT_ID) AS documentCount " +
	            "FROM dbo.DMS_FILES " +
	            "INNER JOIN dbo.DMS_AUDIT ON DMS_FILES.FILE_ID = DMS_AUDIT.FILE_ID " +
	            "INNER JOIN dbo.DMS_PROPERTIES_AUDIT ON DMS_AUDIT.DMS_AUDIT_ID = DMS_PROPERTIES_AUDIT.DMS_AUDIT_ID " +
	            "WHERE DMS_PROPERTIES_AUDIT.PROPERTY_NAME = 'DegreeSecrecy' " +
	            "AND DMS_AUDIT.DMS_AUDIT_ID IN ( " +
	            "    SELECT MAX(DMS_AUDIT_INNER.DMS_AUDIT_ID) " +
	            "    FROM dbo.DMS_AUDIT DMS_AUDIT_INNER " +
	            "    INNER JOIN dbo.DMS_PROPERTIES_AUDIT DMS_PROPERTIES_AUDIT_INNER " +
	            "    ON DMS_AUDIT_INNER.DMS_AUDIT_ID = DMS_PROPERTIES_AUDIT_INNER.DMS_AUDIT_ID " +
	            "    WHERE DMS_PROPERTIES_AUDIT_INNER.PROPERTY_NAME = 'DegreeSecrecy' " +
	            "    GROUP BY DMS_AUDIT_INNER.FILE_ID " +
	            ") " +
	            "GROUP BY DMS_PROPERTIES_AUDIT.PROPERTY_VALUE " +
	            "ORDER BY documentCount DESC"
	        );

	        stmt = dbConnection.getCon().prepareStatement(SQL.toString());
	        rs = stmt.executeQuery();

	        List<Map<String, Object>> classificationCounts = new ArrayList<>();
	        while (rs.next()) {
	            Map<String, Object> classificationData = new HashMap<>();
	            classificationData.put("propertyValue", rs.getString("propertyValue"));
	            classificationData.put("documentCount", rs.getInt("documentCount"));
	            classificationCounts.add(classificationData);
	        }
	        return classificationCounts;

	    } catch (SQLException e) {
	        throw new DatabaseException("Error fetching confidential document classification", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	        } catch (SQLException ex) {
	            throw new DatabaseException("Error closing database resources", ex);
	        }
	    }
	}



//	public Set<DmsFiles> getNationalCenterTransferReadyFiles() throws DatabaseException{
//		
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try{
//			String SQL = " SELECT  "
//					+ "	( select sum(CLASS_DEPT.SAVE_PERIOD)  as TEMP_SAVE_PERIOD_COUNT  "
//					+ "		from dbo.CLASS_DEPT "
//					+ "		INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID"
//					+ "		INNER JOIN dbo.DMS_FILES on DMS_FILES.DOCUMENT_CLASS = CLASSIFICTIONS.SYMPOLIC_NAME"
//					+ "		where DMS_FILES_OUTER.FILE_ID = DMS_FILES.FILE_ID	"
//					+ "		group by  DMS_FILES.FILE_ID ) AS permanent_save_period_sum , "     
//					+ " DMS_FILES_OUTER.DOCUMENT_ID, DMS_FILES_OUTER.FILE_ID, DMS_FILES_OUTER.FOLDER_ID, DMS_FILES_OUTER.DOCUMENT_CLASS, DMS_FILES_OUTER.DEPT_ID, "
//					+ " DMS_FILES_OUTER.USER_ID,  DMS_FILES_OUTER.NO_PAGES, " 
//					
//					+ "	DMS_FILES_OUTER.CREATED_DATE AS CREATE_DATE , DMS_FILES_OUTER.MODIFIED_DATE , DMS_FILES_OUTER.DOCUMENT_NAME , "
//
//					+ " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME,  "
//					+ " USERS.UserArname, USERS.UserEnName,  "
//					+ " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " 
//					+ " CLASS_DEPT.SAVE_PERIOD,  " 
////					+ " STORAGE_CENTER_TYPE.TYPE_AR, STORAGE_CENTER_TYPE.TYPE_EN,"
//					+ " DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID , DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN ,"
//					+ " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO"
//					+ "	FROM "   
//					+ " dbo.DMS_FILES AS DMS_FILES_OUTER"
//					+ " INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES_OUTER.DOCUMENT_CLASS "
//					+ " LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES_OUTER.FOLDER_ID"
//					+ " LEFT JOIN dbo.BOX on BOX.BOX_ID = FOLDER.BOX_ID"
////					+ " INNER JOIN dbo.STORAGE_CENTER_TYPE on STORAGE_CENTER_TYPE.STORAGE_CENTER_TYPE_ID = CLASSIFICTIONS.SAVE_TYPE "
//					+ " INNER JOIN dbo.CLASS_DEPT on CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " 
//					+ " and CLASS_DEPT.DEPT_ID = DMS_FILES_OUTER.DEPT_ID "
//					+ " INNER JOIN dbo.DEPARTMENTS on DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
//					+ " INNER JOIN dbo.USERS on USERS.USER_ID = DMS_FILES_OUTER.USER_ID "
//					+ " LEFT JOIN dbo.DMS_TRANSFER_STATUS on  DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES_OUTER.TRANSFER_STATUS_ID "
//					+ " WHERE  "
//					+ " CLASSIFICTIONS.SAVE_TYPE = 1 "
//					+ " AND "
//					+ " DMS_FILES_OUTER.TRANSFER_STATUS_ID in (2,3) "
//					+ " AND"
//					+ " DMS_FILES_OUTER.NATIONAL_CENTER_STATMENT_ID IS NULL " 
//					+ " ORDER BY DEPARTMENTS.DEPT_ID ";
//			stmt = dbConnection.getCon().prepareStatement(SQL);
//			rs = stmt.executeQuery();
//			Set<DmsFiles> dmsFiles = new HashSet<DmsFiles>();
//			while(rs.next()){
//				DmsFiles file = null;
//				boolean canTransferFile = false;
//				int permanentSavePeriodSum = rs.getInt("permanent_save_period_sum");
//				int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();
//				// use create date to calculate destruction process
//				if(ConfigManager.useTransferCreateDate()){
//					int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
//					if(currentHijriYear >= modifiedYearHijri + permanentSavePeriodSum){
//						canTransferFile = true;	
////						System.out.println("currentHijriYear: "+currentHijriYear +" modifiedYearHijri: "+modifiedYearHijri +"permanentSavePeriodSum: "+permanentSavePeriodSum);	
//					}
//				}else{
//					// get custom property Date from audit property table
//					int customHijrYear = getCustomPopertyDateHijriYear(rs.getInt("FILE_ID") , ConfigManager.getTransferCustomProperty());
//					if(customHijrYear > 0 && (currentHijriYear >=  customHijrYear + permanentSavePeriodSum)){
//						canTransferFile = true;
////						System.out.println("currentHijriYear: "+currentHijriYear +" customHijrYear: "+customHijrYear +"  permanentSavePeriodSum: "+permanentSavePeriodSum);
//					}
//				}
//				if(canTransferFile){
//					file= new DmsFiles();
//					file.setFileId(rs.getInt("FILE_ID"));
//					file.setDocumentId(rs.getString("DOCUMENT_ID"));
//					file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
//					file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
//					file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
//					file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
//					file.setFolerNo(rs.getInt("FOLDER_NO"));
//					file.setBoxNo(rs.getInt("BOX_NO"));
//					file.setNoPages(rs.getInt("NO_PAGES"));
//					LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
//					file.setCreateYearHijri(muslimDate.getYear()+"");
//					file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear()+"");
//
//					file.setPermanentSavePeriodAllDeptSum(rs.getInt("permanent_save_period_sum"));
//					
//					Classifiction classifiction = new Classifiction();
//					classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
//					classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
//					classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
//					file.setClassifiction(classifiction);
//					
//					Department department =  new Department();
//					department.setDeptArName(rs.getString("DEPT_AR_NAME"));
//					department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
//					file.setDepartment(department);
//					
//					DMSTransferStatus transferStatus = new DMSTransferStatus();
//					transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
//					transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
//					transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
//					file.setTransferStatus(transferStatus);
//					
//				}
//				if(file !=null)
//					dmsFiles.add(file);
//				
//			}
//			
//			return dmsFiles;
//		} catch (SQLException e) {
//			throw new DatabaseException("Error getNationalCenterTransferReadyFiles", e);
//		} finally {
//        	try {
//				if (rs != null) {
//					rs.close();
//				}
//
//				if (stmt != null) {
//					stmt.close();
//				}
//				
//			} catch (SQLException unexpected) {
//				throw new DatabaseException("Error getNationalCenterTransferReadyFiles", unexpected);
//			}
//        }
//	}
	
	public Set<DmsFiles> getNationalCenterTransferReadyFiles(String departmentName) throws DatabaseException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        // Build the SQL query with a conditional filter for department name
	        StringBuilder SQL = new StringBuilder(
	            "SELECT " +
	            "   (SELECT SUM(CLASS_DEPT.SAVE_PERIOD) AS TEMP_SAVE_PERIOD_COUNT " +
	            "    FROM dbo.CLASS_DEPT " +
	            "    INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID " +
	            "    INNER JOIN dbo.DMS_FILES ON DMS_FILES.DOCUMENT_CLASS = CLASSIFICTIONS.SYMPOLIC_NAME " +
	            "    WHERE DMS_FILES_OUTER.FILE_ID = DMS_FILES.FILE_ID " +
	            "    GROUP BY DMS_FILES.FILE_ID) AS permanent_save_period_sum, " +
	            "   DMS_FILES_OUTER.DOCUMENT_ID, DMS_FILES_OUTER.FILE_ID, DMS_FILES_OUTER.FOLDER_ID, DMS_FILES_OUTER.DOCUMENT_CLASS, " +
	            "   DMS_FILES_OUTER.DEPT_ID, DMS_FILES_OUTER.USER_ID, DMS_FILES_OUTER.NO_PAGES, " +
	            "   DMS_FILES_OUTER.CREATED_DATE AS CREATE_DATE, DMS_FILES_OUTER.MODIFIED_DATE, DMS_FILES_OUTER.DOCUMENT_NAME, " +
	            "   DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME, " +
	            "   USERS.UserArname, USERS.UserEnName, " +
	            "   CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " +
	            "   CLASS_DEPT.SAVE_PERIOD, " +
	            "   DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN, " +
	            "   FOLDER.SERIAL AS FOLDER_NO, BOX.SERIAL AS BOX_NO " +
	            "FROM " +
	            "   dbo.DMS_FILES AS DMS_FILES_OUTER " +
	            "   INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES_OUTER.DOCUMENT_CLASS " +
	            "   LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES_OUTER.FOLDER_ID " +
	            "   LEFT JOIN dbo.BOX ON BOX.BOX_ID = FOLDER.BOX_ID " +
	            "   INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " +
	            "       AND CLASS_DEPT.DEPT_ID = DMS_FILES_OUTER.DEPT_ID " +
	            "   INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID " +
	            "   INNER JOIN dbo.USERS ON USERS.USER_ID = DMS_FILES_OUTER.USER_ID " +
	            "   LEFT JOIN dbo.DMS_TRANSFER_STATUS ON DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES_OUTER.TRANSFER_STATUS_ID " +
	            "WHERE " +
	            "   CLASSIFICTIONS.SAVE_TYPE = 1 " +
	            "   AND DMS_FILES_OUTER.TRANSFER_STATUS_ID IN (2, 3) " +
	            "   AND DMS_FILES_OUTER.NATIONAL_CENTER_STATMENT_ID IS NULL "
	        );

	        // Add department filter if provided
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            SQL.append("AND DEPARTMENTS.DEPT_ID = ? ");
	        }

	        SQL.append("ORDER BY DEPARTMENTS.DEPT_ID");

	        stmt = dbConnection.getCon().prepareStatement(SQL.toString());

	        // Bind departmentName if provided
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            stmt.setString(1, departmentName);
	        }

	        rs = stmt.executeQuery();
	        Set<DmsFiles> dmsFiles = new HashSet<>();

	        while (rs.next()) {
	            DmsFiles file = null;
	            boolean canTransferFile = false;
	            int permanentSavePeriodSum = rs.getInt("permanent_save_period_sum");
	            int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();

	            // Logic for determining if the file can be transferred
	            if (ConfigManager.useTransferCreateDate()) {
	                int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
	                if (currentHijriYear >= modifiedYearHijri + permanentSavePeriodSum) {
	                    canTransferFile = true;
	                }
	            } else {
	                int customHijriYear = getCustomPopertyDateHijriYear(rs.getInt("FILE_ID"), ConfigManager.getTransferCustomProperty());
	                if (customHijriYear > 0 && (currentHijriYear >= customHijriYear + permanentSavePeriodSum)) {
	                    canTransferFile = true;
	                }
	            }

	            if (canTransferFile) {
	                file = new DmsFiles();
	                file.setFileId(rs.getInt("FILE_ID"));
	                file.setDocumentId(rs.getString("DOCUMENT_ID"));
	                file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
	                file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
	                file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
	                file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
	                file.setFolerNo(rs.getInt("FOLDER_NO"));
	                file.setBoxNo(rs.getInt("BOX_NO"));
	                file.setNoPages(rs.getInt("NO_PAGES"));
	                LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
	                file.setCreateYearHijri(String.valueOf(muslimDate.getYear()));
	                file.setCreateYear(String.valueOf(file.getCreateDate().toLocalDateTime().getYear()));
	                file.setPermanentSavePeriodAllDeptSum(rs.getInt("permanent_save_period_sum"));

	                // Set classification and department data
	                Classifiction classifiction = new Classifiction();
	                classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
	                classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
	                classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
	                file.setClassifiction(classifiction);

	                Department department = new Department();
	                department.setDeptArName(rs.getString("DEPT_AR_NAME"));
	                department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
	                file.setDepartment(department);

	                DMSTransferStatus transferStatus = new DMSTransferStatus();
	                transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
	                transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
	                transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
	                file.setTransferStatus(transferStatus);

	                dmsFiles.add(file);
	            }
	        }

	        return dmsFiles;

	    } catch (SQLException e) {
	        throw new DatabaseException("Error in getNationalCenterTransferReadyFiles", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	        } catch (SQLException unexpected) {
	            throw new DatabaseException("Error closing resources", unexpected);
	        }
	    }
	}

	

//	public Set<DmsFiles> getNationalCenterTransferdFiles() throws DatabaseException{
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try{
//			String SQL =  " SELECT  "       
//					+ " DMS_FILES.DOCUMENT_ID, DMS_FILES.FILE_ID, DMS_FILES.FOLDER_ID, DMS_FILES.DOCUMENT_CLASS, DMS_FILES.DEPT_ID, "
//					+ "	DMS_FILES.USER_ID,  DMS_FILES.NO_PAGES, " 
//					+ "	DMS_FILES.CREATED_DATE AS CREATE_DATE , DMS_FILES.MODIFIED_DATE , DMS_FILES.DOCUMENT_NAME , "
//
//					+ " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME,  "
//					+ " USERS.UserArname, USERS.UserEnName,  "
//					+ " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME, " 
//					+ " CLASS_DEPT.SAVE_PERIOD,  " 
////					+ " STORAGE_CENTER_TYPE.TYPE_AR, STORAGE_CENTER_TYPE.TYPE_EN,"
//					+ " DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID , DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_AR, DMS_TRANSFER_STATUS.TRANSFER_STATUS_NAME_EN ,"
//					+ " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO"
//					+ "	FROM "   
//					+ " dbo.DMS_FILES "
//					+ " INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
//					+ " LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES.FOLDER_ID"
//					+ " LEFT JOIN dbo.BOX on BOX.BOX_ID = FOLDER.BOX_ID"
////					+ " INNER JOIN dbo.STORAGE_CENTER_TYPE on STORAGE_CENTER_TYPE.STORAGE_CENTER_TYPE_ID = CLASSIFICTIONS.SAVE_TYPE "
//					+ " INNER JOIN dbo.CLASS_DEPT on CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " 
//					+ " and CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
//					+ " INNER JOIN dbo.DEPARTMENTS on DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
//					+ " INNER JOIN dbo.USERS on USERS.USER_ID = DMS_FILES.USER_ID "
//					+ " LEFT JOIN dbo.DMS_TRANSFER_STATUS on  DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES.TRANSFER_STATUS_ID "
//					+ " WHERE  "
//					+ " CLASSIFICTIONS.SAVE_TYPE = 1 "
//					+ " AND "
//					+ " DMS_FILES.TRANSFER_STATUS_ID  =4 "
//					+ " ORDER BY DEPARTMENTS.DEPT_ID ";
//			stmt = dbConnection.getCon().prepareStatement(SQL);
//			rs = stmt.executeQuery();
//			Set<DmsFiles> dmsFiles = new HashSet<DmsFiles>();
//			while(rs.next()){
//				DmsFiles file = new DmsFiles();
//				file.setFileId(rs.getInt("FILE_ID"));
//				file.setDocumentId(rs.getString("DOCUMENT_ID"));
//				file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
//				file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
//				file.setSavePeriod(rs.getInt("SAVE_PERIOD"));
//				file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
//				file.setFolerNo(rs.getInt("FOLDER_NO"));
//				file.setBoxNo(rs.getInt("BOX_NO"));
//				file.setNoPages(rs.getInt("NO_PAGES"));
//				LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
//				file.setCreateYearHijri(muslimDate.getYear()+"");
//				file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear()+"");
//
//				Classifiction classifiction = new Classifiction();
//				classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
//				classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
//				classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
//				file.setClassifiction(classifiction);
//				
//				Department department =  new Department();
//				department.setDeptArName(rs.getString("DEPT_AR_NAME"));
//				department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
//				file.setDepartment(department);
//					
//				DMSTransferStatus transferStatus = new DMSTransferStatus();
//				transferStatus.setTransferStatusId(rs.getInt("TRANSFER_STATUS_ID"));
//				transferStatus.setTransferStatusNameAr(rs.getString("TRANSFER_STATUS_NAME_AR"));
//				transferStatus.setTransferStatusNameEn(rs.getString("TRANSFER_STATUS_NAME_EN"));
//				file.setTransferStatus(transferStatus);
//		
//				
//				dmsFiles.add(file);
//				
//			}
//			
//			return dmsFiles;
//		} catch (SQLException e) {
//			throw new DatabaseException("Error getArchiveCenterTransferdFiles", e);
//		} finally {
//        	try {
//				if (rs != null) {
//					rs.close();
//				}
//
//				if (stmt != null) {
//					stmt.close();
//				}
//				
//			} catch (SQLException unexpected) {
//				throw new DatabaseException("Error getArchiveCenterTransferdFiles", unexpected);
//			}
//        }
//	}
	
//	public List<Map<String, Object>> getNationalCenterTransferdFiles() throws DatabaseException {
//	    PreparedStatement stmt = null;
//	    ResultSet rs = null;
//	    try {
//	        String SQL =  "SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, "
//	                    + "COUNT(DMS_FILES.DOCUMENT_ID) AS documentCount "
//	                    + "FROM dbo.DMS_FILES "
//	                    + "INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
//	                    + "INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID "
//	                    + "AND CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
//	                    + "INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
//	                    + "WHERE CLASSIFICTIONS.SAVE_TYPE = 1 "
//	                    + "AND DMS_FILES.TRANSFER_STATUS_ID = 4 "
//	                    + "GROUP BY DEPARTMENTS.DEPT_AR_NAME "
//	                    + "ORDER BY DEPARTMENTS.DEPT_AR_NAME";
//	        
//	        stmt = dbConnection.getCon().prepareStatement(SQL);
//	        rs = stmt.executeQuery();
//
//	        List<Map<String, Object>> departmentCounts = new ArrayList<>();
//	        
//	        while (rs.next()) {
//	            Map<String, Object> departmentData = new HashMap<>();
//	            departmentData.put("deptArName", rs.getString("deptArName"));
//	            departmentData.put("documentCount", rs.getInt("documentCount"));
//	            departmentCounts.add(departmentData);
//	        }
//	        
//	        return departmentCounts;
//	    } catch (SQLException e) {
//	        throw new DatabaseException("Error getNationalCenterTransferdFiles", e);
//	    } finally {
//	        try {
//	            if (rs != null) rs.close();
//	            if (stmt != null) stmt.close();
//	        } catch (SQLException unexpected) {
//	            throw new DatabaseException("Error closing resources", unexpected);
//	        }
//	    }
//	}
	
	public List<Map<String, Object>> getNationalCenterTransferdFiles(String departmentName) throws DatabaseException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        // Build base SQL query with condition for filtering by department name
	        StringBuilder SQL = new StringBuilder(
	            "SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, "
	            + "COUNT(DMS_FILES.DOCUMENT_ID) AS documentCount "
	            + "FROM dbo.DMS_FILES "
	            + "INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES.DOCUMENT_CLASS "
	            + "INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID "
	            + "AND CLASS_DEPT.DEPT_ID = DMS_FILES.DEPT_ID "
	            + "INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
	            + "WHERE CLASSIFICTIONS.SAVE_TYPE = 1 "
	            + "AND DMS_FILES.TRANSFER_STATUS_ID = 4 "
	        );

	        // Conditionally add filter for department name
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            SQL.append("AND DEPARTMENTS.DEPT_ID = ? ");
	        }

	        SQL.append("GROUP BY DEPARTMENTS.DEPT_AR_NAME ORDER BY DEPARTMENTS.DEPT_AR_NAME");

	        stmt = dbConnection.getCon().prepareStatement(SQL.toString());

	        // Bind departmentName parameter if filter is used
	        if (departmentName != null && !departmentName.trim().isEmpty()) {
	            stmt.setString(1, departmentName);
	        }

	        rs = stmt.executeQuery();
	        List<Map<String, Object>> departmentCounts = new ArrayList<>();

	        while (rs.next()) {
	            Map<String, Object> departmentData = new HashMap<>();
	            departmentData.put("deptArName", rs.getString("deptArName"));
	            departmentData.put("documentCount", rs.getInt("documentCount"));
	            departmentCounts.add(departmentData);
	        }

	        return departmentCounts;
	    } catch (SQLException e) {
	        throw new DatabaseException("Error in getNationalCenterTransferdFiles", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	        } catch (SQLException unexpected) {
	            throw new DatabaseException("Error closing resources", unexpected);
	        }
	    }
	}


	

	
}
