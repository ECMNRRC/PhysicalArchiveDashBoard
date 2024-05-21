package com.dataserve.pad.bean;

public class CommitteesStepResponsesCount {
	private int id;
	private StepRespons stepRespons;
	private int responseCount;
	public int getId() {
		return id;
	}
	public void setId( int id) {
		this.id = id;
	}
	
	public StepRespons getStepRespons() {
		return stepRespons;
	}
	public void setStepRespons(
			StepRespons stepRespons) {
		this.stepRespons = stepRespons;
	}
	public int getResponseCount() {
		return responseCount;
	}
	public void setResponseCount(
			int responseCount) {
		this.responseCount = responseCount;
	}
	
	
}
