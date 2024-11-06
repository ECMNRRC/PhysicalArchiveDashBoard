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
import com.dataserve.pad.bean.DMSDestroyStatus;
import com.dataserve.pad.bean.Department;
import com.dataserve.pad.bean.DmsFiles;
import com.dataserve.pad.util.ConfigManager;
import com.dataserve.pad.util.DateUtil;

public class DestroyFilesDAO {
	ConnectionManager dbConnection = null;

	public DestroyFilesDAO(ConnectionManager dbConnection) {
		super();
		this.dbConnection = dbConnection;
	}
	
	public Set<DmsFiles> getAllReadyDestroyFiles() throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String SQL = " select  "
//						+ "	( select sum(CLASS_DEPT.SAVE_PERIOD)  as TEMP_SAVE_PERIOD_COUNT  "
//						+ "		from dbo.CLASS_DEPT "
//						+ "		INNER JOIN dbo.CLASSIFICTIONS on  CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID"
//						+ "		INNER JOIN dbo.DMS_FILES on DMS_FILES.DOCUMENT_CLASS = CLASSIFICTIONS.SYMPOLIC_NAME"
//						+ "		where DMS_FILES_OUTER.FILE_ID = DMS_FILES.FILE_ID	"
//						+ "		group by  DMS_FILES.FILE_ID ) AS temp_save_period_sum , "
						+ " CLASS_DEPT.SAVE_PERIOD AS temp_save_period_sum  ,  " 
						+ " DEPARTMENTS.DEPT_AR_NAME, DEPARTMENTS.DEPT_EN_NAME,  "

						+ " DMS_FILES_OUTER.DOCUMENT_ID, DMS_FILES_OUTER.FILE_ID, DMS_FILES_OUTER.FOLDER_ID, DMS_FILES_OUTER.DOCUMENT_CLASS , "
						+ " DMS_FILES_OUTER.DEPT_ID, DMS_FILES_OUTER.USER_ID,  DMS_FILES_OUTER.NO_PAGES, "
						+ "	DMS_FILES_OUTER.CREATED_DATE AS CREATE_DATE , DMS_FILES_OUTER.MODIFIED_DATE , DMS_FILES_OUTER.DOCUMENT_NAME , "
						+ " CLASSIFICTIONS.CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME, CLASSIFICTIONS.CLASSIFICATION_ID, CLASSIFICTIONS.SYMPOLIC_NAME , "
						+ " USERS.UserArname, USERS.UserEnName, "
//						+ " STORAGE_CENTER_TYPE.TYPE_AR, STORAGE_CENTER_TYPE.TYPE_EN,"
						+ " DMS_DESTROY_STATUS.DESTROY_STATUS_ID , DMS_DESTROY_STATUS.DESTROY_STATUS_NAME_AR, DMS_DESTROY_STATUS.DESTROY_STATUS_NAME_EN , "
						+ " FOLDER.SERIAL FOLDER_NO, BOX.SERIAL BOX_NO"
						
						+" from dbo.DMS_FILES DMS_FILES_OUTER"
						+" INNER JOIN dbo.CLASSIFICTIONS on CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES_OUTER.DOCUMENT_CLASS"
						+" LEFT JOIN dbo.FOLDER ON FOLDER.FOLDER_ID = DMS_FILES_OUTER.FOLDER_ID"
						+" LEFT JOIN dbo.BOX on BOX.BOX_ID = FOLDER.BOX_ID"
//						+" INNER JOIN dbo.STORAGE_CENTER_TYPE on STORAGE_CENTER_TYPE.STORAGE_CENTER_TYPE_ID = CLASSIFICTIONS.SAVE_TYPE "
						
						+ " INNER JOIN dbo.CLASS_DEPT on CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID " 
//						+ " and CLASS_DEPT.DEPT_ID = DMS_FILES_OUTER.DEPT_ID "
						+ " INNER JOIN dbo.DEPARTMENTS on DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
						
						+" INNER JOIN dbo.USERS on USERS.USER_ID = DMS_FILES_OUTER.USER_ID "
						+" LEFT JOIN dbo.DMS_DESTROY_STATUS on  DMS_DESTROY_STATUS.DESTROY_STATUS_ID = DMS_FILES_OUTER.DESTROY_STATUS_ID "
						+ "INNER JOIN dbo.DMS_TRANSFER_STATUS on  DMS_TRANSFER_STATUS.TRANSFER_STATUS_ID = DMS_FILES_OUTER.TRANSFER_STATUS_ID "

						
						+" where"
						+" CLASSIFICTIONS.SAVE_TYPE = 2"
						+ " AND "
						+ " DMS_FILES_OUTER.TRANSFER_STATUS_ID = 2 "
						+ " AND "
						+ " DEPARTMENTS.IS_ARCHIVE_CENTER = 1"
						+" AND"
						+" ( DMS_FILES_OUTER.DESTROY_STATUS_ID is null or DMS_FILES_OUTER.DESTROY_STATUS_ID  =1 )"
						+ " AND"
						+ " DMS_FILES_OUTER.DESTRUCTION_STATMENT_ID IS NULL " ;


