package com.spaceheroes.task;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.sforce.soap.apex.RunTestFailure;
import com.sforce.soap.apex.RunTestSuccess;
import com.sforce.soap.apex.RunTestsRequest;
import com.sforce.soap.apex.RunTestsResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.junit.TestCase;
import com.spaceheroes.junit.TestFailure;
import com.spaceheroes.junit.TestSuite;
import com.spaceheroes.util.ConnectionFactory;

public class RunTestsTask extends SalesforceTask {
	
	private static final String TEST_SUITE_NAME = "Salesforce Apex Test Execution";
	private static final Integer TIME_CONVERSION_FACTOR = 1000;
	
	@Override
	public void execute() throws BuildException { 
		try {
			SoapConnection tc = ConnectionFactory.getToolingConnection(getConfig());
			RunTestsRequest request = new RunTestsRequest();
			
			log("Running all tests...");
			request.setAllTests(true);
			RunTestsResult result = tc.runTests(request);
			TestSuite testSuite = buildTestReport(result);
			
			try {
				Serializer serializer = new Persister();
				File source = new File("test-report.xml");
				serializer.write(testSuite, source);
			} catch (Exception e) {
				log("Error: " + e);
				e.printStackTrace();
			}
		} catch (ConnectionException e) {
			handleException(e);
		}
	}
	
	private TestSuite buildTestReport(RunTestsResult result) {
		TestSuite testSuite = new TestSuite();
		
		testSuite.setName(TEST_SUITE_NAME);
		testSuite.setFailures(result.getNumFailures());
		testSuite.setErrors(0);
		testSuite.setTests(result.getNumTestsRun());
		testSuite.setTime(result.getTotalTime()/TIME_CONVERSION_FACTOR);
		
		// Process all successes
		for (RunTestSuccess testResult : result.getSuccesses()) {
			TestCase testCase = new TestCase();
			testCase.setName(testResult.getMethodName());
			testCase.setTime(testResult.getTime()/TIME_CONVERSION_FACTOR);
			testSuite.addTestCase(testCase);
		}
		
		// Process all failures
		for (RunTestFailure testResult : result.getFailures()) {
			TestCase testCase = new TestCase();
			testCase.setName(testResult.getMethodName());
			testCase.setTime(testResult.getTime()/TIME_CONVERSION_FACTOR);
			
			TestFailure testFailure = new TestFailure();
			testFailure.setType(testResult.getType());
			testFailure.setMessage(testResult.getMessage());
			testFailure.setStackTrace(testResult.getStackTrace());
			
			testCase.addTestFailure(testFailure);
			testSuite.addTestCase(testCase);
		}
		
		return testSuite;
	}

}
