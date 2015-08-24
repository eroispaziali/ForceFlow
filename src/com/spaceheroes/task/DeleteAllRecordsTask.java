package com.spaceheroes.task;

import org.apache.tools.ant.BuildException;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

/**
 * A task that deletes all records from a Salesforce object
 * @author Lorenzo Frattini
 *
 */
public class DeleteAllRecordsTask extends SalesforceTask {
	
	private String objectApiName;
	private static String FORMAT_SOQL = "SELECT Id FROM %s";
	private static String FORMAT_DELETE_MSG = "Deleting %d record(s) from %s";

	public DeleteAllRecordsTask() {
	 super();
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			PartnerConnection pc = ConnectionFactory.getPartnerConnection(getConfig());
			String soql = String.format(FORMAT_SOQL, objectApiName);
			QueryResult qr = pc.query(soql);
			Boolean done = false;
			log(String.format(FORMAT_DELETE_MSG, qr.getSize(), objectApiName));
			if (qr.getSize() > 0) {
				while (!done) {
					com.sforce.soap.partner.sobject.SObject[] records = qr.getRecords();
					String[] ids = new String[records.length];
					for (int i = 0; i<records.length; ++i) {
						ids[i] = (String)records[i].getField("Id");
					}
					pc.delete(ids);
					if (qr.isDone()) {
						done = true;
					} else {
						qr = pc.queryMore(qr.getQueryLocator());
					}
				}
			}
		} catch (ConnectionException e) {
			handleException(e);
		}
	}

	public void setObject(String object) {
		this.objectApiName = object;
	}

}
