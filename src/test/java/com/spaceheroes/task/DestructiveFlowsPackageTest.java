package com.spaceheroes.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.spaceheroes.task.DestructiveFlowsPackage;
import com.spaceheroes.util.FileUtils;
import com.spaceheroes.util.PackageXMLCreator;

public class DestructiveFlowsPackageTest {
	
	public static final String RESOURCES_FOLDER = "src/test/resources";
	public static final String RESOURCES_FOLDER_ORIGIN = "origin";
	public static final String RESOURCES_FOLDER_ORIGIN_NO_FLOW = "origin_no_flow";
	public static final String RESOURCES_FOLDER_ORIGIN_NO_FLOW_DEFINITION = "origin_no_flow_definition";
	public static final String RESOURCES_FOLDER_DESTINATION = "destination";
	public static final String RESOURCES_FOLDER_DESTINATION_NO_FLOW = "destination_no_flow";
	public static final String RESOURCES_FOLDER_DESTINATION_NO_FLOW_DEFINITION = "destination_no_flow_definition";
	public static final String RESOURCES_FOLDER_RESULT = "result/destructive.xml";
	public static final String CREATION_FOLDER_FORMAT_STRING = "%s/%s/%s";
	
	public static final int EXPECTED_MEMBERS_NODES = 1;
	public static final List<String> EXPECTED_ELEMENTS_IN_XML = new ArrayList<String>(); 
	
	
	@Before
	public void testFolderTestSetup() {
		// Folders checking variables
		String originFlows;
		String destinationFlows;
		String originFlowDefinitions;
		String destinationFlowDefinitions;
		File originFolderFlows;
		File destinationFolderFlows;
		File originFolderFlowDefinitions;
		File destinationFolderFlowDefinitions;
		
		
		printMessage("*** Checking if the origin and destination folders exists. ***");
		originFlows = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN), DestructiveFlowsPackage.FLOW);
		destinationFlows = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION), DestructiveFlowsPackage.FLOW);
		originFolderFlows = new File(originFlows);
		destinationFolderFlows = new File(destinationFlows);
		
		Assert.assertTrue(originFolderFlows.exists());
		printMessage("    " + originFolderFlows.getAbsolutePath()  +  "-Exist.");
		Assert.assertTrue(originFolderFlows.isDirectory());
		printMessage("    " + originFolderFlows.getAbsolutePath()  +  "-Is a directory.");
		Assert.assertTrue(destinationFolderFlows.exists());
		printMessage("    " + destinationFolderFlows.getAbsolutePath()  +  "-Exist.");
		Assert.assertTrue(destinationFolderFlows.isDirectory());
		printMessage("    " + destinationFolderFlows.getAbsolutePath()  +  "-Is a directory.");
		
		originFlowDefinitions = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN), DestructiveFlowsPackage.FLOW_DEFINITION);
		destinationFlowDefinitions = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION), DestructiveFlowsPackage.FLOW_DEFINITION);
		originFolderFlowDefinitions = new File(originFlowDefinitions);
		destinationFolderFlowDefinitions = new File(destinationFlowDefinitions);
		Assert.assertTrue(originFolderFlowDefinitions.exists());
		printMessage("    " + originFolderFlowDefinitions.getAbsolutePath()  +  "-Exist.");
		Assert.assertTrue(originFolderFlowDefinitions.isDirectory());
		printMessage("    " + originFolderFlowDefinitions.getAbsolutePath()  +  "-Is a directory.");
		Assert.assertTrue(destinationFolderFlowDefinitions.exists());
		printMessage("    " + destinationFolderFlowDefinitions.getAbsolutePath()  +  "-Exist.");
		Assert.assertTrue(destinationFolderFlowDefinitions.isDirectory());
		printMessage("    " + destinationFolderFlowDefinitions.getAbsolutePath()  +  "-Is a directory.");
		
		printMessage("*** Checking if the origin and destination folders exists. ***");
		originFlows = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN_NO_FLOW), DestructiveFlowsPackage.FLOW);
		destinationFlows = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION_NO_FLOW), DestructiveFlowsPackage.FLOW);
		originFolderFlows = new File(originFlows);
		destinationFolderFlows = new File(destinationFlows);
		
		Assert.assertTrue(!originFolderFlows.exists());
		printMessage("    " + originFolderFlows.getAbsolutePath()  +  "-Doesn't exist.");
		Assert.assertTrue(!originFolderFlows.isDirectory());
		printMessage("    " + originFolderFlows.getAbsolutePath()  +  "-Is not a directory.");
		Assert.assertTrue(!destinationFolderFlows.exists());
		printMessage("    " + destinationFolderFlows.getAbsolutePath()  +  "-Doesn't xist.");
		Assert.assertTrue(!destinationFolderFlows.isDirectory());
		printMessage("    " + destinationFolderFlows.getAbsolutePath()  +  "-Is not a directory.");
		
		originFlowDefinitions = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN_NO_FLOW_DEFINITION), DestructiveFlowsPackage.FLOW_DEFINITION);
		destinationFlowDefinitions = FileUtils.getCorrectFolderPath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION_NO_FLOW_DEFINITION), DestructiveFlowsPackage.FLOW_DEFINITION);
		originFolderFlowDefinitions = new File(originFlowDefinitions);
		destinationFolderFlowDefinitions = new File(destinationFlowDefinitions);
		Assert.assertTrue(!originFolderFlowDefinitions.exists());
		printMessage("    " + originFolderFlowDefinitions.getAbsolutePath()  +  "-Doesn't exist.");
		Assert.assertTrue(!originFolderFlowDefinitions.isDirectory());
		printMessage("    " + originFolderFlowDefinitions.getAbsolutePath()  +  "-Is not a directory.");
		Assert.assertTrue(!destinationFolderFlowDefinitions.exists());
		printMessage("    " + destinationFolderFlowDefinitions.getAbsolutePath()  +  "-Doesn't exist.");
		Assert.assertTrue(!destinationFolderFlowDefinitions.isDirectory());
		printMessage("    " + destinationFolderFlowDefinitions.getAbsolutePath()  +  "-Is not a directory.");
		
	}
	@Test
	public void testFlowDestructivePackageSuccess() {
		printMessage("*** STARTING TEST ***.");
		DestructiveFlowsPackage toTest = new DestructiveFlowsPackage();
		String destinationFlows;
		String originFlowDefinitions;
		String destinationFlowDefinitions;
		final File destinationFolderFlows;
		final File destinationFolderFlowDefinitions;
		//XML reading variables
		File fXmlFile;
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		Document doc;
		NodeList nList;
		EXPECTED_ELEMENTS_IN_XML.add(DestructiveFlowsPackage.XML_NODE_FLOW.toLowerCase());
		EXPECTED_ELEMENTS_IN_XML.add(DestructiveFlowsPackage.XML_NODE_FLOW_DEFINITION.toLowerCase());
		
		toTest.setOriginRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN));
		toTest.setDestinationRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION));
		toTest.setDestinationDestructivePackage(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_RESULT));
		destinationFlows = FileUtils.getCorrectFolderPath(toTest.getDestinationRetrievePath(), DestructiveFlowsPackage.FLOW);
		destinationFolderFlows = new File(destinationFlows);
		destinationFlowDefinitions = FileUtils.getCorrectFolderPath(toTest.getDestinationRetrievePath(), DestructiveFlowsPackage.FLOW_DEFINITION);
		destinationFolderFlowDefinitions = new File(destinationFlowDefinitions);
		printMessage(toTest.getDestinationRetrievePath());
		toTest.execute();
		
		List<String> flowsInFolder = new ArrayList<String>();
		for (String fileName : FileUtils.listFilesForFolder(destinationFolderFlows)){
			flowsInFolder.add(FileUtils.getFileNameWithoutExtension(fileName));
		}
		for (String fileName : FileUtils.listFilesForFolder(destinationFolderFlowDefinitions)){
			flowsInFolder.add(FileUtils.getFileNameWithoutExtension(fileName));
		}
		
		fXmlFile = new File(toTest.getDestinationDestructivePackage());
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			nList = doc.getElementsByTagName(PackageXMLCreator.TYPES_NODE);
			printMessage("*** Checking the destructive package created. ***");
			printMessage("    Number of member nodes: " +  nList.getLength());
			Assert.assertEquals(EXPECTED_MEMBERS_NODES, nList.getLength());
			printMessage(flowsInFolder);
			for (int iCont = 0; iCont < nList.getLength(); iCont++){
				Node parentNode = nList.item(iCont);
				NodeList childs = parentNode.getChildNodes();
				for (int iCont2 = 0; iCont2< childs.getLength(); iCont2 ++){
					Node node = childs.item(iCont2);
					printMessage("        node.getNodeName(): " + node.getNodeName());
					if (node.getNodeName().equals(PackageXMLCreator.NAME_NODE)){
						printMessage("    Node Name value: " + node.getTextContent().toLowerCase());
						Assert.assertTrue(EXPECTED_ELEMENTS_IN_XML.contains(node.getTextContent().toLowerCase()));
					} else if (node.getNodeName().equals(PackageXMLCreator.MEMBERS_NODE)){
						printMessage("    Node Member value: " + node.getTextContent());
						Assert.assertTrue(flowsInFolder.contains(node.getTextContent()));
					} else {
						Assert.assertTrue(false);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			printMessage(e.getMessage());
			return;
		} catch (SAXException e) {
			printMessage(e.getMessage());
			return;
		} catch (IOException e) {
			printMessage(e.getMessage());
			return;
		}
		
		printMessage("*** FINISHED TEST ***.");
	}

	@Test
	public void testFlowDestructivePackageErrors() {
		printMessage("*** STARTING TEST ***.");
		DestructiveFlowsPackage toTest = new DestructiveFlowsPackage();
		
		toTest.setOriginRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"), RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN_NO_FLOW));
		toTest.setDestinationRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION_NO_FLOW));
		toTest.setDestinationDestructivePackage(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_RESULT));
		
		printMessage("*** Checking if the origin and destination folders exists. ***");
		
		try{
			toTest.execute();
		} catch(BuildException e){
			printMessage(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getOriginRetrievePath() + "/" + DestructiveFlowsPackage.FLOW));
			printMessage(e.getMessage());
			Assert.assertEquals(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getOriginRetrievePath() + "/" + DestructiveFlowsPackage.FLOW), e.getMessage());
		}
		toTest.setOriginRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"), RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN));
		try{
			toTest.execute();
		} catch(BuildException e){
			printMessage(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getDestinationRetrievePath() + "/" + DestructiveFlowsPackage.FLOW));
			printMessage(e.getMessage());
			Assert.assertEquals(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getDestinationRetrievePath() + "/" + DestructiveFlowsPackage.FLOW), e.getMessage());
		}
		toTest.setDestinationRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION));
		toTest.setOriginRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"), RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN_NO_FLOW_DEFINITION));
		try{
			toTest.execute();
		} catch(BuildException e){
			printMessage(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getOriginRetrievePath() + "/" + DestructiveFlowsPackage.FLOW_DEFINITION));
			printMessage(e.getMessage());
			Assert.assertEquals(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getOriginRetrievePath() + "/" + DestructiveFlowsPackage.FLOW_DEFINITION), e.getMessage());
		}
		toTest.setDestinationRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"),RESOURCES_FOLDER, RESOURCES_FOLDER_DESTINATION_NO_FLOW_DEFINITION));
		toTest.setOriginRetrievePath(String.format(CREATION_FOLDER_FORMAT_STRING, System.getProperty("user.dir"), RESOURCES_FOLDER, RESOURCES_FOLDER_ORIGIN));
		try{
			toTest.execute();
		} catch(BuildException e){
			printMessage(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getDestinationRetrievePath() + "/" + DestructiveFlowsPackage.FLOW_DEFINITION));
			printMessage(e.getMessage());
			Assert.assertEquals(String.format(DestructiveFlowsPackage.MESSAGE_UNKNOWN_FOLDER, toTest.getDestinationRetrievePath() + "/" + DestructiveFlowsPackage.FLOW_DEFINITION), e.getMessage());
		}
	}
	
	protected void printMessage(Object pMessage){
		System.out.println(pMessage);
		System.out.flush();
	}
}
