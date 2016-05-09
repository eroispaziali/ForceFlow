package com.spaceheroes.task;

import org.apache.tools.ant.BuildException;

import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

public class ScheduleJobTask extends SalesforceTask {
	
	private String cron;
	private String className;
	private String scheduleName;
	
	private static String SCHEDULE_APEX = "%s myJob = new %s(); System.schedule('%s', '%s', myJob);";
	
	
	private void checkCronExpression() throws BuildException {
		if (cron==null) {
			throw new BuildException("Cron expression is not correct");
		}
		//cron = StringUtils.replace(cron, "'", "''");
	}
	
	private void checkScheduleName() throws BuildException {
		if (scheduleName=="" || scheduleName==null) {
			scheduleName = className;
		}
	}
	
	@Override
	public void execute() throws BuildException {
		
		checkCronExpression();
		checkScheduleName();
		
		try {
			SoapConnection tc = ConnectionFactory.getToolingConnection(getConfig());
			String apexCode = String.format(SCHEDULE_APEX, className, className, scheduleName, cron);
			executeApex(tc, apexCode);
			log(String.format("%s has been scheduled",className));
		} catch (ConnectionException e) {
			handleException(e);
		}
		
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	
}
