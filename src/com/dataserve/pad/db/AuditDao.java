package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;

import com.dataserve.pad.bean.AuditBean;
import com.dataserve.pad.bean.DMSAuditBean;
import com.dataserve.pad.db.DatabaseException;

public class AuditDao extends AbstractDAO {

	public AuditDao() throws DatabaseException {
		super();
	}
	
	public void addAuditLog(AuditBean bean) throws DatabaseException {
		try {
			stmt = con.prepareStatement("INSERT INTO AUDIT (USER_ID, TRANSACTION_DATE, MODULE_ID, ACTION_TYPE_ID, METHOD, PARAMS) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, bean.getUserId());
			stmt.setTimestamp(2, bean.getTransactionDate());
			stmt.setInt(3, bean.getModuleId());
			stmt.setInt(4, bean.getActionTypeId());
			stmt.setString(5, bean.getMethod());
			stmt.setNString(6, bean.getParams());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error inserting into table AUDIT", e);
		} finally {
			releaseResources();
			safeClose();
		}
	}
	
	public Long addDMSAudit(DMSAuditBean bean) throws DatabaseException {
		ResultSet rs2 = null;
		long recordId = 0;
		try {
			stmt = con.prepareStatement("INSERT INTO DMS_AUDIT (DATE, USER_ID, OPERATION_ID, DOCUMENT_CLASS, DOCUMENT_ID, FILE_ID) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setTimestamp(1, bean.getDate());
			stmt.setString(2, bean.getUsername());
			stmt.setInt(3, 2);
			
			if (bean.getDocumentClass() != null) {
				stmt.setString(4, bean.getDocumentClass());
			} else {
				stmt.setNull(4, Types.NVARCHAR);
			}
			
			if (bean.getDocumentId() != null) {
				stmt.setString(5, bean.getDocumentId());
			} else {
				stmt.setNull(5, Types.NVARCHAR);
			}
			
			if (bean.getFileId() == null) {
				stmt.setNull(6, Types.BIGINT);
			} else {
				stmt.setLong(6, bean.getFileId());
			}
			
			stmt.executeUpdate();
			rs2 = stmt.getGeneratedKeys();
			if (rs2.next()) {
				recordId = rs2.getLong(1);
				if (bean.getProperties() != null) {
					addPropertiesAudit(recordId, bean.getProperties());
				}
			}
		} catch (Exception e) {
			System.err.println("Error adding DMS Audit record");
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			safeClose();
		}
		return recordId;
	}

	private void addPropertiesAudit(long recordId, Map<String, String> seachedFields) throws DatabaseException {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("INSERT INTO DMS_PROPERTIES_AUDIT (DMS_AUDIT_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (?, ?, ?)");
			for (Map.Entry<String, String> entry : seachedFields.entrySet()) {
				ps.setLong(1, recordId);
				ps.setString(2, entry.getKey());
				ps.setNString(3, entry.getValue());
				ps.execute();
			}
		} catch (Exception e) {
			throw new DatabaseException("Error inserting into table DMS_PROPERTIES_AUDIT", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
