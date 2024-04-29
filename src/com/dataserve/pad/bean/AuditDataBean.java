package com.dataserve.pad.bean;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
public class AuditDataBean extends AbstractBean {
	
	private int AuditId ;
	private String UserId;
	private String OperationId ;
	private String DocumentId ;
	private Integer FileId;
	private String UserArName;
	private String UserEnName; 
	private String DepNameAr;
	private String DepNameEn;
	private String ClassNameAr; 
	private String ClassNameEn;
	private String OperationNameAr;
	private String OperationNameEn;
	private Integer FileCount;
	private Integer OperationCount;
	private Integer WeekNumber;
	private Integer Year;
	private	Integer ClassCount;
	private String	ClasDate;


public String getDocumentId() {
	return DocumentId;
}
public void setDocumentId(String documentId) {
	DocumentId = documentId;
}

public int getFileId() {
	return FileId;
}
public void setFileId(int fileId) {
	FileId = fileId;
}

public int getAuditId() {
	return AuditId;
}
public void setAuditId(int auditId) {
	AuditId = auditId;
}

public String getUserId() {
	return UserId;
}
public void setUserId(String userId) {
	UserId = userId;
}

public String getOperationId() {
	return OperationId;
}
public void setOperationId(String operationId) {
	OperationId = operationId;
}

public String getUserArName() {
	return UserArName;
}
public void setUserArName(String userArName) {
	UserArName = userArName;
}

public String getUserEnName() {
	return UserEnName;
}
public void setUserEnName(String userEnName) {
	UserEnName = userEnName;
}

public String getDepNameEn() {
	return DepNameEn;
}
public void setDepNameEn(String depNameEn) {
	DepNameEn = depNameEn;
}

public String getDepNameAr() {
	return DepNameAr;
}
public void setDepNameAr(String depNameAr) {
	DepNameAr = depNameAr;
}

public String getClassNameAr() {
	return ClassNameAr;
}
public void setClassNameAr(String classNameAr) {
	ClassNameAr = classNameAr;
}

public String getClassNameEn() {
	return ClassNameEn;
}
public void setClassNameEn(String clasNameEn) {
	ClassNameEn = clasNameEn;
}

public Integer getFileCount() {
	return FileCount;
}
public void setFileCount(Integer fileCount) {
	FileCount = fileCount;
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
public Integer getOperationCount() {
	return OperationCount;
}
public void setOperationCount(Integer operationCount) {
	OperationCount = operationCount;
}
public Integer getWeekNumber() {
	return WeekNumber;
}
public void setWeekNumber(Integer weekNumber) {
	WeekNumber = weekNumber;
}
public Integer getYear() {
	return Year;
}
public void setYear(Integer year) {
	Year = year;
}
public Integer getClassCount() {
	return ClassCount;
}
public void setClassCount(Integer classCount) {
	ClassCount = classCount;
}
public String getClasDate() {
	return ClasDate;
}
public void setClasDate(String clasDate) {
	ClasDate = clasDate;
}

}