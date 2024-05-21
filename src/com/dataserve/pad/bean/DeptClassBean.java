package com.dataserve.pad.bean;


public class DeptClassBean  {
	public int getCLASSIFICATION_ID() {
		return CLASSIFICATION_ID;
	}
	public void setCLASSIFICATION_ID(int cLASSIFICATION_ID) {
		CLASSIFICATION_ID = cLASSIFICATION_ID;
	}
	public int getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(int dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public int getSAVE_PERIOD() {
		return SAVE_PERIOD;
	}
	public void setSAVE_PERIOD(int sAVE_PERIOD) {
		SAVE_PERIOD = sAVE_PERIOD;
	}
	public int getSaveType() {
		return SaveType;
	}
	public void setSaveType(int saveType) {
		SaveType = saveType;
	}
	private int CLASSIFICATION_ID;
	 private int DEPT_ID;

	 private int SAVE_PERIOD;
	 private int SaveType;
	
	
}