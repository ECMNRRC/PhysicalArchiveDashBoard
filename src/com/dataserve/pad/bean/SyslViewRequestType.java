package com.dataserve.pad.bean;

import java.io.Serializable;



public class SyslViewRequestType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String nameAr;
	private String nameEn;

	public SyslViewRequestType() {
	}


	public SyslViewRequestType(int id) {
		super();
		this.id = id;
	}

	public SyslViewRequestType(int id, String description, String nameAr,
			String nameEn) {
		super();
		this.id = id;
		this.description = description;
		this.nameAr = nameAr;
		this.nameEn = nameEn;
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

}