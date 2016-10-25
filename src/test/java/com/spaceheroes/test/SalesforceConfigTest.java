package com.spaceheroes.test;

import org.junit.Assert;
import org.junit.Test;

import com.spaceheroes.util.SalesforceConfig;

public class SalesforceConfigTest {
	
	@Test
	public void setServerUrlTestHappyPath() {
		SalesforceConfig config = new SalesforceConfig("username", "password", "login.salesforce.com");
		Assert.assertEquals("login.salesforce.com", config.getServerUrl());
	}
	
	@Test
	public void setServerUrlTestWhenUsingPrefix() {
		SalesforceConfig config = new SalesforceConfig("username", "password", "https://login.salesforce.com");
		Assert.assertEquals("login.salesforce.com", config.getServerUrl());
	}
	
}
