package com.dataserve.pad.bean;

public class EmailTemplateBodyDataFields {
	private int corrId;
	private java.sql.Timestamp corrCreateData;
	private String corrStatus;
	private String stepResponseNam;
	private String fromParticipantName;
	private java.sql.Timestamp corrForwardDate;
	private String toParticipantName;
	public int getCorrId() {
		return corrId;
	}
	public void setCorrId(int corrId) {
		this.corrId = corrId;
	}
	public java.sql.Timestamp getCorrCreateData() {
		return corrCreateData;
	}
	public void setCorrCreateData(java.sql.Timestamp corrCreateData) {
		this.corrCreateData = corrCreateData;
	}
	public java.sql.Timestamp getCorrForwardDate() {
		return corrForwardDate;
	}
	public void setCorrForwardDate(java.sql.Timestamp corrForwardDate) {
		this.corrForwardDate = corrForwardDate;
	}
	public String getToParticipantName() {
		return toParticipantName;
	}
	public void setToParticipantName(String toParticipantName) {
		this.toParticipantName = toParticipantName;
	}
	public String getCorrStatus() {
		return corrStatus;
	}
	public void setCorrStatus(String corrStatus) {
		this.corrStatus = corrStatus;
	}
	public String getStepResponseNam() {
		return stepResponseNam;
	}
	public void setStepResponseNam(String stepResponseNam) {
		this.stepResponseNam = stepResponseNam;
	}
	public String getFromParticipantName() {
		return fromParticipantName;
	}
	public void setFromParticipantName(String fromParticipantName) {
		this.fromParticipantName = fromParticipantName;
	}
	

}
