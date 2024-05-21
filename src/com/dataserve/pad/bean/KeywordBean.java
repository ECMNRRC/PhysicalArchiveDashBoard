package com.dataserve.pad.bean;

public class KeywordBean {
	private String classSymbolicName;
	private String propertySymbolicName;
	private String listDisplayName;
	private String lang;
	private String displayName;
	private String value;
	private String depOn;
	private String depValue;
	private Integer order;
	private boolean isValueHasDepOnValues;
	private boolean isNew;
	private String depDisplayName;
	
	public KeywordBean() {}

	public KeywordBean(String classSymbolicName, String propertySymbolicName, String listDisplayName, String lang,
			String displayName, String value, String depOn, String depValue, Integer order) {
		super();
		this.classSymbolicName = classSymbolicName;
		this.propertySymbolicName = propertySymbolicName;
		this.listDisplayName = listDisplayName;
		this.lang = lang;
		this.displayName = displayName;
		this.value = value;
		this.depOn = depOn;
		this.depValue = depValue;
		this.order = order;
	}

	public String getClassSymbolicName() {
		return classSymbolicName;
	}

	public void setClassSymbolicName(String classSymbolicName) {
		this.classSymbolicName = classSymbolicName;
	}
	
	public String getPropertySymbolicName() {
		return propertySymbolicName;
	}

	public void setPropertySymbolicName(String propertySymbolicName) {
		this.propertySymbolicName = propertySymbolicName;
	}

	public String getListDisplayName() {
		return listDisplayName;
	}

	public void setListDisplayName(String listDisplayName) {
		this.listDisplayName = listDisplayName;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDepOn() {
		return depOn;
	}

	public void setDepOn(String depOn) {
		this.depOn = depOn;
	}

	public String getDepValue() {
		return depValue;
	}

	public void setDepValue(String depValue) {
		this.depValue = depValue;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean isValueHasDepOnValues() {
		return isValueHasDepOnValues;
	}

	public void setValueHasDepOnValues(boolean isValueHasDepOnValues) {
		this.isValueHasDepOnValues = isValueHasDepOnValues;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public String getDepDisplayName() {
		return depDisplayName;
	}

	public void setDepDisplayName(String depDisplayName) {
		this.depDisplayName = depDisplayName;
	}

	public void print() {
//		System.out.println("Property Definition " + "\t" + "classSymbolicName : " + this.getClassSymbolicName()
//				+ "\t" + "propertySymbolicName : " + this.getPropertySymbolicName() + "\t" + "listDisplayName : "
//				+ this.getListDisplayName() + "\t" + "lang : " + this.getLang() + "\t" + "displayName : "
//				+ this.getDisplayName() + "\t" + "value : " + this.getValue() + "\t" + "depOn : "
//				+ this.getDepOn() + "\t" + "depValue : " + this.getDepValue() + "\t" + "order : "
//				+ this.getOrder()+ "\t" + "depDisplayName : " + this.getDepDisplayName()
//
//		);
	}
	
}
