package com.spaceheroes.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.spaceheroes.task.SalesforceTask;
import com.spaceheroes.util.FileUtils;
import com.spaceheroes.util.PackageXMLCreator;

/**
 * @name DestructiveFlowsPackage
 * @description: This class implements the new ant task that compare the flows from a origin organization 
 * with the destination organization and remove from the destination organization if the flows exits in
 * both organizations.  
 * @author jesus.cantero
 */
public class DestructiveFlowsPackage extends SalesforceTask {
	// Static string for xml nodes and folders names
	public static final String FLOW = "flows";
	public static final String FLOW_DEFINITION = "flowDefinitions";
	public static final String XML_NODE_FLOW = "Flow";
	public static final String XML_NODE_FLOW_DEFINITION = "FlowDefinition";
	public static final String XML_NODE_FLOW_DEFINITION_ACTIVE_VERSION = "activeVersionNumber";
	public static final String XML_EXTENSION = ".xml";
	public static final String EXTRA_FOLDER = "Deploy";
	// Message for exception for folder unknown
	public static final String MESSAGE_UNKNOWN_FOLDER = "The folder doesn't exist: %s";
	public static final String MESSAGE_WRONG_DEACTIVATION = "The deactivation of the flow failed."; 
	// Local variables
	protected String originRetrievePath; // Path of the origin flow to be deployed
	protected String destinationRetrievePath; // Path of the destination org flows retrieve
	protected String destinationDestructivePackage; // Path of the destructive file to create

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
	
	public DestructiveFlowsPackage(){
		super();
	}
	
	/**
	 * @name execute
	 * @description: This funcion execute the async task and read the folder
	 * for the destination and origin flows and creates the destructive package
	 * with the flow and flow definition that match 
	 * @author jesus.cantero
	 */
	@Override
	public void execute() throws BuildException {
		
		List<String> matchesFlows = this.getCommonElements(readFlowsRetrived(this.originRetrievePath), readFlowsRetrived(this.destinationRetrievePath));
		List<String> matchesFlowDefinitions = this.getCommonElements(readFlowDefintionssRetrived(this.originRetrievePath), readFlowDefintionssRetrived(this.destinationRetrievePath));
		try {
			deactiveFlows(matchesFlowDefinitions, readElementsRetrivedFullPath(this.destinationRetrievePath + EXTRA_FOLDER, FLOW_DEFINITION));
			cleanDeactivationFolder(matchesFlowDefinitions, readElementsRetrivedFullPath(this.destinationRetrievePath + EXTRA_FOLDER, FLOW_DEFINITION));
		} catch (ParserConfigurationException e) {
			throw new BuildException(String.format(MESSAGE_WRONG_DEACTIVATION, e));
		} catch (SAXException e){
			throw new BuildException(String.format(MESSAGE_WRONG_DEACTIVATION, e));
		} catch (IOException e){
			throw new BuildException(String.format(MESSAGE_WRONG_DEACTIVATION, e));
		} catch (TransformerException e) {
			throw new BuildException(String.format(MESSAGE_WRONG_DEACTIVATION, e));
		}
		Map<String, List<String>> matchesElements = new HashMap<String, List<String>>();
		matchesElements.put(XML_NODE_FLOW, matchesFlows);
		this.createDestructivePackage(matchesElements);
	}
	
	
	/**
	 * @name readFlowsRetrived
	 * @description: Read the flow elements in the folder receive of paramenter
	 * @param String pPath: path that contains the flows files
	 * @return List<String> list of flow files in the folder.
	 * @author jesus.cantero
	 */
	public List<String> readFlowsRetrived(String pPath) throws BuildException{
		return readElementsRetrived(pPath, FLOW);
	}
	
	/**
	 * @name readFlowDefintionssRetrived
	 * @description: Read the flow definition elements in the folder receive of paramenter
	 * @param String pPath: path that contains the flows files
	 * @return List<String> list of flow definitio files in the folder.
	 * @author jesus.cantero
	 */
	public List<String> readFlowDefintionssRetrived(String pPath) throws BuildException{
		return readElementsRetrived(pPath, FLOW_DEFINITION);
	}
	
	/**
	 * @name readElementsRetrived
	 * @description: Read the type elements in the folder both received as parameter
	 * @param String pPath: path that contains the flows files
	 * @param String pObjectType: Object type to be read
	 * @return List<String> list of elements type files in the folder.
	 * @author jesus.cantero
	 */
	protected List<String> readElementsRetrived(String pPath, String pObjectType) throws BuildException{
		String fullPath = FileUtils.getCorrectFolderPath(pPath, pObjectType);
		final File folder = new File(fullPath);
		if (!folder.exists() || !folder.isDirectory()){
			throw new BuildException(String.format(MESSAGE_UNKNOWN_FOLDER, fullPath));
		}
		
		return FileUtils.listFilesForFolder(folder);
	}
	
