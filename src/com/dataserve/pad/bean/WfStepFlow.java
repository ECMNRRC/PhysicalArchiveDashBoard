package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class WfStepFlow implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int responseOrder;
	private WfStep wfStepFrom;
	private WfStep wfStepTo;
	private StepRespons stepRespons;
	

	public WfStepFlow() {
	}


	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getResponseOrder() {
		return this.responseOrder;
	}

	public void setResponseOrder(int responseOrder) {
		this.responseOrder = responseOrder;
	}

	public WfStep getWfStepFrom() {
		return wfStepFrom;
	}



	public void setWfStepFrom(WfStep wfStepFrom) {
		this.wfStepFrom = wfStepFrom;
	}



	public WfStep getWfStepTo() {
		return wfStepTo;
	}



	public void setWfStepTo(WfStep wfStepTo) {
		this.wfStepTo = wfStepTo;
	}



	public StepRespons getStepRespons() {
		return this.stepRespons;
	}

	public void setStepRespons(StepRespons stepRespons) {
		this.stepRespons = stepRespons;
	}

	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("responseOrder", this.getResponseOrder());
		
		if(this.getWfStepFrom() != null ) {
			obj.put("wfStepFrom", this.getWfStepFrom().getAsJson(locale));
			
		}
		if(this.getWfStepTo() != null ) {
			obj.put("wfStepTo", this.getWfStepTo().getAsJson(locale));
			
		}

		return obj;
	}
}