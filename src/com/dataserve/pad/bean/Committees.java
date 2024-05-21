package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;



public class Committees implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String nameAr;
	private String nameEn;
	private List<CommitteeUsers> committeeUsers;

	public Committees() {
	}


	
	public Committees(int id) {
		super();
		this.id = id;
	}



	public int getId() {
		return id;
	}



	public void setId(
			int id) {
		this.id = id;
	}



	public String getNameAr() {
		return nameAr;
	}



	public void setNameAr(
			String nameAr) {
		this.nameAr = nameAr;
	}



	public String getNameEn() {
		return nameEn;
	}



	public void setNameEn(
			String nameEn) {
		this.nameEn = nameEn;
	}



	public List<CommitteeUsers> getCommitteeUsers() {
		return committeeUsers;
	}



	public void setCommitteeUsers(
			List<CommitteeUsers> committeeUsers) {
		this.committeeUsers = committeeUsers;
	}

	public Object getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());
		JSONArray array = new JSONArray();
		if(this.getCommitteeUsers() !=null && this.getCommitteeUsers().size() > 0) {			
			for (CommitteeUsers committeeUsers : this.getCommitteeUsers()) {
				array.add(committeeUsers.getAsJson(locale));
			}			
		}
		obj.put("committeeUsers", array);
		return obj;
	}
}