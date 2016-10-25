package com.spaceheroes.util;

public class SalesforceConfig {
	
	private String username;
	private String password;
	private String serverUrl;
	private String apiVersion = "37.0";

	public SalesforceConfig(String username, String password, String serverUrl) {
		this.username = username;
		this.password = password;
		this.serverUrl = serverUrl;
	}
	
	public SalesforceConfig(String username, String password) {
		this.username = username;
		this.password = password;
		this.serverUrl = "login.salesforce.com";
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

}
