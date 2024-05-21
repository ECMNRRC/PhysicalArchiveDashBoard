package com.dataserve.pad.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.ibm.json.java.JSONObject;



public class WfStep implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String code;
	private String description;
	private String nameAr;
	private String nameEn;
	private List<StepRespons> stepResponses;
	private WorkflowType workflowType;
	private SyslStepType syslStepType;
	private WfStepParticipant wfStepParticipant;

	public WfStep() {
	}


	
	public WfStep(int id) {
		super();
		this.id = id;
	}



	public WfStep(int id, String code, String description,
			String nameAr, String nameEn) {
		super();
		this.id = id;
		this.code = code;
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



	

	public List<StepRespons> getStepResponses() {
		return this.stepResponses;
	}

	public void setStepResponses(List<StepRespons> stepResponses) {
		this.stepResponses = stepResponses;
	}

	

	
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}


	
	public SyslStepType getSyslStepType() {
		return this.syslStepType;
	}

	public void setSyslStepType(SyslStepType syslStepType) {
		this.syslStepType = syslStepType;
	}


	



	public WfStepParticipant getWfStepParticipant() {
		return wfStepParticipant;
	}



	public void setWfStepParticipant(WfStepParticipant wfStepParticipant) {
		this.wfStepParticipant = wfStepParticipant;
	}


	public JSONObject getAsJson(Locale locale) {

		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("nameAr", this.getNameAr());
		obj.put("nameEn", this.getNameEn());

		if(this.getSyslStepType() != null){
			obj.put("syslStepType", this.getSyslStepType().getAsJson(locale));
		}
		
		if(this.getWfStepParticipant() !=null){
			obj.put("wfStepParticipant", this.getWfStepParticipant().getAsJson(locale));
		}
		
		return obj;
	
	}
	



	
}