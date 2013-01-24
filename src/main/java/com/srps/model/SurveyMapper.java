package com.srps.model;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class SurveyMapper {
	
	private String formName; 
	private boolean visibility; 
	private String deviceId; 
	private String username; 
	
	private List<FormElement> elements;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<FormElement> getElements() {
		return elements;
	}

	public void setElements(List<FormElement> elements) {
		this.elements = elements;
	} 
}
