package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.DepartmentBean;

public class DepartmentsDAO extends AbstractDAO {
	public DepartmentsDAO() throws DatabaseException {
		super();
	}
	
	public boolean addDepartment(DepartmentBean bean) throws DatabaseException {
        try {
        	
        	stmt = con.prepareStatement("SELECT * FROM DEPARTMENTS WHERE DEPT_CODE = ?");
        	stmt.setString(1, bean.getCode());
        	rs = stmt.executeQuery();
        	if (rs.next()) {
        		throw new DatabaseException("Department already exists");
        	}
        	
        	if(bean.getIsArchiveCenter() == 1) {
	        	stmt = con.prepareStatement("SELECT * FROM DEPARTMENTS WHERE IS_ARCHIVE_CENTER = 1");
	        	rs = stmt.executeQuery();
	        	if (rs.next()) {
	        		throw new DatabaseException("Archived department already exists");
	        	}
        	}
        	
        	stmt = con.prepareStatement("INSERT INTO DEPARTMENTS(DEPT_AR_NAME,DEPT_EN_NAME,ENABLED,DEPT_CODE,DEPT_EMAIL,IS_ARCHIVE_CENTER,PARENT_ID) VALUES (?, ?, ?, ?,?,?,?)");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setBoolean(3, bean.isEnabled());
        	stmt.setNString(4, bean.getCode());
        	stmt.setString(5, bean.getDeptEmail());

        	stmt.setInt(6, bean.getIsArchiveCenter());
        	if (bean.getParentId() != null) {
        		stmt.setInt(7, bean.getParentId());
        	} else {
        		stmt.setNull(7, Types.INTEGER);
        	}
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new record to table DEPARTMENTS", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	
	public Set<DepartmentBean> fetchDepartments (Set<Integer> ids) throws DatabaseException {
		Set<DepartmentBean> departments = new LinkedHashSet<DepartmentBean>();
		if (ids.size() < 1) {
			return departments;
		}
		String param = ids.toString().replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("SELECT DEPT_ID,DEPT_AR_NAME ,DEPT_EN_NAME ,ENABLED,DEPT_CODE ,DEPT_EMAIL,MANAGER_USER_ID,IS_ARCHIVE_CENTER,PARENT_ID  FROM DEPARTMENTS where DEPT_ID in (" + param + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {		
				DepartmentBean bean = new DepartmentBean();		
				bean.setId(rs.getInt("DEPT_ID"));
				bean.setNameAr(rs.getNString("DEPT_AR_NAME"));
				bean.setNameEn(rs.getString("DEPT_EN_NAME"));
				bean.setEnabled(rs.getBoolean("ENABLED"));
				bean.setCode(rs.getNString("DEPT_CODE"));
				bean.setDeptEmail(rs.getString("DEPT_EMAIL"));
				bean.setManagerId(rs.getInt("MANAGER_USER_ID"));
				bean.setIsArchiveCenter(rs.getInt("IS_ARCHIVE_CENTER"));
				bean.setParentId(rs.getInt("PARENT_ID"));
				bean.setChildrenIds(getChildrenIds(bean.getId()));
				bean.setUsersIds(fetchDepartmentUsers(bean.getId()));
				bean.setStorageCenters(fetchDepartmentStorageCenters(bean.getId()));
				bean.setClassificationIds(fetchDepartmentClassificationIds(bean.getId()));
				departments.add(bean);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table DEPARTMENTS", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return departments;
	}
	
	public Set<DepartmentBean> fetchDepartments() throws DatabaseException {
		Set<DepartmentBean> departments = new LinkedHashSet<DepartmentBean>();
		try {
			stmt = con.prepareStatement("SELECT DEPT_ID,DEPT_AR_NAME ,DEPT_EN_NAME ,ENABLED,DEPT_CODE,PARENT_ID FROM DEPARTMENTS");
			rs = stmt.executeQuery();
			while (rs.next()) {		
				DepartmentBean bean = new DepartmentBean();		
				bean.setId(rs.getInt("DEPT_ID"));
				bean.setNameAr(rs.getNString("DEPT_AR_NAME"));
				bean.setNameEn(rs.getString("DEPT_EN_NAME"));
				bean.setEnabled(rs.getBoolean("ENABLED"));
				bean.setCode(rs.getNString("DEPT_CODE"));
				bean.setParentId(rs.getInt("PARENT_ID"));
				bean.setChildrenIds(getChildrenIds(bean.getId()));
				bean.setUsersIds(fetchDepartmentUsers(bean.getId()));
				bean.setStorageCenters(fetchDepartmentStorageCenters(bean.getId()));
				bean.setClassificationIds(fetchDepartmentClassificationIds(bean.getId()));
				departments.add(bean);					
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table DEPARTMENTS", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return departments;
	}
	
	private Set<Integer> getChildrenIds(int parentId) throws DatabaseException {
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		Set<Integer> results = new LinkedHashSet<Integer>();
		try {
			stmt2 = con.prepareStatement("SELECT DEPT_ID,DEPT_AR_NAME ,DEPT_EN_NAME ,ENABLED,DEPT_CODE ,DEPT_EMAIL,MANAGER_USER_ID,IS_ARCHIVE_CENTER,PARENT_ID  FROM DEPARTMENTS WHERE PARENT_ID = ?");
			stmt2.setInt(1, parentId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				results.add(rs2.getInt("DEPT_ID"));
			}
			return results;
		} catch (Exception e) {
			throw new DatabaseException("Error loading children of DEPARTMENTS with id " + parentId, e);
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

	
	public DepartmentBean fetchArchivedDepartment() throws DatabaseException {
		DepartmentBean department = new DepartmentBean();
		try {
			stmt = con.prepareStatement("SELECT IS_ARCHIVE_CENTER,DEPT_AR_NAME ,DEPT_EN_NAME  FROM DEPARTMENTS WHERE IS_ARCHIVE_CENTER = 1");
			rs = stmt.executeQuery();
			while (rs.next()) {		
				DepartmentBean bean = new DepartmentBean();		
				bean.setIsArchiveCenter(rs.getInt("IS_ARCHIVE_CENTER"));
				bean.setNameAr(rs.getNString("DEPT_AR_NAME"));
				bean.setNameEn(rs.getString("DEPT_EN_NAME"));
				bean.setUsersIds(fetchDepartmentUsers(bean.getId()));
				bean.setStorageCenters(fetchDepartmentStorageCenters(bean.getId()));
				bean.setClassificationIds(fetchDepartmentClassificationIds(bean.getId()));
				department = bean;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching archived record from table DEPARTMENTS", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return department;
	}
	
	private Set<Integer> fetchDepartmentClassificationIds(int id) {
		Set<Integer> deptClasses = new LinkedHashSet<Integer>();
		PreparedStatement stmt2 = null; 
		ResultSet rs2 = null;		
		try {
			stmt2 = con.prepareStatement("SELECT CLASSIFICATION_ID FROM CLASS_DEPT where DEPT_ID = ?");
			stmt2.setInt(1, id);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				deptClasses.add(rs2.getInt("CLASSIFICATION_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return deptClasses;
	}

	private Set<Integer> fetchDepartmentUsers(int deptId) {
		Set<Integer> deptUsers = new LinkedHashSet<Integer>();
		PreparedStatement stmt2 = null; 
		ResultSet rs2 = null;		
		try {
			stmt2 = con.prepareStatement("SELECT USER_ID FROM USERS where DEPARTMENT_ID = ?");
			stmt2.setInt(1, deptId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				deptUsers.add(rs2.getInt("USER_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return deptUsers;
	}
	
	private Set<Integer> fetchDepartmentStorageCenters(int deptId) {
		Set<Integer> deptCenters = new LinkedHashSet<Integer>();
		PreparedStatement stmt2 = null; 
		ResultSet rs2 = null;		
		try {
			stmt2 = con.prepareStatement("SELECT CENTER_ID FROM STORAGE_CENTER WHERE DEPT_ID = ?");
			stmt2.setInt(1, deptId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				deptCenters.add(rs2.getInt("CENTER_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return deptCenters;
	}

	public int updateDepartment(DepartmentBean bean) throws DatabaseException {
		try {
			
			// first check if department code already exists (in another department) before adding to db
        	stmt = con.prepareStatement("SELECT * FROM DEPARTMENTS WHERE DEPT_CODE = ? AND DEPT_ID != ?");
        	stmt.setString(1, bean.getCode());
        	stmt.setInt(2, bean.getId());
        	rs = stmt.executeQuery();
        	if (rs.next()) {
        		throw new DatabaseException("Department already exists");
        	}
        	
        	// check if there is an already archived department before adding to db
        	if(bean.getIsArchiveCenter() == 1) {
	        	stmt = con.prepareStatement("SELECT * FROM DEPARTMENTS WHERE IS_ARCHIVE_CENTER = 1 AND DEPT_ID != ?");
	        	stmt.setInt(1, bean.getId());
	        	rs = stmt.executeQuery();
	        	if (rs.next()) {
	        		throw new DatabaseException("Archived department already exists");
	        	}
        	}
        	
        	stmt = con.prepareStatement("UPDATE DEPARTMENTS SET DEPT_AR_NAME = ?, DEPT_EN_NAME = ?, ENABLED = ?, DEPT_CODE = ? ,DEPT_EMAIL = ? ,MANAGER_USER_ID = ? ,IS_ARCHIVE_CENTER=?,PARENT_ID=? WHERE DEPT_ID = ?");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setBoolean(3, bean.isEnabled());
        	stmt.setNString(4, bean.getCode());
        	stmt.setString(5, bean.getDeptEmail());
        	if(bean.getManagerId()==0) {
        		stmt.setNull(6, Types.INTEGER);
        	} else {
               stmt.setInt	(6, bean.getManagerId());
        	}
        	stmt.setInt(7, bean.getIsArchiveCenter());
        	if (bean.getParentId() != null) {
        		stmt.setInt(8, bean.getParentId());
        	} else {
        		stmt.setNull(8, Types.INTEGER);
        	}
        	stmt.setInt(9, bean.getId());
        
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating table DEPARTMENTS", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}
	
	public int deleteDepartments(String departmentIdStr) throws DatabaseException {
		String params = departmentIdStr.replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("DELETE FROM DEPARTMENTS WHERE DEPT_ID IN (" + params + ")");
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error delete from DEPARTMENTS table", e);
		} finally {
            safeClose();
            releaseResources();
        }
	}

	public int disableDepartments(String departmentIdStr) throws DatabaseException {
		String params = departmentIdStr.replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("UPDATE DEPARTMENTS SET ENABLED = 0 WHERE DEPT_ID IN (" + params + ")");
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error update DEPARTMENTS table", e);
		} finally {
            safeClose();
            releaseResources();
        }
	}

}