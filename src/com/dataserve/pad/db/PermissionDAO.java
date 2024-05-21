package com.dataserve.pad.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.PermissionBean;
import com.dataserve.pad.permissions.PermissionException;

public class PermissionDAO  extends AbstractDAO {
	
	public PermissionDAO() throws DatabaseException {
		super();
	}
	
	public boolean addPermission(PermissionBean bean) throws DatabaseException {
        try {
        	stmt = con.prepareStatement("INSERT INTO PERMISSIONS(PermissionArName, PermissionEnName, PermissionDescription, Enabled, MODULE_ID, ACTION_TYPE_ID) VALUES (?, ?, ?, ?, ?, ?)");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getDescription());
        	stmt.setBoolean(4, bean.isEnabled());
        	stmt.setInt(5, bean.getModuleId());
        	stmt.setInt(6, bean.getTypeId());
        	 
            return stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new record to table PERMISSIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
    }
	
	public PermissionBean fetchPermission (Integer id) throws DatabaseException {
		Set<PermissionBean> results = fetchPermissions(id + "");
		if (results.size() == 0) {
			throw new DatabaseException("Permission with id '" + id + "' does not exist!");
		}
		return results.iterator().next();
	}
	
	public Set<PermissionBean> fetchPermissions (Set<Integer> ids) throws DatabaseException {
		String stringIds = ids.toString().replace("[", "").replace("]", "");
		return fetchPermissions(stringIds);
	}
	
	public Set<PermissionBean> fetchPermissions (String ids) throws DatabaseException {
		Set<PermissionBean> Permissions = new LinkedHashSet<PermissionBean>();
		String statement = "SELECT PermissionID, PermissionArName, PermissionEnName, PermissionDescription, MODULE_ID, ACTION_TYPE_ID, Enabled FROM PERMISSIONS";
		if (ids != null && !ids.equals("ALL")) {
			statement +=  " WHERE PermissionID in (" + ids + ")";
		}
		PreparedStatement stmt2 = null; 
		ResultSet rs2 = null;
		try {
			stmt = con.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PermissionBean bean = new PermissionBean();
				
				bean.setId(rs.getInt("PermissionID"));
				bean.setNameAr(rs.getString("PermissionArName"));
				bean.setNameEn(rs.getString("PermissionEnName"));
				bean.setDescription(rs.getString("PermissionDescription"));
				bean.setModuleId(rs.getInt("MODULE_ID"));
				bean.setTypeId(rs.getInt("ACTION_TYPE_ID"));
				bean.setEnabled(rs.getBoolean("Enabled"));
			
				stmt2 = con.prepareStatement("SELECT  GROUP_ID FROM  GROUP_PERMISSIONS where PERMISSION_ID  = ?");
				stmt2.setInt(1, bean.getId());
				rs2 = stmt2.executeQuery();
				bean.setGroupIds(new LinkedHashSet<Integer>());
				while (rs2.next()) {
					bean.getGroupIds().add(rs2.getInt("GROUP_ID"));
				}
				
				Permissions.add(bean);				 		
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table PERMISSIONS", e);
		} finally {
			safeClose();
			releaseResources();
			
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
		return Permissions;
	}

	public Set<PermissionBean> fetchAllPermissions() throws DatabaseException {
		return fetchPermissions("ALL");
	}

	public void updatePermission(PermissionBean  bean) throws DatabaseException {
		try {
        	stmt = con.prepareStatement("UPDATE PERMISSIONS SET PermissionArName = ?, PermissionEnName = ?, PermissionDescription = ?, Enabled = ?, MODULE_ID = ?, ACTION_TYPE_ID = ? WHERE PermissionID = ?");
        	stmt.setNString(1, bean.getNameAr());
        	stmt.setString(2, bean.getNameEn());
        	stmt.setString(3, bean.getDescription());
        	stmt.setBoolean(4, bean.isEnabled());
        	stmt.setInt(5, bean.getModuleId());
        	stmt.setInt(6, bean.getTypeId());
        	stmt.setInt(7, bean.getId());
            stmt.execute();
            updatePermissionGroups(bean);
        } catch (SQLException e) {
            throw new DatabaseException("Error updating table PERMISSIONS", e);
        } catch (PermissionException e) {
        	throw new DatabaseException("Error updating relation in GROUP_PERMISSIONS", e);
        } finally {
            safeClose();
            releaseResources();
        }
	}

	private void updatePermissionGroups(PermissionBean bean) throws PermissionException {
		PreparedStatement stmt2 = null;
		try {
			con.setAutoCommit(false);
			stmt2 = con.prepareStatement("DELETE FROM GROUP_PERMISSIONS WHERE PERMISSION_ID = ?");
			stmt2.setInt(1, bean.getId());
			stmt2.executeUpdate();
			
			for (int groupId : bean.getGroupIds()) {
				try {
					stmt2 = con.prepareStatement("INSERT INTO GROUP_PERMISSIONS (GROUP_ID, PERMISSION_ID) VALUES (?, ?)");
					stmt2.setInt(1, groupId);
					stmt2.setInt(2, bean.getId());
					stmt2.executeUpdate();
				} catch (Exception ne) {
					System.err.println("Error inserting into GROUP_PERMISSIONS");
					ne.printStackTrace();
				}
			}
			
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Error occurred while rolling back deleting groups from permission with id '" + bean.getId() + "'");
				e1.printStackTrace();
			}
			throw new PermissionException("Error updating GROUP_PERMISSIONS table", e);
		} finally {
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	public void addPermissionGroups(int permissionId, String groupIds) throws DatabaseException {
		try {
			stmt = con.prepareStatement("INSERT INTO GROUP_PERMISSIONS (PERMISSION_ID, GROUP_ID) VALUES (?, ?)");
			for (String groupId : groupIds.replace("[", "").replace("]", "").split(",")) {
				stmt.setInt(1, permissionId);
				stmt.setInt(2, Integer.parseInt(groupId));
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			throw new DatabaseException("Error inserting into table GROUP_PERMISSIONS", e);
		} finally {
			safeClose();
            releaseResources();
		}
	}
}
