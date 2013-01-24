package com.srps.model;

import org.springframework.stereotype.Repository;

@Repository
public class FormElement {
	
	private String element; 
	private String type; 
	private String text; 
	private String other;
	
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	} 
}
