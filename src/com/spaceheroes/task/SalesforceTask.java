package com.spaceheroes.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.partner.fault.InvalidSObjectFault;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.SalesforceConfig;

public abstract class SalesforceTask extends Task {
	
	private String username;
	private String password;
	private String serverUrl = "login.salesforce.com";
	private Boolean ignoreErrors = false;
	
	protected Boolean error = false;

	public SalesforceTask() {
		super();
	}
	
	protected void handleException(Exception e) throws BuildException {
		String message = "";
		if (e instanceof InvalidSObjectFault) {
			message = e.getLocalizedMessage();
		} else if (e instanceof ConnectionException) {
			message = "Unable to connect to Salesforce";
		} else {
			message = e.getLocalizedMessage();
		}
		if (!getIgnoreErrors()) {
			throw new BuildException(message, e);
		} else {
			error = true;
			log("Ignored error: " + message);
		}
	}
	
	protected void executeApex(SoapConnection tc, String apexCode) throws BuildException {
		try {
			ExecuteAnonymousResult result = tc.executeAnonymous(apexCode); 
			if (!result.isSuccess()) {
				String error = result.isCompiled() ? result.getExceptionMessage() : result.getCompileProblem();
				throw new BuildException(error);
			}
		} catch (ConnectionException e) {
			handleException(e);
		}
	}
	
	protected SalesforceConfig getConfig() {
		return new SalesforceConfig(username, password, serverUrl);
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
	
	protected String escapeSingleQuotes(String st) {
		//return StringUtils.replace(st, "'", "''");
		return st;
	}

	public Boolean getIgnoreErrors() {
		return ignoreErrors;
	}

	public void setIgnoreErrors(Boolean ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}

}
