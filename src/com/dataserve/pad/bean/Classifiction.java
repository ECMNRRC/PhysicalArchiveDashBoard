package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;



public class Classifiction implements Serializable {
	private static final long serialVersionUID = 1L;
	private int classificationId;
	private String classArName;
	private String classCode;
	private String classEnName;
	private boolean isFnAdded;
	private long parentId;
	private String sympolicName;


	public Classifiction() {
	}


	
	public Classifiction(String sympolicName) {
		super();
		this.sympolicName = sympolicName;
	}



	public int getClassificationId() {
		return this.classificationId;
	}

	public void setClassificationId(int classificationId) {
		this.classificationId = classificationId;
	}


	public String getClassArName() {
		return this.classArName;
	}

	public void setClassArName(String classArName) {
		this.classArName = classArName;
	}


	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}


	public String getClassEnName() {
		return this.classEnName;
	}

	public void setClassEnName(String classEnName) {
		this.classEnName = classEnName;
	}


	public boolean getIsFnAdded() {
		return this.isFnAdded;
	}

	public void setIsFnAdded(boolean isFnAdded) {
		this.isFnAdded = isFnAdded;
	}


	public long getParentId() {
		return this.parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}


	public String getSympolicName() {
		return this.sympolicName;
	}

	public void setSympolicName(String sympolicName) {
		this.sympolicName = sympolicName;
	}


	

}