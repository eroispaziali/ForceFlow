package com.spaceheroes.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;


/*
 * <sh:runtests output="junit.xml" />
 * should produce output in this format http://help.catchsoftware.com/display/ET/JUnit+Format
 */

public class RenameObject extends SalesforceTask {
	
	private String oldname;
	private String newname;

	public RenameObject() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			MetadataConnection mc = ConnectionFactory.getMetadataConnection(getConfig());
			SaveResult result = mc.renameMetadata("CustomObject", oldname, newname);
			if (!result.isSuccess()) {
				String error = StringUtils.join(result.getErrors(),"\n");
				throw new BuildException(error);
			}
		} catch (ConnectionException e) {
			throw new BuildException("Unable to connect to Salesforce", e);
		}
		
	}

}
