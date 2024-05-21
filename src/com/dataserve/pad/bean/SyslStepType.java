package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class SyslStepType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String nameAr;
	private String nameEn;
	private List wfSteps;

	public SyslStepType() {
	}


	public SyslStepType(int id) {
		super();
		this.id = id;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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


	public List getWfSteps() {
		return this.wfSteps;
	}

	public void setWfSteps(List wfSteps) {
		this.wfSteps = wfSteps;
	}


	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());
		return obj;
	}

	

}