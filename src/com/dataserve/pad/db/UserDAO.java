package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.UserBean;

public class UserDAO extends   AbstractDAO {

	public UserDAO() throws DatabaseException {
		super();
	}
	
	public boolean addUser(UserBean bean) throws DatabaseException {
        try {
        	stmt = con.prepareStatement("SELECT * FROM USERS WHERE UsernameLDAP = ?");
        	stmt.setString(1, bean.getUsernameLDAP());
        	rs = stmt.executeQuery();
        	if (rs.next()) {
        		throw new DatabaseException("User already exists");
        	}
        	
        	stmt = con.prepareStatement("INSERT INTO USERS(UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID) VALUES (?, ?, ?, ?, ?, ?, ?)");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getUsernameLDAP());
        	if (bean.getEmail() == null || bean.getEmail().trim().equals("")) {
        		stmt.setNull(4, Types.VARCHAR);
        	} else {
        		stmt.setString(4, bean.getEmail());
        	}
        	stmt.setBoolean(5, false);
        	stmt.setBoolean(6, bean.getIsActive());
        	if (bean.getDepartmentId() == null) {
        		stmt.setNull(7, Types.INTEGER);
        	} else {
        		stmt.setInt(7, bean.getDepartmentId());
        	}   
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new record to table User", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	
	public Set<UserBean> fetchUsers(Set<Integer> ids) throws DatabaseException {
		String param = ids.toString().replace("[", "").replace("]", "");
		return fetchUsers(param);
	}
	
	public Set<UserBean> fetchUsers(String ids) throws DatabaseException {
		Set<UserBean> Users = new LinkedHashSet<UserBean>();
		try {
			stmt = con.prepareStatement("SELECT USER_ID, UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM USERS where USER_ID in (" + ids + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				
				bean.setGroupsIds(fetchUserGroupIds(bean.getId()));
				Users.add(bean);
			}
			return Users;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}
	
	public Set<UserBean> fetchUsersByDepId(String ids) throws DatabaseException {
		Set<UserBean> Users = new LinkedHashSet<UserBean>();
		try {
			stmt = con.prepareStatement("SELECT USER_ID, UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM USERS where DEPARTMENT_ID in (" + ids + ")");
			rs = stmt.executeQuery();
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				
				bean.setGroupsIds(fetchUserGroupIds(bean.getId()));
				Users.add(bean);
			}
			return Users;
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}
	
	public UserBean fetchUser(int userId) throws DatabaseException {
		try {
			stmt = con.prepareStatement("SELECT USER_ID,UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM USERS where USER_ID = ?");
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				bean.setGroupsIds(fetchUserGroupIds(bean.getId()));
				return bean;
			} else {
				throw new DatabaseException("User with id '" + userId + "' was not found");
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}
	
	
	public Set<UserBean> fetchAllUsers() throws DatabaseException {
		Set<UserBean> Users = new LinkedHashSet<UserBean>();
	
		try {
			stmt = con.prepareStatement("SELECT USER_ID, UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM USERS");
			rs = stmt.executeQuery();
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				Users.add(bean);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return Users;
	}
	
	@SuppressWarnings("resource")
	public Set<UserBean> fetchUsersWithAllRelations(Set<Integer> ids) throws DatabaseException {
		Set<UserBean> Users = new LinkedHashSet<UserBean>();
		String param = ids.toString().replace("[", "").replace("]", "");
		PreparedStatement stmt2 = null; 
		ResultSet rs2 = null;
		try {
			stmt = con.prepareStatement("SELECT USER_ID, UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM ARCHIVE.dbo.USERS where USER_ID in (" + param + ")");
			rs = stmt.executeQuery();
			UserBean bean = new UserBean();
			while (rs.next()) {
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
			}
		
			stmt2 = con.prepareStatement("SELECT GROUP_ID  FROM USERS_GROUPS where USER_ID in (" + param + " )");	 
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				bean.getGroupsIds(). add(rs2.getInt("GROUP_ID"));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
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
			safeClose();
			releaseResources();
		}
		return Users;
	}
	
	private Set<Integer > fetchUserGroupIds(int userId) throws DatabaseException {
		Set<Integer> groupIds = new LinkedHashSet<Integer>();
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try {
			stmt2 = con.prepareStatement("SELECT GROUP_ID FROM USERS_GROUPS WHERE USER_ID = ?");
			stmt2.setInt(1, userId);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				groupIds.add(rs2.getInt("GROUP_ID"));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
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
		return groupIds;
	}

	public UserBean fetUserByNameLDAP(String userNameLDAP) throws DatabaseException {
		try {
			stmt = con.prepareStatement("SELECT USER_ID, UserArname, UserEnName, UsernameLDAP, UserEmail, IsLogin, IsActive, DEPARTMENT_ID FROM USERS WHERE UsernameLDAP = ?");
			stmt.setString(1, userNameLDAP);
			rs = stmt.executeQuery();
			if (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("USER_ID"));
				bean.setNameAr(rs.getString("UserArname"));
				bean.setNameEn(rs.getString("UserEnName"));
				bean.setUsernameLDAP(rs.getString("UsernameLDAP"));
				bean.setEmail(rs.getString("UserEmail"));
				bean.setIsLogin(rs.getBoolean("IsLogin"));
				bean.setIsActive(rs.getBoolean("IsActive"));
				bean.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				bean.setGroupsIds(fetchUserGroupIds(bean.getId()));
				return bean;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table User", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return null;
	}
	
	public boolean updateUser(UserBean  bean) throws DatabaseException {
		try {
			stmt = con.prepareStatement("SELECT * FROM USERS WHERE UsernameLDAP = ? AND USER_ID != ?");
        	stmt.setString(1, bean.getUsernameLDAP());
        	stmt.setInt(2, bean.getId());
        	rs = stmt.executeQuery();
        	if (rs.next()) {
        		throw new DatabaseException("User already exists");
        	}
        	
        	stmt = con.prepareStatement("UPDATE USERS SET UserArname = ?, UserEnName = ?, UsernameLDAP = ?, UserEmail = ?, IsLogin = ?, IsActive = ?, DEPARTMENT_ID = ? WHERE USER_ID = ?");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getUsernameLDAP());
        	if (bean.getEmail() == null || bean.getEmail().trim().equals("")) {
        		stmt.setNull(4, Types.VARCHAR);
        	} else {
        		stmt.setString(4, bean.getEmail());
        	}
        	stmt.setBoolean(5, bean.getIsLogin());
        	stmt.setBoolean(6, bean.getIsActive());
        	if (bean.getDepartmentId() != null) {
        		stmt.setInt(7, bean.getDepartmentId());
        	} else {
        		stmt.setNull(7, Types.INTEGER);
        	}   	
        	stmt.setInt(8, bean.getId());
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating table User", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}
	
	public int deleteUser(String Userstr) throws DatabaseException {
		String params = Userstr.replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("DELETE FROM USERS WHERE USER_ID IN (" + params + ")");
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error delete from users table", e);
		} finally {
            safeClose();
            releaseResources();
        }
	}

	public int disableUser(String Userstr) throws DatabaseException {
		String params = Userstr.replace("[", "").replace("]", "");
		try {
			stmt = con.prepareStatement("UPDATE USERS SET IsActive = 0 WHERE USER_ID IN (" + params + ")");
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error update users table", e);
		} finally {
            safeClose();
            releaseResources();
        }
	}

	public Set<Integer> fetchUserPermissionIds(UserBean bean) throws DatabaseException {
		Set<Integer> permissionIds = new LinkedHashSet<Integer>();
		StringBuilder query = new StringBuilder(400);
		query.append("SELECT GROUP_PERMISSIONS.PERMISSION_ID PERMISSION_ID ");
		query.append("FROM GROUP_PERMISSIONS GROUP_PERMISSIONS ");
		query.append("JOIN USERS_GROUPS USERS_GROUPS ON (GROUP_PERMISSIONS.GROUP_ID = USERS_GROUPS.GROUP_ID) ");
		query.append("WHERE USERS_GROUPS.USER_ID = ?");
		try {
			stmt = con.prepareStatement(query.toString());
			stmt.setInt(1, bean.getId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				permissionIds.add(rs.getInt("PERMISSION_ID"));
			}
			return permissionIds;
		} catch (Exception e) {
			throw new DatabaseException("Error fetching user permission ids", e);
		} finally {
            safeClose();
            releaseResources();
        }
	}

	public boolean updateUserSignature(UserBean bean) throws DatabaseException {
		try {
			
        	
        	stmt = con.prepareStatement("UPDATE USERS SET UserSignatureBas64 = ? WHERE USER_ID = ?");
        	stmt.setString(1, bean.getUserSignature());
        	stmt.setInt(2, bean.getId());
        	
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating table User", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}
}