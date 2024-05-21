package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;



public class WorkflowType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String code;
	private String descriptionAr;
	private String descriptionEn;
	private boolean fActive;
	private String nameAr;
	private String nameEn;
	private List<WfStep> wfSteps;
	private List<ClassDeptWorkflow> classDeptWorkflows;
	private SyslCorrType corrType;

	public WorkflowType() {
	}

	public WorkflowType(int id) {
		super();
		this.id = id;
	}
	
	public WorkflowType(int id, String code, String descriptionAr,
			String descriptionEn, boolean fActive, String nameAr, String nameEn) {
		super();
		this.id = id;
		this.code = code;
		this.descriptionAr = descriptionAr;
		this.descriptionEn = descriptionEn;
		this.fActive = fActive;
		this.nameAr = nameAr;
		this.nameEn = nameEn;
	}


	public WorkflowType(int id, String code, String descriptionAr,
			String descriptionEn, boolean fActive, String nameAr,
			String nameEn, List<WfStep> wfSteps,
			List<ClassDeptWorkflow> classDeptWorkflows) {
		super();
		this.id = id;
		this.code = code;
		this.descriptionAr = descriptionAr;
		this.descriptionEn = descriptionEn;
		this.fActive = fActive;
		this.nameAr = nameAr;
		this.nameEn = nameEn;
		this.wfSteps = wfSteps;
		this.classDeptWorkflows = classDeptWorkflows;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getDescriptionAr() {
		return this.descriptionAr;
	}

	public void setDescriptionAr(String descriptionAr) {
		this.descriptionAr = descriptionAr;
	}


	public String getDescriptionEn() {
		return this.descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}


	public boolean getFActive() {
		return this.fActive;
	}

	public void setFActive(boolean fActive) {
		this.fActive = fActive;
	}


	public String getNameAr() {
		return this.nameAr;
	}

	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}


	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}



	public List<WfStep> getWfSteps() {
		return this.wfSteps;
	}

	public void setWfSteps(List<WfStep> wfSteps) {
		this.wfSteps = wfSteps;
	}


	public List<ClassDeptWorkflow> getClassDeptWorkflows() {
		return classDeptWorkflows;
	}

	public void setClassDeptWorkflows(List<ClassDeptWorkflow> classDeptWorkflows) {
		this.classDeptWorkflows = classDeptWorkflows;
	}

	public SyslCorrType getCorrType() {
		return corrType;
	}

	public void setCorrType(SyslCorrType corrType) {
		this.corrType = corrType;
	}

	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("code", this.getCode());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());
		if(this.getCorrType() != null ) {
			obj.put("corrType", this.getCorrType().getAsJson(locale));
			obj.put("corrTypeId", this.getCorrType().getId());
			if(Locale.ENGLISH.equals(locale)){
				obj.put("corrTypeName", this.getCorrType().getNameEn());
			}
			else {
				obj.put("corrTypeName", this.getCorrType().getNameAr());
			}
		}
		obj.put("fActive", this.getFActive());
		
		JSONArray jsonArray = new JSONArray();
		if(this.getClassDeptWorkflows() !=null && this.getClassDeptWorkflows().size() > 0) {
			for (ClassDeptWorkflow classDeptWorkflow : this.getClassDeptWorkflows()) {
				jsonArray.add(classDeptWorkflow.getAsJson(locale));
			}		
		}
		obj.put("classDeptWorkflows", jsonArray);
		return obj;
	}


	
}