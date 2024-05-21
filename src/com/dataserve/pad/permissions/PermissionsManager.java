package com.dataserve.pad.permissions;

import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.PermissionBean;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.PermissionDAO;

public class PermissionsManager {
	private static boolean initialized = false;
	private static Set<PermissionModel> permissions;
	
	static {
		if (!initialized) {
			try {
				loadPermissions();
			} catch (PermissionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void loadPermissions() throws PermissionException {
		try {
			PermissionDAO dao = new PermissionDAO();
			Set<PermissionBean> beans = dao.fetchAllPermissions();
			permissions = new LinkedHashSet<PermissionModel>();
			for (PermissionBean b : beans) {
				try {
					PermissionModel pm = new PermissionModel(b);
					permissions.add(pm);
				} catch (Exception e) {
					System.err.println("Error loading permission ... ");
					e.printStackTrace();
				}
			}
			initialized = true;
		} catch (DatabaseException e) {
			throw new PermissionException("Error loading permissions list", e);
		}
	}
	
	public static PermissionModel getPermissionById(int id) {
		for (PermissionModel pm : permissions) {
			if (pm.getId() == id) {
				return pm;
			}
		}
		return null;
	}
}
