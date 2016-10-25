package com.spaceheroes.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import com.spaceheroes.util.FlowUtils;

public class FlowUtilsTest {
	
	@Test
	public void testFlowPackage() throws Exception {
		FlowUtils.createRetrieveAllFlowsManifest("target/test-tmp/flow-retrieve");
	}

	@Test
	public void testFlowInactivation() throws Exception {
		FlowUtils.createFlowInactivation("src/test/resources/src", "target/test-tmp/flow-inactivation", true);
	}
	
	@Test
	public void testFlowDeletion() throws Exception {
		FlowUtils.createFlowDeletionPack("src/test/resources/src", "target/test-tmp/flow-deletion");
	}
	
	@After
	public void tearDown() {
		FileUtils.deleteQuietly(new File("target/test-tmp"));
	}
	
}
