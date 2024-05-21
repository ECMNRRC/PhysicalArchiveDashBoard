package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;



public class SyslCorrInboxStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String nameAr;
	private String nameEn;
	private List corrInboxs;

	public SyslCorrInboxStatus() {
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


	
	public List getCorrInboxs() {
		return this.corrInboxs;
	}

	public void setCorrInboxs(List corrInboxs) {
		this.corrInboxs = corrInboxs;
	}

	

}