package com.dataserve.pad.business.classification;

import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.DeptClassBean;
import com.dataserve.pad.db.DatabaseException;
import com.dataserve.pad.db.DeptClassDAO;
import com.ibm.json.java.JSONObject;

public class DeptClassModel {
	private DeptClassBean bean;
	 
	
	public int getCLASSIFICATION_ID() {
		return bean.getCLASSIFICATION_ID();
	}
	public int getDEPT_ID() {
		return bean.getDEPT_ID();
	}
	public int getSAVE_PERIOD() {
		return bean.getSAVE_PERIOD();
	}
	public int getCLASS_SAVE_TYPE() {
		return bean.getSaveType();
	}

	 
	public DeptClassModel() {
	}
	public DeptClassModel(DeptClassBean bean) {
		this.bean = bean;
	}
	public JSONObject getAsJson() {
		JSONObject obj = new JSONObject();
		obj.put("CLASSIFICATION_ID",""+ bean.getCLASSIFICATION_ID()+"");
		obj.put("DEPT_ID",""+ bean.getDEPT_ID()+"");
		obj.put("SAVE_PERIOD",""+ bean.getSAVE_PERIOD()+"");
		obj.put("SaveType",""+bean.getSaveType()+"");
		 return obj;
	}
	
	public static DeptClassModel createNewDeptClass(int CLASSIFICATION_ID, int Dept_Id,int sAVE_PERIOD,int saveType) {
		DeptClassModel dm = null;
		try {
			DeptClassBean bean = new DeptClassBean();
			bean.setCLASSIFICATION_ID(CLASSIFICATION_ID);; 
			bean.setDEPT_ID(Dept_Id);
			bean.setSAVE_PERIOD(sAVE_PERIOD);
			bean.setSaveType(saveType);
			 
			dm = new DeptClassModel(bean);
			dm.save();
		} catch (ClassificationException e) {
			e.printStackTrace();
		}
		return dm;
	}
	public static Set<DeptClassModel> fetchDeptClass(int CLASSIFICATION_ID) throws ClassificationException {
		
		
		
		try {
			DeptClassDAO dao = new DeptClassDAO();
//			DeptClassBean  deptCalssBean=new DeptClassBean();
			Set<DeptClassBean> deptCalssBeans = dao.fetchCLASSDEPT(CLASSIFICATION_ID);
			
			Set<DeptClassModel> dcList = new LinkedHashSet<DeptClassModel>();
			for (DeptClassBean bean : deptCalssBeans) {
				DeptClassModel dc = new DeptClassModel(bean);
				dcList.add(dc);
			}
		
		return dcList;
	
	} catch (Exception e) {
		throw new ClassificationException("Error getting user departments", e);
	}
		 
	}
 
		public void save() throws ClassificationException {
			try {
				DeptClassDAO dao = new DeptClassDAO();
				if (bean.getCLASSIFICATION_ID() == 0) {
					dao.addClassDept(bean);
				} else {
					dao.updateClassDept(bean);
				}
			} catch (DatabaseException e) {
				throw new ClassificationException("Error happend while trying to save Classification!", e);
			}
			
		 
		
	}
}
