package com.spaceheroes.task;

import org.apache.tools.ant.BuildException;

import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

/**
 * A task that deletes all the scheduled jobs in a Salesforce org.
 * @author Lorenzo Frattini
 *
 */
public class ClearJobScheduleTask extends SalesforceTask {

	private static final String SOQL_JOBS = "SELECT Id FROM CronTrigger";
	private static final String APEX_ABORT = "System.abortJob('%s');";
	
	// TODO: private static final String SOQL_JOB_DETAIL = "SELECT Id,JobType,Name FROM CronJobDetail";

	@Override
	public void execute() throws BuildException {
		try {
			PartnerConnection pc = ConnectionFactory.getPartnerConnection(getConfig());
			QueryResult result = pc.query(SOQL_JOBS);
			if (result!=null && result.getSize() > 0) {
				log(String.format("Clearing %d scheduled jobs...", result.getSize()));
				SoapConnection tc = ConnectionFactory.getToolingConnection(pc, getConfig());
				for (SObject o : result.getRecords()) {
					String jobId = (String) o.getId();
					log("Aborting Job: " + jobId);
					executeApex(tc, String.format(APEX_ABORT, jobId));
				}
			} else {
				log("No scheduled job found");
			}
			
		} catch (ConnectionException e) {
			handleException(e);
		
		} catch (Exception e) {
			log(e.getMessage());
		}
	}
}
