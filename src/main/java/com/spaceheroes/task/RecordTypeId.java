package com.spaceheroes.task;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;
import com.spaceheroes.util.SalesforceConfig;

public class RecordTypeId extends SalesforceTask {
	
	private String developerName = "";
	private String object = "";
	private String propertyRef;
	
	private void checkAttributes() throws BuildException {
		
		if (propertyRef == null) {
			throw new BuildException("Property not set");
		}
		
		if (developerName == null) {
			throw new BuildException("Developer name required");
		}
		
	}
	
	private String getCompiledQuery() {
		
		// Compile WHERE clause
		Set<String> whereClauses = new HashSet<String>();
		if (developerName != null) { whereClauses.add(String.format("DeveloperName='%s'", escapeSingleQuotes(developerName))); }
		if (object != null) { whereClauses.add(String.format("sObjectType='%s'", escapeSingleQuotes(object))); }
		String compiledWhereClause = StringUtils.join(whereClauses, " AND ");
		
		// Compile SOQL and return
		return String.format("SELECT Id FROM RecordType WHERE (%s)", compiledWhereClause);
	}
	
	@Override
	public void execute() throws BuildException {
		
		checkAttributes();

		// prepare where clause
		
		try {
			SalesforceConfig conf = getConfig();
			PartnerConnection connection = ConnectionFactory.getPartnerConnection(conf);
			log(String.format("Getting RecordTypeId: %s.%s", object, developerName));
			QueryResult result = connection.query(getCompiledQuery());
			if (result.getSize() == 1) {
				String value = (String) result.getRecords()[0].getField("Id");
				getProject().setNewProperty(propertyRef, value);
			} else {
				throw new BuildException("Unable to find a unique result");	
			}
		} catch (ConnectionException e) {
			throw new BuildException(e.getLocalizedMessage());
		}
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public void setPropertyRef(String propertyRef) {
		this.propertyRef = propertyRef;
	}

}
