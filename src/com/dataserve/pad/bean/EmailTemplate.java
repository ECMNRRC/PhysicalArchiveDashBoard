package com.dataserve.pad.bean;

import java.util.Locale;

import com.ibm.json.java.JSONObject;

public class EmailTemplate {
	private int id;
	private String emailName;
	private String emailSubject;
	private String mailBody;
	private boolean sendToRequester;
	
	
	public EmailTemplate() {
	}
	
	public EmailTemplate(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmailName() {
		return emailName;
	}
	public void setEmailName(String emailName) {
		this.emailName = emailName;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getMailBody() {
		return mailBody;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	public boolean isSendToRequester() {
		return sendToRequester;
	}
	public void setSendToRequester(boolean sendToRequester) {
		this.sendToRequester = sendToRequester;
	}

	public JSONObject getAsJson(Locale locale) {
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("emailName", this.getEmailName());
		obj.put("emailSubject", this.getEmailSubject());
		obj.put("mailBody", this.getMailBody());
		obj.put("sendToRequester", this.isSendToRequester());

		return obj;
	}
}
