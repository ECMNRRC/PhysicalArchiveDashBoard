package com.dataserve.pad.bean;

import java.io.Serializable;
import java.sql.Timestamp;



public class CommittesRoutes implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String requiredComment;
	private Timestamp routeDate;
	private UserBean user;
	private CorrInbox fromCorrInbox;
	private String fromInboxNameAr;
	private String FromInboxNameEn;
	private String routeDateFormat;
	private StepRespons stepRespons;


	

	public CommittesRoutes() {
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getRequiredComment() {
		return this.requiredComment;
	}

	public void setRequiredComment(String requiredComment) {
		this.requiredComment = requiredComment;
	}


	public Timestamp getRouteDate() {
		return this.routeDate;
	}

	public void setRouteDate(Timestamp routeDate) {
		this.routeDate = routeDate;
	}


	
	public UserBean getUser() {
		return this.user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}



	public CorrInbox getFromCorrInbox() {
		return fromCorrInbox;
	}


	public void setFromCorrInbox(CorrInbox fromCorrInbox) {
		this.fromCorrInbox = fromCorrInbox;
	}

	
	public StepRespons getStepRespons() {
		return this.stepRespons;
	}

	public void setStepRespons(StepRespons stepRespons) {
		this.stepRespons = stepRespons;
	}


	public String getFromInboxNameAr() {
		return fromInboxNameAr;
	}


	public void setFromInboxNameAr(String fromInboxNameAr) {
		this.fromInboxNameAr = fromInboxNameAr;
	}


	public String getFromInboxNameEn() {
		return FromInboxNameEn;
	}


	public void setFromInboxNameEn(String fromInboxNameEn) {
		FromInboxNameEn = fromInboxNameEn;
	}


	public String getRouteDateFormat() {
		return routeDateFormat;
	}


	public void setRouteDateFormat(String routeDateFormat) {
		this.routeDateFormat = routeDateFormat;
	}



}