			stmt = dbConnection.getCon().prepareStatement(SQL);
			rs = stmt.executeQuery();
			Set<DmsFiles> dmsFiles = new HashSet<DmsFiles>();
			while(rs.next()){
				DmsFiles file = null;
				boolean canDestroyFile = false;
				int tempSavePeriodSum = rs.getInt("temp_save_period_sum");
				int currentHijriYear = DateUtil.toMuslim(LocalDateTime.now()).getYear();
				// use create date to calculate destruction process
				if(ConfigManager.useDestructionCreateDate()){
					int modifiedYearHijri = DateUtil.toMuslim(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()).getYear();
					if(currentHijriYear >= modifiedYearHijri + tempSavePeriodSum){
						canDestroyFile = true;				
					}
				}else{
					// get custom property Date from audit property table
					int customHijrYear = getCustomPopertyDateHijriYear(rs.getInt("FILE_ID") , ConfigManager.getDestructionCustomProperty());
					if(customHijrYear > 0 && (currentHijriYear >=  customHijrYear + tempSavePeriodSum)){
						canDestroyFile = true;
					}				
				}
				if(canDestroyFile){
					file = new DmsFiles();
					file.setFileId(rs.getInt("FILE_ID"));
					file.setDocumentId(rs.getString("DOCUMENT_ID"));
					file.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					file.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
					file.setTempSavePeriodAllDeptSum(rs.getInt("temp_save_period_sum"));
					file.setDocumentTitle(rs.getString("DOCUMENT_NAME"));
					file.setFolerNo(rs.getInt("FOLDER_NO"));
					file.setBoxNo(rs.getInt("BOX_NO"));
					file.setNoPages(rs.getInt("NO_PAGES"));    
					LocalDate muslimDate = DateUtil.toMuslim(file.getCreateDate().toLocalDateTime());
					file.setCreateYearHijri(muslimDate.getYear()+"");
					file.setCreateYear(file.getCreateDate().toLocalDateTime().getYear()+"");

					Classifiction classifiction = new Classifiction();
					classifiction.setSympolicName(rs.getString("SYMPOLIC_NAME"));
					classifiction.setClassArName(rs.getString("CLASS_AR_NAME"));
					classifiction.setClassEnName(rs.getString("CLASS_EN_NAME"));
					file.setClassifiction(classifiction);
					
					Department department = new Department();
	                department.setDeptArName(rs.getString("DEPT_AR_NAME"));
	                department.setDeptEnName(rs.getString("DEPT_EN_NAME"));
	                
	                file.setDepartment(department);
	                
					
					
					DMSDestroyStatus destroyStatus = new DMSDestroyStatus();
					destroyStatus.setDestroyStatusId(rs.getInt("DESTROY_STATUS_ID"));
					destroyStatus.setDestroyStatusNameAr(rs.getString("DESTROY_STATUS_NAME_AR"));
					destroyStatus.setDestroyStatusNameEn(rs.getString("DESTROY_STATUS_NAME_EN"));

					file.setDestroyStatus(destroyStatus);
					if(file!=null)
						dmsFiles.add(file);
				}
				
				
			}
			
			return dmsFiles;
		} catch (SQLException e) {
			throw new DatabaseException("Error getAllReadyDestroyFiles", e);
		} finally {
        	try {
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}
				
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error getAllReadyDestroyFiles", unexpected);
			}
        }
	}

	public List<Map<String, Object>> getAllDestroedFiles(String departmentName) throws DatabaseException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        // Build base SQL query with condition for filtering by department name
	        StringBuilder SQL = new StringBuilder(
	            "SELECT DEPARTMENTS.DEPT_AR_NAME AS deptArName, "
	            + "COUNT(DMS_FILES_OUTER.DOCUMENT_ID) AS documentCount "
	            + "FROM dbo.DMS_FILES DMS_FILES_OUTER "
	            + "INNER JOIN dbo.CLASSIFICTIONS ON CLASSIFICTIONS.SYMPOLIC_NAME = DMS_FILES_OUTER.DOCUMENT_CLASS "
	            + "INNER JOIN dbo.CLASS_DEPT ON CLASS_DEPT.CLASSIFICATION_ID = CLASSIFICTIONS.CLASSIFICATION_ID "
//	            + "AND CLASS_DEPT.DEPT_ID = DMS_FILES_OUTER.DEPT_ID "
	            + "INNER JOIN dbo.DEPARTMENTS ON DEPARTMENTS.DEPT_ID = CLASS_DEPT.DEPT_ID "
	            + "INNER JOIN dbo.USERS ON USERS.USER_ID = DMS_FILES_OUTER.USER_ID "
	            + "LEFT JOIN dbo.DMS_DESTROY_STATUS ON DMS_DESTROY_STATUS.DESTROY_STATUS_ID = DMS_FILES_OUTER.DESTROY_STATUS_ID "
	            + "WHERE CLASSIFICTIONS.SAVE_TYPE = 2 "
	            + "AND DMS_FILES_OUTER.DESTROY_STATUS_ID = 2 "
	            + "AND DEPARTMENTS.IS_ARCHIVE_CENTER = 1 "
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
	        throw new DatabaseException("Error getAllDestroedFiles", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	        } catch (SQLException unexpected) {
	            throw new DatabaseException("Error closing resources", unexpected);
	        }
	    }
	}


	
	private int getCustomPopertyDateHijriYear(int fileId, String propertyName) throws DatabaseException{
		if(ConfigManager.getDestructionCustomProperty() == null || ConfigManager.getDestructionCustomProperty().equalsIgnoreCase("")){
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
			throw new DatabaseException("Error getCustomPopertyDateHijriYear", e);
		} finally {
        	try {
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}
				
			} catch (SQLException unexpected) {
				throw new DatabaseException("Error getCustomPopertyDateHijriYear", unexpected);
			}
        }
			
	}

}
