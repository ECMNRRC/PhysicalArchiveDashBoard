package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class SyslInboxType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String nameAr;
	private String nameEn;


	public SyslInboxType() {
	}


	
	public SyslInboxType(int id) {
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


	public JSONObject getAsJson(Locale locale) {
		JSONObject object = new JSONObject();
		object.put("id", this.getId());
		object.put("nameAr", this.getNameAr());
		object.put("nameEn", this.getNameEn());

		return object;
	}

	

}