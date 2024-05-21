package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class SyslStepResponsesCondtionsTypes implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String condition;
	private String nameAr;
	private String nameEn;

	public SyslStepResponsesCondtionsTypes() {
	}


	
	public SyslStepResponsesCondtionsTypes(int id) {
		super();
		this.id = id;
	}



	public SyslStepResponsesCondtionsTypes(int id, String condition, String nameAr, String nameEn) {
		super();
		this.id = id;
		this.condition = condition;
		this.nameAr = nameAr;
		this.nameEn = nameEn;
	}



	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());
		obj.put("condition", this.getCondition());
		return obj;
	}
}