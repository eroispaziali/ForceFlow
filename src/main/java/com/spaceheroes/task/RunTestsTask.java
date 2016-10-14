package com.spaceheroes.task;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.sforce.soap.apex.RunTestFailure;
import com.sforce.soap.apex.RunTestSuccess;
import com.sforce.soap.apex.RunTestsRequest;
import com.sforce.soap.apex.RunTestsResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;
import com.spaceheroes.xml.junit.TestCase;
import com.spaceheroes.xml.junit.TestFailure;
import com.spaceheroes.xml.junit.TestSuite;

public class RunTestsTask extends SalesforceTask {
	
	private static final Integer TIME_CONVERSION_FACTOR = 1000;
	private Map<String, TestSuite> testSuitesMap = new HashMap<String, TestSuite>();
	private String testReportsDir = "test-reports";
	

	public String getTestReportsDir() {
		return testReportsDir;
	}

	public void setTestReportsDir(String testReportsDir) {
		this.testReportsDir = testReportsDir;
	}

	private TestSuite getOrCreate(String classname) {
		TestSuite testSuite = testSuitesMap.get(classname);
		if (testSuite==null) {
			testSuite = new TestSuite();
			testSuite.setName(classname);
			testSuitesMap.put(classname, testSuite);
		}
		return testSuite;
	}
	
	@Override
	public void execute() throws BuildException { 
		try {
			SoapConnection tc = ConnectionFactory.getToolingConnection(getConfig());
			RunTestsRequest request = new RunTestsRequest();
			
			log("Running all tests...");
			request.setAllTests(true);
			RunTestsResult result = tc.runTests(request);
			Collection<TestSuite> suites = buildTestReport(result);
			Integer i = 1;
			for (TestSuite testSuite : suites) {
				String filename = String.format("%s/test-report-%s.xml", testReportsDir, i++);
				try {
					Serializer serializer = new Persister();
					File source = new File(filename);
					FileUtils.forceMkdirParent(source);
					serializer.write(testSuite, source);
				} catch (Exception e) {
					log("Error: " + e);
					e.printStackTrace();
				}
			}
			
			
		} catch (ConnectionException e) {
			handleException(e);
		}
	}
	
	private Collection<TestSuite> buildTestReport(RunTestsResult result) {
		
		// Process all successes
		for (RunTestSuccess testResult : result.getSuccesses()) {
			TestSuite s = getOrCreate(testResult.getName());
			TestCase testCase = new TestCase();
			testCase.setName(testResult.getMethodName());
			testCase.setTime(testResult.getTime()/TIME_CONVERSION_FACTOR);
			s.addTestCase(testCase);
		}
		
		// Process all failures
		for (RunTestFailure testResult : result.getFailures()) {
			TestSuite s = getOrCreate(testResult.getName());
			TestCase testCase = new TestCase();
			testCase.setName(testResult.getMethodName());
			testCase.setTime(testResult.getTime()/TIME_CONVERSION_FACTOR);
			
			TestFailure testFailure = new TestFailure();
			testFailure.setType(testResult.getType());
			testFailure.setMessage(testResult.getMessage());
			testFailure.setStackTrace(testResult.getStackTrace());
			
			testCase.addTestFailure(testFailure);
			s.addTestCase(testCase);
		}
		
		return testSuitesMap.values();
	}

}
