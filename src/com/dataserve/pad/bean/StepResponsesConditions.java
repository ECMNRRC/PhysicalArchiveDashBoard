package com.dataserve.pad.bean;

import java.util.Locale;

import com.ibm.json.java.JSONObject;

public class StepResponsesConditions {
	private int id;
	private StepRespons stepRespons;
	private SyslStepResponsesCondtionsTypes  syslStepResponsesCondtionsTypes;
	private int stepResponsId;
	private int responseConditionNumber;
	public int getId() {
		return id;
	}
	public void setId(
			int id) {
		this.id = id;
	}
	public StepRespons getStepRespons() {
		return stepRespons;
	}
	public void setStepRespons(
			StepRespons stepRespons) {
		this.stepRespons = stepRespons;
	}
	public SyslStepResponsesCondtionsTypes getSyslStepResponsesCondtionsTypes() {
		return syslStepResponsesCondtionsTypes;
	}
	public void setSyslStepResponsesCondtions(
			SyslStepResponsesCondtionsTypes syslStepResponsesCondtionsTypes) {
		this.syslStepResponsesCondtionsTypes = syslStepResponsesCondtionsTypes;
	}
	public int getStepResponsId() {
		return stepResponsId;
	}
	public void setStepResponsId(
			int stepResponsId) {
		this.stepResponsId = stepResponsId;
	}
	public int getResponseConditionNumber() {
		return responseConditionNumber;
	}
	public void setResponseConditionNumber(
			int responseConditionNumber) {
		this.responseConditionNumber = responseConditionNumber;
	}
	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("responseConditionNumber", this.getResponseConditionNumber());
		
		if(this.getSyslStepResponsesCondtionsTypes() != null ) {
			obj.put("syslStepResponsesCondtionsTypes", this.getSyslStepResponsesCondtionsTypes().getAsJson(locale));
			
		}
		
		return obj;
	}
	

	
}
