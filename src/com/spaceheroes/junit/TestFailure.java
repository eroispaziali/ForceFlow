package com.spaceheroes.junit;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name="failure")
public class TestFailure {
	
	@Attribute
	private String type;
	
	@Attribute
	private String message;
	
	@Text
	private String value;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStackTrace() {
		return value;
	}
	public void setStackTrace(String body) {
		this.value = body;
	}
	
	

}
