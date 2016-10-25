package com.spaceheroes.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.spaceheroes.util.FlowUtils;
import com.spaceheroes.xml.sfdc.Manifest;


public class FlowUtilsTest {
	
	@Test
	public void testFlowPackage() throws Exception {
		String destinationPath = "target/test-tmp/flow-retrieve";
		FlowUtils.createRetrieveAllFlowsManifest(destinationPath);
		checkThatManifestExists(destinationPath, "package.xml");
	}

	@Test
	public void testFlowInactivation() throws Exception {
		String destinationPath = "target/test-tmp/flow-inactivation";
		FlowUtils.createFlowInactivation("src/test/resources/src", destinationPath, true);
		checkThatManifestExists(destinationPath, "package.xml");
		checkThatManifestExists(destinationPath, "destructiveChangesPost.xml");
	}
	
	@Test
	public void testFlowDeletion() throws Exception {
		String destinationPath = "target/test-tmp/flow-deletion";
		FlowUtils.createFlowDeletionPack("src/test/resources/src", destinationPath);
		checkThatManifestExists(destinationPath, "destructiveChanges.xml");
	}
	
	@After
	public void tearDown() {
		FileUtils.deleteQuietly(new File("target/test-tmp"));
	}
	
	private static void checkThatManifestExists(String path, String filename) {
		
		// Make sure that file exists, and it's not empty
		File file = new File(path + "/" + filename);
		Assert.assertTrue("Manifest must exist", file.exists());
		Assert.assertTrue("Manifest must not be empty", file.length()>0);
		
		// Make sure that file can be read correctly
		try {
			Serializer serializer = new Persister();
			serializer.read(Manifest.class, file);
		} catch (Exception e) {
			Assert.fail("Manifest must be valid and deserializable");
		}
	}
	
}
