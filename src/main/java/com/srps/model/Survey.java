package com.srps.model;

import java.util.Date;

import org.springframework.stereotype.Repository;

@Repository
public class Survey {
	
	private String submissionId;
	private String formName; 
	private String username; 
	private boolean image; 
	private boolean audio; 
	private boolean geopoint; 
	private boolean visibility;
	private Date submissionDate; 
	private String deviceId; 
	
	public String getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	public boolean isImage() {
		return image;
	}
	public void setImage(boolean image) {
		this.image = image;
	}
	public boolean isAudio() {
		return audio;
	}
	public void setAudio(boolean audio) {
		this.audio = audio;
	}
	public boolean isGeopoint() {
		return geopoint;
	}
	public void setGeopoint(boolean geopoint) {
		this.geopoint = geopoint;
	}
	public boolean isVisibility() {
		return visibility;
	}
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	public Date getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
