package com.dataserve.pad.business.departments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.dataserve.pad.bean.ClassificationBean;
import com.dataserve.pad.bean.DepartmentBean;
import com.dataserve.pad.business.classification.ClassificationModel;
import com.dataserve.pad.business.users.UserException;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.DepartmentsDAO;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DepartmentModel {
	private DepartmentBean bean;
	private Set<DepartmentModel> children = new LinkedHashSet<DepartmentModel>();
	private DepartmentModel parent;
	
	public DepartmentModel getParent() {
		return parent;
	}

	public void setParent(DepartmentModel parent) {
		this.parent = parent;
	}

	public DepartmentModel(DepartmentBean bean) {
		this.bean = bean;
	}
	
	public int getDepartmentId() {
		return bean.getId();
	}
	
	public String getDepartmentNameAr() {
		return bean.getNameAr();
	}
	
	public int getSavePeriod() {
		return bean.getId();
	}
	
	public String setSavePeriod() {
		return bean.getNameAr();
	}
	
	public void setDepartmentNameAr(String nameAr){
		bean.setNameAr(nameAr);
	}
	
	public String getDepartmentNameEn() {
		return bean.getNameEn();
	}
	
	public void setDepartmentNameEn(String nameEn){
		bean.setNameEn(nameEn);
	}
	public String getDepartmentEmail() {
		return bean.getDeptEmail();
	}
	
	public void setDepartmentEmail(String email){
		bean.setDeptEmail(email);
	}
	
	public String getDepartmentCode() {
		return bean.getCode();
	}
	
	public void setDepartmentCode(String code) {
		bean.setCode(code);
	}
	
	public boolean isEnabled() {
		return bean.isEnabled();
	}
	
	public void setEnabled(boolean isEnabled) {
		bean.setEnabled(isEnabled);
	}
	public int isArchiveCenter() {
		return bean.getIsArchiveCenter();
	}
	
	public void setIsArchiveCenter(int isArchiveCenter) {
		bean.setIsArchiveCenter(isArchiveCenter);
	}
	public int getManagerId() {
		return bean.getManagerId();
	}
	
	public void setManagerId(int mangerId) {
		bean.setManagerId(mangerId);
	}
	
	public Set<Integer> getStorageCenterIds() {
		return bean.getStorageCenters();
	}
	
	public Set<Integer> getUserIds() {
		return bean.getUsersIds();
	}
	
	public Set<Integer> getClassificationIds() {
		return bean.getClassificationIds();
	}
	
	public Set<DepartmentModel> getChildren() {
		return children;
	}
	
	public int getParentID() {
		return bean.getParentId();
	}

	public void setChildren(Set<DepartmentModel> children) {
		this.children = children;
	}
	
	public void save() throws DepartmentException {
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			if (bean.getId() == 0) {
				dao.addDepartment(bean);
			} else {
				dao.updateDepartment(bean);
			}
		} catch (DatabaseException e) {
			throw new DepartmentException("Error happend while trying to save department!", e);
		}
	}
	
	public JSONArray getAsJsonArray(String parentId, Locale loc) throws UserException {
	    try {
	        JSONArray arr = new JSONArray();
	        JSONObject obj = getAsJson();
	        obj.put("parent", parentId);
	        obj.put("name", getDisplayName(loc));
	        obj.put("isLastLevel", true); 
	        loadChildren();
			for (DepartmentModel child : getChildren()) {
				arr.addAll(child.getAsJsonArray(parentId,loc));
			}
	        arr.add(obj);
	        return arr;
	    } catch (Exception e) {
	        throw new UserException("Error getting department as JSON array", e);
	    }
	}
	
	private String getDisplayName(Locale loc) {
		return "(" + getDepartmentCode() + ") " + (loc.equals(Locale.ENGLISH) ? getDepartmentNameEn() : getDepartmentNameAr());
	}

	public JSONObject getAsJson() throws DepartmentException {
		JSONObject obj = new JSONObject();
		obj.put("id", bean.getId());
		obj.put("nameAr", bean.getNameAr());
		obj.put("nameEn", bean.getNameEn());
		obj.put("parentId", bean.getParentId());
		obj.put("isEnabled", bean.isEnabled());
		obj.put("code", bean.getCode());
		obj.put("email", bean.getDeptEmail());
		obj.put("managerId", bean.getManagerId());
		obj.put("isArchiveCenter",bean.getIsArchiveCenter());
		obj.put("savePeriod", "");
		try {
			JSONArray users = JSONArray.parse(bean.getUsersIds().toString());
			obj.put("users", users);
			
			JSONArray storageCenters = JSONArray.parse(bean.getStorageCenters().toString());
			obj.put("centers", storageCenters);
			
			JSONArray classifications = JSONArray.parse(bean.getClassificationIds().toString());
			obj.put("classifications", classifications);
	        JSONArray childrenArray = new JSONArray();
	        if (this.children != null) {
	            for (DepartmentModel child : this.children) {
	                childrenArray.add(child.getAsJson());
	            }
	        }
	      
	        obj.put("children", childrenArray);

		} catch (IOException e) {
			throw new DepartmentException("Error generating JSON for department with id '" + bean.getId() + "'", e);
		}
		return obj;
	}
	
	public static DepartmentModel getDepartmentById(Integer id) throws DepartmentException {
		Set<Integer> deptIds = new HashSet<Integer>();
		deptIds.add(id);
		DepartmentModel dm = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> beans = dao.fetchDepartments(deptIds);
			for (Iterator<DepartmentBean> b = beans.iterator(); b.hasNext();) {
				dm = new DepartmentModel(b.next());
			}
		} catch (DatabaseException e) {
			throw new DepartmentException("Error getting department by id!", e);
		}
		return dm;
	}
	
	public static Set<DepartmentModel> getDepartmentStructureById(Integer departmentId) throws DepartmentException {
		Set<Integer> deptIds = new HashSet<Integer>();
		deptIds.add(departmentId);
		List<DepartmentModel> dmList = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> deptBeans = dao.fetchDepartments(deptIds);
//			dmList = new ArrayList<>();
//
//		    Map<Integer, DepartmentModel> departmentMap = new HashMap<>();
			Set<DepartmentModel> models = new LinkedHashSet<DepartmentModel>();
			
			for (DepartmentBean b : deptBeans) {
				DepartmentModel depModel = new DepartmentModel(b);
				models.add(	depModel);
			}

//		    for (DepartmentBean bean : deptBeans) {
//		         DepartmentModel dm = new DepartmentModel(bean);
//		         departmentMap.put(bean.getId(), dm);
////		    }
//		        // tree Node
//		    for (DepartmentBean bean : deptBeans) {
//		        DepartmentModel dm = departmentMap.get(bean.getId());
//		        if (bean.getParentId() != 0) {
//		                // If there is a parent
//		        	DepartmentModel parent = departmentMap.get(bean.getParentId());
//	                if (parent != null) {
//	                    parent.addChild(dm);
//	                }
//		         } else {
//		                // If no parent
//		                dmList.add(dm);
//		         }
//		    }
		 return models;

	    } catch (DatabaseException e) {
	        throw new DepartmentException("Error getting user departments", e);
	    }
