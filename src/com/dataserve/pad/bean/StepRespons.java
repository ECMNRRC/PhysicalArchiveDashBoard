package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;



public class StepRespons implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String code;
	private String description;
	private String nameAr;
	private String nameEn;
	private WfStep wfStep;
	private List<WfStepFlow> wfStepFlows;
	private List<EmailTemplate> emailTemplates;
	private StepResponsesConditions stepResponsesConditions;
	private SyslCorrStatus syslCorrStatus;

	public StepRespons() {
	}

	
	
	public StepRespons(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	
	public WfStep getWfStep() {
		return this.wfStep;
	}

	public void setWfStep(WfStep wfStep) {
		this.wfStep = wfStep;
	}
	
	
	public List<EmailTemplate> getEmailTemplates() {
		return emailTemplates;
	}

	public void setEmailTemplates(List<EmailTemplate> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}


	public StepResponsesConditions getStepResponsesConditions() {
		return stepResponsesConditions;
	}


	public void setStepResponsesConditions(
			StepResponsesConditions stepResponsesConditions) {
		this.stepResponsesConditions = stepResponsesConditions;
	}
	
	public SyslCorrStatus getSyslCorrStatus() {
		return syslCorrStatus;
	}

	public void setSyslCorrStatus(SyslCorrStatus syslCorrStatus) {
		this.syslCorrStatus = syslCorrStatus;
	}



	public List<WfStepFlow> getWfStepFlows() {
		return wfStepFlows;
	}



	public void setWfStepFlows(List<WfStepFlow> wfStepFlows) {
		this.wfStepFlows = wfStepFlows;
	}



	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("code", this.getCode());
		obj.put("description", this.getDescription());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());
		
		JSONArray array = new JSONArray();
		if(this.getWfStepFlows() != null && this.getWfStepFlows().size() >0 ){
			
			for (WfStepFlow stepFlow : this.getWfStepFlows()) {
				array.add(stepFlow.getAsJson(locale));
			}		
		}
		obj.put("wfStepFlows", array);
		
		array = new JSONArray();
		if(this.getEmailTemplates() !=null && this.getEmailTemplates().size() > 0){
			
			for (EmailTemplate emailTemplate : this.getEmailTemplates()) {
				array.add(emailTemplate.getAsJson(locale));
			}
		}
		obj.put("emailTemplates", array);
		if(this.getSyslCorrStatus() !=null){
			obj.put("syslCorrStatus", this.getSyslCorrStatus().getAsJson(locale));
		}
			
		if(this.getStepResponsesConditions() !=null){
			obj.put("stepResponsesConditions", this.getStepResponsesConditions().getAsJson(locale));
		}
		
		
		
		return obj;
	}


	public JSONObject getAsJsonChoiceList(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		if(Locale.ENGLISH.equals(locale)){
			obj.put("name", this.getNameEn());
		}else
			obj.put("name", this.getNameAr());
		return obj;
	}
}