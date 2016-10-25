package com.spaceheroes.test;


import org.junit.Assert;
import org.junit.Test;

import com.spaceheroes.task.RunApexTask;

public class RunApexTest {
	
	private static final String TEST_USERNAME = "dummy@salesforce.com";
	private static final String TEST_PASSWORD = "123456";
	private static final String TEST_APEX_CODE = "System.debug('hello');";
	private static final String DEFAULT_ENDPOINT = "login.salesforce.com";
	
	@Test
	public void test_runApexTest_happyPath() throws Exception {
		RunApexTask task = new RunApexTask();
		task.setUsername(TEST_USERNAME);
		task.setPassword(TEST_PASSWORD);
		task.addText(TEST_APEX_CODE);
		Assert.assertEquals(DEFAULT_ENDPOINT, task.getServerUrl());
		Assert.assertEquals(TEST_PASSWORD, task.getPassword());
		Assert.assertEquals(TEST_USERNAME, task.getUsername());
		Assert.assertEquals(TEST_APEX_CODE, task.getApexCode());
	}

}
