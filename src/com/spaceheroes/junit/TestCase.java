package com.spaceheroes.junit;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="testcase")
public class TestCase {
	
	@Attribute
	private Double time;
	
	@Attribute
	private String name;
	
	@ElementList(inline=true, name="testfailures", required=false)
	private List<TestFailure> testFailures = new ArrayList<TestFailure>();

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestFailure> getTestFailures() {
		return testFailures;
	}

	public void setTestFailures(List<TestFailure> testFailures) {
		this.testFailures = testFailures;
	}
	
	public void addTestFailure(TestFailure testFailure) {
		testFailures.add(testFailure);
	}
	
}
