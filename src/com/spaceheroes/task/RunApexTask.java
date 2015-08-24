package com.spaceheroes.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.tools.ant.BuildException;

import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

public class RunApexTask extends SalesforceTask {
	
	private String apexCode = "";

	public RunApexTask() {
		super();
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			SoapConnection tc = ConnectionFactory.getToolingConnection(getConfig());
			log("Running Apex...");
			ExecuteAnonymousResult result = tc.executeAnonymous(apexCode); 
			if (!result.isSuccess()) {
				String error = result.isCompiled() ? result.getExceptionMessage() : result.getCompileProblem();
				throw new BuildException(error);
			}
		} catch (ConnectionException e) {
			throw new BuildException("Unable to connect to Salesforce", e);
		}
	}
	
	public void addText(String apexCode) {
		this.apexCode = apexCode;
	}

	@SuppressWarnings("resource")
	public void setSourceFile(File sourceFile) throws FileNotFoundException {
		addText(new Scanner(sourceFile, "UTF-8" ).useDelimiter("\\A").next());
	}

}
