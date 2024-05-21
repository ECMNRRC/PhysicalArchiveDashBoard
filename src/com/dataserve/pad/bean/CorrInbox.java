package com.dataserve.pad.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



public class CorrInbox implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private Timestamp readDate;
	private Timestamp receiveDate;
	private Inbox inbox;
	private SyslCorrInboxStatus syslCorrInboxStatus;
	private WfStep wfStep;
	public CorrInbox() {
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getReadDate() {
		return this.readDate;
	}

	public void setReadDate(Timestamp readDate) {
		this.readDate = readDate;
	}


	public Timestamp getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}


	public Inbox getInbox() {
		return this.inbox;
	}

	public void setInbox(Inbox inbox) {
		this.inbox = inbox;
	}

	public SyslCorrInboxStatus getSyslCorrInboxStatus() {
		return this.syslCorrInboxStatus;
	}

	public void setSyslCorrInboxStatus(SyslCorrInboxStatus syslCorrInboxStatus) {
		this.syslCorrInboxStatus = syslCorrInboxStatus;
	}


	public WfStep getWfStep() {
		return wfStep;
	}


	public void setWfStep(WfStep wfStep) {
		this.wfStep = wfStep;
	}


	

	

}