	/**
	 * @name readElementsRetrivedFullPath
	 * @description: Read the full paht type elements in the folder both received as 
	 * parameter
	 * @param String pPath: path that contains the flows files
	 * @param String pObjectType: Object type to be read
	 * @return List<String> list of elements type files in the folder.
	 * @author jesus.cantero
	 */
	protected List<String> readElementsRetrivedFullPath(String pPath, String pObjectType) throws BuildException{
		String fullPath = FileUtils.getCorrectFolderPath(pPath, pObjectType);
		final File folder = new File(fullPath);
		if (!folder.exists() || !folder.isDirectory()){
			throw new BuildException(String.format(MESSAGE_UNKNOWN_FOLDER, fullPath));
		}
		
		return FileUtils.listFilesForFolderFullPath(folder);
	}

	/**
	 * @name getCommonElements
	 * @description: Get the elements with the same name in the two list recevied
	 * as parameter
	 * @param List<String> pOriginOrg: List of elements in the origin org
	 * @param List<String> pDestination: List of elements in the destination org
	 * @return List<String> list of elements that are in the both List. The version and
	 * the extension are not used to compare them.
	 * @author jesus.cantero
	 */
	protected List<String> getCommonElements(List<String> pOriginOrg, List<String> pDestination){
		List<String> listToReturn = new ArrayList<String>();
		for(String origin : pOriginOrg){
			
			for(String destination : pDestination){
				if (FileUtils.getNormalizedFileName(destination).equals(FileUtils.getNormalizedFileName(origin))){
					listToReturn.add(FileUtils.getFileNameWithoutExtension(destination));
				}
			}
		}
		return listToReturn;
	}
	
	/**
	 * @name createDestructivePackage
	 * @description: Create the destructive package with the elements of the map as parameter.
	 * The key of the map is the type of element and the value the list of elements.
	 * @param Map<String,List<String>> pElementsToBeAdded: Map with key the element type and 
	 * the value of the list of name of elements
	 * @author jesus.cantero
	 */
	protected void createDestructivePackage(Map<String,List<String>> pElementsToBeAdded) throws BuildException{
		try {
			PackageXMLCreator.createPackageXML(pElementsToBeAdded, destinationDestructivePackage);
		} catch (TransformerException e) {
			throw new BuildException(e);
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		}
	}
	
	/**
	 * @name deactiveFlows
	 * @description: Inactive all the flows that match between the origin and destination.
	 * The function is updating the activeVersionNumber of the flow definition to zero.
	 * @param List<String> pFlowsToBeDeactivated: Flows to be inactive
	 * @param List<String> pFlowsDefFiles: List of full paths of the flow definition to be
	 * updated
	 * @return void
	 * @author jesus.cantero
	 */
	protected void deactiveFlows(List<String> pFlowsToBeInactivated, List<String> pFlowsDefFiles) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		for (String flowName : pFlowsToBeInactivated){
			for(String fileName : pFlowsDefFiles){
				if (fileName.toLowerCase().contains(flowName)){
					Document doc = PackageXMLCreator.readPackageXML(fileName);
					NodeList nList = doc.getElementsByTagName(XML_NODE_FLOW_DEFINITION);
					for (int iCont = 0; iCont < nList.getLength(); iCont++) {
						Node node = nList.item(iCont);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							NodeList nodeElements = element.getElementsByTagName(XML_NODE_FLOW_DEFINITION_ACTIVE_VERSION);
							for (int iCont2 = 0; iCont2 < nodeElements.getLength(); iCont2++){
								nodeElements.item(iCont2).setTextContent("0");
								nodeElements.item(iCont2).getChildNodes().item(0).setNodeValue("0");
							}
						}
					}
					PackageXMLCreator.writePackageXML(doc, fileName);
				}
			}
		}
	}
	
	/**
	 * @name cleanDeactivationFolder
	 * @description: Delete the flow definitions that don't change and it's not needed to 
	 * be deployed
	 * @param List<String> pMatchesFD: Flows to be inactive
	 * @param List<String> pFullPathFD: List of full paths of the flow definition to be
	 * updated
	 * @return void
	 * @author jesus.cantero
	 */
	protected void cleanDeactivationFolder(List<String> pMatchesFD, List<String> pFullPathFD){
		for(String path : pFullPathFD){
			for (String match : pMatchesFD){
				if (!path.contains(match)) {
					File file = new File(path);
		    		if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
				}
			}
		}
	}
	 
}
