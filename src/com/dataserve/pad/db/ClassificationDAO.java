package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.ClassificationBean;
import com.dataserve.pad.business.classification.DeptClassModel;

public class ClassificationDAO extends AbstractDAO{
	public ClassificationDAO() throws DatabaseException {
		super();
	}
	
	public boolean addClassification(ClassificationBean bean) throws DatabaseException {
        try {
        	stmt = con.prepareStatement("INSERT INTO CLASSIFICTIONS (CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE) VALUES (?, ?, ?, ?, ?, ?)");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getSymbolicName());
        	stmt.setInt(4, bean.getParentID());
        	stmt.setString(5, bean.getClassCode());
        	stmt.setInt(6, bean.getSaveTypeId());
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new record to table CLASSIFICTIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	public boolean addClassification(ClassificationBean bean,Set<DeptClassModel> DCModel) throws DatabaseException {
		try {
        	con.setAutoCommit(false);
        	stmt = con.prepareStatement("INSERT INTO CLASSIFICTIONS (CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID,CLASS_CODE, SAVE_TYPE) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getSymbolicName());
        	stmt.setInt(4, bean.getParentID());
        	stmt.setString(5, bean.getClassCode());
        	stmt.setInt(6, bean.getSaveTypeId());
            if (stmt.executeUpdate() > 0){
            	ResultSet rs = stmt.getGeneratedKeys();
            	if (rs.next()) {
            		for (DeptClassModel DCModele : DCModel) {
	            		stmt = con.prepareStatement("INSERT INTO CLASS_DEPT (CLASSIFICATION_ID, DEPT_ID, SAVE_PERIOD) VALUES (?, ?, ?)");
	            		stmt.setInt(1, rs.getInt(1));
	            		stmt.setInt(2, DCModele.getDEPT_ID());
	            		stmt.setInt(3, DCModele.getSAVE_PERIOD());
	            		stmt.execute();
            		}
            	}
            	con.commit();
                return true;
            } else {
            	try{
                	con.rollback();
                } catch (Exception ex ){
                    System.err.println("Error rollingback classification entry");
                    ex.printStackTrace();
                }
            	return false;
            }
        } catch (SQLException e) {
        	try{
        		con.rollback();
        	} catch (Exception ex ){
        		System.err.println("Error rollingback classification entry");
                ex.printStackTrace();
        	}
        	throw new DatabaseException("Error adding new record to table CLASSIFICTIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	
	public boolean updateClassififcation(ClassificationBean bean,Set<DeptClassModel> DCModel) throws DatabaseException {
		try {
			con.setAutoCommit(false);
        	stmt = con.prepareStatement("UPDATE CLASSIFICTIONS SET CLASS_AR_NAME = ?, CLASS_EN_NAME = ?, SYMPOLIC_NAME = ?, CLASS_CODE = ?, SAVE_TYPE = ? WHERE CLASSIFICATION_ID = ?");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getSymbolicName());
        	stmt.setString(4, bean.getClassCode());
        	stmt.setInt(5, bean.getSaveTypeId());
        	stmt.setInt(6, bean.getId());
        	if (stmt.executeUpdate()>0){
        		stmt = con.prepareStatement("DELETE FROM CLASS_DEPT where CLASSIFICATION_ID =?");
        		stmt.setInt(1,bean.getId());
        		if (stmt.executeUpdate() >= 0){
        			
              		for (DeptClassModel DCModele : DCModel) {            
              			stmt = con.prepareStatement("INSERT INTO CLASS_DEPT (CLASSIFICATION_ID, DEPT_ID, SAVE_PERIOD) VALUES (?, ?, ?)");
              			stmt.setInt(1,bean.getId());
              			stmt.setInt(2, DCModele.getDEPT_ID());
              			stmt.setInt(3, DCModele.getSAVE_PERIOD());
              			stmt.execute();
              		}
        		}
              	con.commit();
            } else {
            	try{
                	con.rollback();
                } catch (Exception ex ){
                	System.err.println("Error rollingback classification update");
                    ex.printStackTrace();
                }
            	return false;
            }
        	return true;
        } catch (SQLException e) {
        	try{
        		con.rollback();
        	} catch (Exception ex ){
        		System.err.println("Error rollingback classification update");
                ex.printStackTrace();
        	}
        	throw new DatabaseException("Error updateing record in table CLASSIFICTIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}
	
	public boolean isSymbolicNameInUse(String symbolicName) throws DatabaseException {
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = con.prepareStatement("SELECT CLASSIFICATION_ID FROM CLASSIFICTIONS WHERE SYMPOLIC_NAME = ?");
			st.setString(1, symbolicName);
			r = st.executeQuery();
			return r.next();
		} catch (SQLException e) {
			throw new DatabaseException("Error validating symbolic name uniqueness for classification with symbolicName" + symbolicName, e);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Set<ClassificationBean> fetchClassificationsByUserId(int id) throws DatabaseException {		
		try {
			stmt = con.prepareStatement("SELECT distinct CLASSIFICTIONS.CLASSIFICATION_ID CLASSIFICATION_ID, CLASSIFICTIONS.CLASS_AR_NAME CLASS_AR_NAME, " +
				"CLASSIFICTIONS.CLASS_EN_NAME CLASS_EN_NAME, CLASSIFICTIONS.SYMPOLIC_NAME SYMPOLIC_NAME, " +
				"CLASSIFICTIONS.PARENT_ID PARENT_ID, CLASSIFICTIONS.CLASS_CODE CLASS_CODE, CLASSIFICTIONS.SAVE_TYPE SAVE_TYPE " +
				"FROM CLASSIFICTIONS CLASSIFICTIONS " +
                "INNER JOIN CLASS_DEPT CLASS_DEPT ON CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID " + 
                "INNER JOIN DEPARTMENTS DEPARTMENTS ON CLASS_DEPT.DEPT_ID = DEPARTMENTS.DEPT_ID " + 
                "INNER JOIN USERS USERS ON DEPARTMENTS.DEPT_ID = USERS.DEPARTMENT_ID " + 
				"WHERE USERS.USER_ID = ? ");
			
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();			
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				bean.setChildrenIds(getChildrenIds(id,bean.getId()));
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS BY User ID ", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}

	public Set<ClassificationBean> fetchAvailableClassificationsByUserId(int userId) throws DatabaseException {
		try {
			stmt = con.prepareStatement("SELECT distinct CLASSIFICTIONS.CLASSIFICATION_ID CLASSIFICATION_ID, CLASSIFICTIONS.CLASS_AR_NAME CLASS_AR_NAME, " +
				"CLASSIFICTIONS.CLASS_EN_NAME CLASS_EN_NAME, CLASSIFICTIONS.SYMPOLIC_NAME SYMPOLIC_NAME, " +
				"FROM CLASSIFICTIONS CLASSIFICTIONS " +
                "INNER JOIN CLASS_DEPT CLASS_DEPT ON CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID " + 
                "INNER JOIN DEPARTMENTS DEPARTMENTS ON CLASS_DEPT.DEPT_ID = DEPARTMENTS.DEPT_ID " + 
                "INNER JOIN USERS USERS ON DEPARTMENTS.DEPT_ID = USERS.DEPARTMENT_ID " + 
				"WHERE is_fn_added = 1 AND USERS.USER_ID = ? ");
			
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();			
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				beans.add(bean);
			}
			return beans;
		} catch (Exception e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS BY User ID ", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}

	public Set<ClassificationBean> fetchClassificationsByUserAndStorageCenter(int userId, int storageCenterId) throws DatabaseException {
		try {
			stmt = con.prepareStatement("SELECT distinct " +
				"CLASSIFICTIONS.CLASSIFICATION_ID CLASSIFICATION_ID, CLASSIFICTIONS.CLASS_AR_NAME CLASS_AR_NAME, CLASSIFICTIONS.CLASS_EN_NAME CLASS_EN_NAME, " + 
				"CLASSIFICTIONS.SYMPOLIC_NAME SYMPOLIC_NAME, CLASSIFICTIONS.PARENT_ID PARENT_ID, CLASSIFICTIONS.CLASS_CODE CLASS_CODE, CLASSIFICTIONS.SAVE_TYPE " +
				"FROM CLASSIFICTIONS CLASSIFICTIONS " + 
				"INNER JOIN CLASS_DEPT CLASS_DEPT ON CLASSIFICTIONS.CLASSIFICATION_ID = CLASS_DEPT.CLASSIFICATION_ID " +
                "INNER JOIN DEPARTMENTS DEPARTMENTS ON CLASS_DEPT.DEPT_ID = DEPARTMENTS.DEPT_ID " + 
                "INNER JOIN USERS USERS ON DEPARTMENTS.DEPT_ID = USERS.DEPARTMENT_ID " + 
                "INNER JOIN STORAGE_CENTER CENTER ON DEPARTMENTS.DEPT_ID = CENTER.DEPT_ID " + 
				"WHERE USERS.USER_ID = ? AND CENTER.CENTER_ID = ?");
			stmt.setInt(1, userId);
			stmt.setInt(2, storageCenterId);
			rs = stmt.executeQuery();
			Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();			
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				bean.setChildrenIds(getChildrenIds(userId, bean.getId()));
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS BY User ID ", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}
	
	public Set<ClassificationBean> fetchClassificationsByIds(String ids) throws DatabaseException {		
		String params = ids.replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE ,is_fn_added FROM CLASSIFICTIONS WHERE CLASSIFICATION_ID IN (" + params + ")");		
			rs = stmt.executeQuery();
			Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();			
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				bean.setisFnAdded(rs.getBoolean("is_fn_added"));
				bean.setChildrenIds(getChildrenIds(bean.getId()));
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS BY User ID ", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}

	public Set<ClassificationBean> fetchAllClassifications() throws DatabaseException {
	    try {
	        stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME FROM CLASSIFICTIONS");		
	        rs = stmt.executeQuery();
	        Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();			
	        while (rs.next()) {
	            ClassificationBean bean = new ClassificationBean();
	            bean.setId(rs.getInt("CLASSIFICATION_ID"));
	            bean.setNameAr(rs.getString("CLASS_AR_NAME"));
	            
	            String classEnName = rs.getString("CLASS_EN_NAME");
	            if (classEnName == null || classEnName.trim().isEmpty()) {
	                bean.setNameEn(rs.getString("CLASS_AR_NAME"));
	            } else {
	                bean.setNameEn(classEnName);
	            }
	            
	            bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
	            bean.setChildrenIds(getChildrenIds(bean.getId()));
	            beans.add(bean);
	        }
	        return beans;
	    } catch (SQLException e) {
	        throw new DatabaseException("Error fetching record from table CLASSIFICTIONS", e);
	    } finally {
	        safeClose();
	        releaseResources();
	    }
	}

	
	private Set<Integer> getChildrenIds(int parentId) throws DatabaseException {
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<Integer> results = new LinkedHashSet<Integer>();
		try {
			stmt2 = con.prepareStatement("SELECT CLASSIFICATION_ID FROM CLASSIFICTIONS WHERE PARENT_ID = ?");
			stmt2.setInt(1, parentId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				results.add(rs2.getInt("CLASSIFICATION_ID"));
			}
			return results;
		} catch (Exception e) {
			throw new DatabaseException("Error loading children of classification with id " + parentId, e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	private Set<Integer> getChildrenIds( int UserId,int parentId) throws DatabaseException {
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<Integer> results = new LinkedHashSet<Integer>();
		try {
			stmt2 = con.prepareStatement("SELECT CLASSIFICTIONS.CLASSIFICATION_ID FROM     CLASSIFICTIONS INNER JOIN " +
                 " CLASS_DEPT ON CLASSIFICTIONS.[CLASSIFICATION_ID] = CLASS_DEPT.CLASSIFICATION_ID INNER JOIN " +
                " DEPARTMENTS ON CLASS_DEPT.DEPT_ID = DEPARTMENTS.DEPT_ID INNER JOIN " +
                 " USERS ON DEPARTMENTS.DEPT_ID = USERS.DEPARTMENT_ID " +
				   " WHERE USERS.USER_ID = ? AND CLASSIFICTIONS.PARENT_ID = ?");
			stmt2.setInt(1, UserId);
			stmt2.setInt(2, parentId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				results.add(rs2.getInt("CLASSIFICATION_ID"));
			}
			return results;
		} catch (Exception e) {
			throw new DatabaseException("Error loading children of classification with id " + parentId, e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
		}
	}

	@SuppressWarnings("resource")
	public Set<ClassificationBean> fetchClassification(int  userId, Set<Integer> ids) throws DatabaseException {
		String params = ids.toString().replace("[", "").replace("]", "");
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE FROM CLASSIFICTIONS WHERE CLASSIFICATION_ID IN (" + params + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				
				stmt2 = con.prepareStatement("SELECT DEPT_ID FROM  CLASS_DEPT where  CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setDeptsIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getDeptsIds().add(rs2.getInt("DEPT_ID"));
				}

				stmt2 = con.prepareStatement("SELECT FOLDER_ID FROM FOLDER WHERE CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setFoldersIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getFoldersIds().add(rs2.getInt("FOLDER_ID"));
				}

				bean.setChildrenIds(getChildrenIds( userId,bean.getId()));
				
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS", e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
			safeClose();
			releaseResources();
		}
	}

	public Set<ClassificationBean> fetchAvailableClassification(int userId, Set<Integer> childrenIds)  throws DatabaseException {
		String params = childrenIds.toString().replace("[", "").replace("]", "");
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE FROM CLASSIFICTIONS WHERE is_fn_added = 1 AND CLASSIFICATION_ID IN (" + params + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				bean.setChildrenIds(getChildrenIds(userId, bean.getId()));
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS", e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
			safeClose();
			releaseResources();
		}
	}

	@SuppressWarnings("resource")
	public Set<ClassificationBean> fetchClassification(Set<Integer> ids) throws DatabaseException {
		String params = ids.toString().replace("[", "").replace("]", "");
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<ClassificationBean> beans = new LinkedHashSet<ClassificationBean>();
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE FROM CLASSIFICTIONS WHERE CLASSIFICATION_ID IN (" + params + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				
				stmt2 = con.prepareStatement("SELECT DEPT_ID FROM  CLASS_DEPT where  CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setDeptsIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getDeptsIds().add(rs2.getInt("DEPT_ID"));
				}

				stmt2 = con.prepareStatement("SELECT FOLDER_ID FROM FOLDER WHERE CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setFoldersIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getFoldersIds().add(rs2.getInt("FOLDER_ID"));
				}

				bean.setChildrenIds(getChildrenIds(bean.getId()));
				
				beans.add(bean);
			}
			return beans;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS", e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
			safeClose();
			releaseResources();
		}
	}

	@SuppressWarnings("resource")
	public ClassificationBean fetchClassificationBySymbolicName(String className) throws DatabaseException {
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try {
			stmt = con.prepareStatement("SELECT CLASSIFICATION_ID, CLASS_AR_NAME, CLASS_EN_NAME, SYMPOLIC_NAME, PARENT_ID, CLASS_CODE, SAVE_TYPE FROM CLASSIFICTIONS WHERE SYMPOLIC_NAME = ?");
			stmt.setString(1, className);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ClassificationBean bean = new ClassificationBean();
				bean.setId(rs.getInt("CLASSIFICATION_ID"));
				bean.setNameAr(rs.getString("CLASS_AR_NAME"));
				bean.setNameEn(rs.getString("CLASS_EN_NAME"));
				bean.setSymbolicName(rs.getString("SYMPOLIC_NAME"));
				bean.setParentID(rs.getInt("PARENT_ID"));
				bean.setClassCode(rs.getString("CLASS_CODE"));
				bean.setSaveTypeId(rs.getInt("SAVE_TYPE"));
				stmt2 = con.prepareStatement("SELECT DEPT_ID FROM  CLASS_DEPT where  CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setDeptsIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getDeptsIds().add(rs2.getInt("DEPT_ID"));
				}

				stmt2 = con.prepareStatement("SELECT FOLDER_ID FROM FOLDER WHERE CLASSIFICATION_ID = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setFoldersIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getFoldersIds().add(rs2.getInt("FOLDER_ID"));
				}

				bean.setChildrenIds(getChildrenIds(bean.getId()));
				return bean;
			} else {
				throw new DatabaseException("Classification with symbolic name '" + className + "' does not exist in database");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CLASSIFICTIONS", e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {}
			}
			safeClose();
			releaseResources();
		}
	}
	
	public boolean deleteClass(String ClassId) throws DatabaseException {
		String params = ClassId.replace("[", "").replace("]", "");
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement("DELETE FROM CLASS_DEPT WHERE CLASSIFICATION_ID IN (" + params + ")");
        	
			if (stmt.executeUpdate() > 0){
				stmt = con.prepareStatement("DELETE FROM CLASSIFICTIONS WHERE CLASSIFICATION_ID IN (" + params + ")");
				if (stmt.executeUpdate() > 0){
	            	con.commit();
	                return true;
	            } else {
            		con.rollback();
            		return false;
            	}
			} else {
            	try{
                	con.rollback();
                } catch (Exception ex ){
                    throw new DatabaseException("Error adding new record to table CLASSIFICTIONS", ex);
                }
            	return false;
            }	
		} catch (SQLException e) {
        	try{
        		con.rollback();
        	} catch (Exception ex ){
        		System.err.println("Error deleting new record to table CLASSIFICTIONS");
        		ex.printStackTrace();
        	}
        	throw new DatabaseException("Error deleting new record to table CLASSIFICTIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}

}