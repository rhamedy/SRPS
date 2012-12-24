package com.srps.model;

public class Survey {

	private String formId; 
	private String formName; 
	private String formType; 
	private String hashToken;
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getHashToken() {
		return hashToken;
	}
	public void setHashToken(String hashToken) {
		this.hashToken = hashToken;
	} 
	
}
