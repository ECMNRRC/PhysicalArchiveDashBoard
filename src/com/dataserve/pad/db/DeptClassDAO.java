package com.dataserve.pad.db;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.DeptClassBean;

public class DeptClassDAO extends AbstractDAO{
	public DeptClassDAO() throws DatabaseException {
		super();
	}
	
	public boolean addClassDept(DeptClassBean bean) throws DatabaseException {
        try {
        	stmt = con.prepareStatement("INSERT INTO CLASS_DEPT (CLASSIFICATION_ID, DEPT_ID, SAVE_PERIOD) VALUES (?, ?, ?)");
        	stmt.setInt(1, bean.getCLASSIFICATION_ID());
        	stmt.setInt(2, bean.getDEPT_ID());
        	stmt.setInt(3, bean.getSAVE_PERIOD());
        	  
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new record to table CLASSIFICTIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	
	 

	public boolean updateClassDept(DeptClassBean bean) throws DatabaseException {
		try {
        	stmt = con.prepareStatement("UPDATE CLASS_DEPT SET CLASSIFICATION_ID = ?, DEPT_ID = ?, SAVE_PERIOD = ? WHERE CLASSIFICATION_ID = ?");
        	stmt.setInt(1, bean.getCLASSIFICATION_ID());
        	stmt.setInt(2, bean.getDEPT_ID());
        	stmt.setInt(3, bean.getSAVE_PERIOD());
        	
        	 
        	 
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating table CLASS_DEPT", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}
	
	public Set<DeptClassBean> fetchCLASSDEPT(int id) throws DatabaseException {
		Set<DeptClassBean> DCBeans=new LinkedHashSet<DeptClassBean>();
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, DEPT_ID, SAVE_PERIOD  FROM CLASS_DEPT WHERE CLASSIFICATION_ID = ?");
			
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DeptClassBean bean = new DeptClassBean();
				bean.setCLASSIFICATION_ID(rs.getInt("CLASSIFICATION_ID"));
				bean.setDEPT_ID(rs.getInt("DEPT_ID"));
				bean.setSAVE_PERIOD(rs.getInt("SAVE_PERIOD"));
				
				DCBeans.add(bean);
			}
			
			 
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASS_DEPT", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return DCBeans;
	}
	
 
	
	 

}