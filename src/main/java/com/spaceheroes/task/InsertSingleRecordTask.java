package com.spaceheroes.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.fault.InvalidSObjectFault;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

/**
 * Task to insert a single record in the Salesforce database
 * @author Lorenzo Frattini
 *
 */

public class InsertSingleRecordTask extends SalesforceTask {
	
	private static final String PROPERTY_DESCRIPTION_FORMAT = "%s='%s'";
	private static final String SUCCESS_LOG_FORMAT = "Inserted %s";
	
	private String objectApiName;
	private Vector<Property> properties = new Vector<Property>();
	
	private void processResult(SaveResult[] results) throws BuildException {
		if (results!=null) {
			for (SaveResult r : results) {
				if (!r.isSuccess()) {
					List<String> errorMessages = new ArrayList<String>();
					for (com.sforce.soap.partner.Error e : r.getErrors()) {
						errorMessages.add(e.getMessage());
					}
					throw new BuildException(StringUtils.join(errorMessages, ", "));
				}
			}
		}
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			PartnerConnection pc = ConnectionFactory.getPartnerConnection(getConfig());
			Set<String> propertyDescriptions = new HashSet<String>();
			SObject record = new SObject();
			record.setType(objectApiName);
			for (Property p : properties) {
				record.setField(p.getName(), p.getValue());
				String description = String.format(PROPERTY_DESCRIPTION_FORMAT, p.getName(), p.getValue());
				propertyDescriptions.add(description);
			}
			SaveResult[] results = null;
			try {
				results = pc.create(new SObject[] {record});
				processResult(results);
				log(String.format(SUCCESS_LOG_FORMAT, objectApiName, StringUtils.join(propertyDescriptions, ", ")));
			} catch (InvalidSObjectFault e) {
				handleException(e);
			} catch (Exception e) {
				handleException(e);
			}
		} catch (ConnectionException e) {
			handleException(e);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	public Property createProperty () {
		Property property = new Property();
		properties.add(property);
		return property;
	}

	public String getObject() {
		return objectApiName;
	}

	public void setObject(String object) {
		this.objectApiName = object;
	}

	public Vector<Property> getSet() {
		return properties;
	}
	
	public void setSet(Vector<Property> set) {
		this.properties = set;
	}

}
