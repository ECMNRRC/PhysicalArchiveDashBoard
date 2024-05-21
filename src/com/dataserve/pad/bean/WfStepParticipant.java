package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class WfStepParticipant implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int corrCreator;
	private Inbox inbox;
	private WfStep wfStep;
	private int inboxId;
	private int wfStepId;
	private int wfStepParticipantTypeId;

	public WfStepParticipant() {
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getCorrCreator() {
		return this.corrCreator;
	}

	public void setCorrCreator(int corrCreator) {
		this.corrCreator = corrCreator;
	}


	public Inbox getInbox() {
		return this.inbox;
	}

	public void setInbox(Inbox inbox) {
		this.inbox = inbox;
	}


	public WfStep getWfStep() {
		return wfStep;
	}


	public void setWfStep(WfStep wfStep) {
		this.wfStep = wfStep;
	}


	public int getInboxId() {
		return inboxId;
	}


	public void setInboxId(int inboxId) {
		this.inboxId = inboxId;
	}


	public int getWfStepId() {
		return wfStepId;
	}


	public void setWfStepId(int wfStepId) {
		this.wfStepId = wfStepId;
	}


	public int getWfStepParticipantTypeId() {
		return wfStepParticipantTypeId;
	}


	public void setWfStepParticipantTypeId(int wfStepParticipantTypeId) {
		this.wfStepParticipantTypeId = wfStepParticipantTypeId;
	}


	public JSONObject getAsJson(Locale locale) {
		JSONObject object = new JSONObject();
		object.put("id", this.getId());
		object.put("wfStepParticipantTypeId", this.getWfStepParticipantTypeId());
		if(this.getInbox()!=null){
			object.put("inbox", this.getInbox().getAsJson(locale));
		}
		
		return object;
	}



}