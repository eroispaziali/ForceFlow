package com.spaceheroes.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.sforce.soap.metadata.DeleteResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

public class DeleteMetadataTask extends SalesforceTask {
	
	public String type;
	public String names;
	
	@Override
	public void execute() throws BuildException {
		try {
			MetadataConnection mc = ConnectionFactory.getMetadataConnection(getConfig());
			DeleteResult[] results = mc.deleteMetadata(type, StringUtils.split(names, ",") );
			for (DeleteResult r : results) {
				log("Name: " + r.getFullName() +  " Success: " + r.getSuccess());
				if (!r.getSuccess()) {
					for (com.sforce.soap.metadata.Error e : r.getErrors()) {
						log("Error: " + e.getMessage());
					}
					if (!getIgnoreErrors()) {
						throw new BuildException(r.getErrors().toString());
					}
				}
			}
		} catch (ConnectionException e) {
			handleException(e);
		}
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
