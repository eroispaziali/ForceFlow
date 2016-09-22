package com.spaceheroes.task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.tools.ant.BuildException;

import com.spaceheroes.util.FileUtils;
import com.spaceheroes.util.PackageXMLCreator;

public class DestructiveFlowsPackage extends SalesforceTask {
	public static final String FLOW = "Flow";
	public static final String XML_EXTENSION = ".xml";
	
	public static final String MESSAGE_UNKNOWN_FOLDER = "The folder doesn't exist: %s"; 
	
	protected String originRetrievePath;
	protected String destinationRetrievePath;
	protected String destinationDestructivePackage;

	public String getDestinationDestructivePackage() {
		return destinationDestructivePackage;
	}

	public void setDestinationDestructivePackage(String destinationDestructivePackage) {
		this.destinationDestructivePackage = destinationDestructivePackage;
	}

	public String getOriginRetrievePath() {
		return originRetrievePath;
	}

	public void setOriginRetrievePath(String originRetrievePath) {
		this.originRetrievePath = originRetrievePath;
	}

	public String getDestinationRetrievePath() {
		return destinationRetrievePath;
	}

	public void setDestinationRetrievePath(String destinationRetrievePath) {
		this.destinationRetrievePath = destinationRetrievePath;
	}
	
	@Override
	public void execute() throws BuildException {
		List<String> originOrgElements = readFlowsRetrived(this.originRetrievePath);
		log("originOrgElements: " + originOrgElements);
		List<String> destinationOrgElements = readFlowsRetrived(this.destinationRetrievePath);
		log("destinationOrgElements: " + destinationOrgElements);
		List<String> matchesElements = this.getCommonElements(originOrgElements, destinationOrgElements);
		log("matchesElements: " + matchesElements);
		//this.createDestructivePackage(matchesElements);
	}
	
	protected List<String> readFlowsRetrived(String pPath) throws BuildException{
		return readFlowsRetrived(pPath, FLOW);
	}
	
	protected List<String> readFlowsRetrived(String pPath, String pObjectType) throws BuildException{
		String fullPath = FileUtils.getCorrectFolderPath(pPath, pObjectType);
		final File folder = new File(fullPath);
		if (!folder.exists() || !folder.isDirectory()){
			throw new BuildException(String.format(MESSAGE_UNKNOWN_FOLDER, fullPath));
		}
		
		return FileUtils.listFilesForFolder(folder);
	}

	protected List<String> getCommonElements(List<String> pOriginOrg, List<String> pDestination){
		List<String> listToReturn = new ArrayList<String>();
		for(String origin : pOriginOrg){
			
			for(String destination : pDestination){
				if (this.getNormalizedFileName(destination).equals(this.getNormalizedFileName(origin))){
					listToReturn.add(destination);
				}
			}
		}
		return listToReturn;
	}
	
	protected void createDestructivePackage(List<String> pElementsToBeAdded) throws BuildException{
		Map<String, List<String>> flows = new HashMap<String, List<String>>();
		flows.put(FLOW, pElementsToBeAdded);
		try {
			PackageXMLCreator.createPackageXML(flows, destinationDestructivePackage);
		} catch (TransformerException e) {
			throw new BuildException(e);
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		}
	}
	
	protected String getNormalizedFileName(String pFileName){
		String normalizedName = pFileName.toLowerCase().replace(XML_EXTENSION, "");
		if (normalizedName.indexOf("-") > -1){
			normalizedName = normalizedName.split("-")[0];
		}
		return normalizedName;
	}
	
	 
}