//	    return dmList;
	}

	public static List<DepartmentModel> getDepartmentsByIds(Set<Integer> deptIds) throws DepartmentException {
		List<DepartmentModel> dmList = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> deptBeans = dao.fetchDepartments(deptIds);
			dmList = new ArrayList<DepartmentModel>();
			for (DepartmentBean bean : deptBeans) {
				DepartmentModel dm = new DepartmentModel(bean);
				dmList.add(dm);
			}
		} catch (DatabaseException e) {
			throw new DepartmentException("Error getting user departments", e);
		}
		return dmList;
	}

	public JSONObject getFullStructure() throws DepartmentException {
	    try {
	        JSONObject obj = getAsJson();
	        JSONArray jsonChildren = new JSONArray();
	        loadChildren(); // Load children before processing
	        for (DepartmentModel child : getChildren()) {
	            jsonChildren.add(child.getFullStructure());
	        }
	        obj.put("children", jsonChildren);
	        return obj;
	    } catch (Exception e) {
	        throw new DepartmentException("Error getting Departments full structure for Department with id '" + bean.getId() + "'", e);
	    }
	}
	
	private void loadChildren() throws DepartmentException {
		try {
			if (bean.getChildrenIds().size() > 0) {
				DepartmentsDAO dao = new DepartmentsDAO();
				Set<DepartmentBean> beans = dao.fetchDepartments(bean.getChildrenIds());
				for (DepartmentBean b : beans) {
					children.add(new DepartmentModel(b));
				}
			} else {
				children = new LinkedHashSet<DepartmentModel>();
			}
		} catch (Exception e) {
			throw new DepartmentException("Error loading Departments children for department with id '" + bean.getId() + "'",e);
		}
	}
	
	public static Set<DepartmentModel> getAllDepartmentsAsTree() throws DepartmentException {
	    List<DepartmentModel> dmList = null;
	    try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> deptBeans = dao.fetchDepartments();

			Set<DepartmentModel> models = new LinkedHashSet<DepartmentModel>();
			
			for (DepartmentBean b : deptBeans) {
				DepartmentModel depModel = new DepartmentModel(b);
				models.add(	depModel);
			}

//	        dmList = new ArrayList<>();
//
//	        Map<Integer, DepartmentModel> departmentMap = new HashMap<>();
//
//	        for (DepartmentBean bean : deptBeans) {
//	            DepartmentModel dm = new DepartmentModel(bean);
//	            departmentMap.put(bean.getId(), dm);
//	        }
//
//	        // tree Node
//	        for (DepartmentBean bean : deptBeans) {
//	            DepartmentModel dm = departmentMap.get(bean.getId());
//	            if (bean.getParentId() != 0) {
//	                // If there is a parent
//	                DepartmentModel parent = departmentMap.get(bean.getParentId());
//	                if (parent != null) {
//	                    parent.addChild(dm);
//	                }
//	            } else {
//	                // If no parent
//	                dmList.add(dm);
//	            }
//	        }
		return models;
	    } catch (DatabaseException e) {
	        throw new DepartmentException("Error getting user departments", e);
	    }
	}
   
	public void addChild(DepartmentModel child) {
        if (children == null) {
            children = new LinkedHashSet<>();
        }
        children.add(child);
    }
	 
	public static List<DepartmentModel> getAllDepartments() throws DepartmentException {
		List<DepartmentModel> dmList = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> deptBeans = dao.fetchDepartments();
			dmList = new ArrayList<DepartmentModel>();
			for (DepartmentBean bean : deptBeans) {
				DepartmentModel dm = new DepartmentModel(bean);
				dmList.add(dm);
			}
		} catch (DatabaseException e) {
			throw new DepartmentException("Error getting user departments", e);
		}
		return dmList;
	}
	
	public static DepartmentModel getStorageCenterDepartment() throws DepartmentException {
		DepartmentModel dm = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			DepartmentBean deptBeans = dao.fetchArchivedDepartment();
			dm = new DepartmentModel(deptBeans);
		} catch (DatabaseException e) {
			throw new DepartmentException("Error getting archived department", e);
		}
		return dm;
	}
	
	public static DepartmentModel DepartmentById(Integer id) throws DepartmentException {
		Set<Integer> deptIds = new HashSet<Integer>();
		deptIds.add(id);
		DepartmentModel dm = null;
		try {
			DepartmentsDAO dao = new DepartmentsDAO();
			Set<DepartmentBean> beans = dao.fetchDepartments(deptIds);
			for (Iterator<DepartmentBean> b = beans.iterator(); b.hasNext();) {
				dm = new DepartmentModel(b.next());
			}
		} catch (DatabaseException e) {
			throw new DepartmentException("Error getting department by id!", e);
		}
		return dm;
	}
}
