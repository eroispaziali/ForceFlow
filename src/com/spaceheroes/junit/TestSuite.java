package com.spaceheroes.junit;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="testsuite")
public class TestSuite {
	
	@Attribute
	private Integer errors = 0;
	
	@Attribute
	private Integer tests = 0;
	
	@Attribute
	private Double time = 0.0;
	
	@Attribute
	private Integer failures = 0;
	
	@Attribute
	private String name;
	
	@ElementList(inline=true, name="testcase", required=false)
	private List<TestCase> testCases = new ArrayList<TestCase>();
	
	
	public Integer getErrors() {
		return errors;
	}
	public void setErrors(Integer errors) {
		this.errors = errors;
	}
	public Integer getTests() {
		return tests;
	}
	public void setTests(Integer tests) {
		this.tests = tests;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	public Integer getFailures() {
		return failures;
	}
	public void setFailures(Integer failures) {
		this.failures = failures;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TestCase> getTestCases() {
		return testCases;
	}
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}
	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
	}
	
}
