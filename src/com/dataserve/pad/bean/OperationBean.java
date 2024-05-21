package com.dataserve.pad.bean;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
public class OperationBean extends AbstractBean {
	

	private String OperationId ;
	private String OperationNameAr;
	private String OperationNameEn;


	
	
	




public String getOperationId() {
	return OperationId;
}
public void setOperationId(String operationId) {
	OperationId = operationId;
}


public String getOperationNameAr() {
	return OperationNameAr;
}
public void setOperationNameAr(String operationNameAr) {
	OperationNameAr = operationNameAr;
}

public String getOperationNameEn() {
	return OperationNameEn;
}
public void setOperationNameEn(String operationNameEn) {
	OperationNameEn = operationNameEn;
}

}