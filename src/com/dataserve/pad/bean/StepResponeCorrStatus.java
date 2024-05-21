package com.dataserve.pad.bean;

public class StepResponeCorrStatus {
	
	private int id;
	private StepRespons stepRespons;
	private SyslCorrStatus syslCorrStatus;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public StepRespons getStepRespons() {
		return stepRespons;
	}
	public void setStepRespons(StepRespons stepRespons) {
		this.stepRespons = stepRespons;
	}
	public SyslCorrStatus getSyslCorrStatus() {
		return syslCorrStatus;
	}
	public void setSyslCorrStatus(SyslCorrStatus syslCorrStatus) {
		this.syslCorrStatus = syslCorrStatus;
	}

	
}
