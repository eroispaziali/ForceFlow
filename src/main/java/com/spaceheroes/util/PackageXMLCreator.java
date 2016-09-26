package com.spaceheroes.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @name PackageXMLCreator
 * @description: Utils funcions to create a package  
 * @author jesus.cantero
 */
public class PackageXMLCreator {
	// Static string with the node name of packages
	public static final String PACKAGE_NODE = "Package";
	public static final String TYPES_NODE = "types";
	public static final String MEMBERS_NODE = "members";
	public static final String NAME_NODE = "name";
	
	/**
	 * @name createPackageXML
	 * @description: This functions create the package with the elements in the
	 * param map.
	 * @param Map<String, List<String>> pElementsByObjectType: map with the key as
	 * the element types
	 * @param  String pPathFile: Path of the file to be save
	 * @author jesus.cantero
	 */
	public static void createPackageXML(Map<String, List<String>> pElementsByObjectType, String pPathFile) throws TransformerException, ParserConfigurationException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(PACKAGE_NODE);
		doc.appendChild(rootElement);
		for (String key : pElementsByObjectType.keySet()){
			List<String> members = pElementsByObjectType.get(key);
			Element types = doc.createElement(TYPES_NODE);
			for(String memberValue : members) {
				Element member = doc.createElement(MEMBERS_NODE);
				member.appendChild(doc.createTextNode(memberValue));
				types.appendChild(member);
			}
			Element name = doc.createElement(NAME_NODE);
			name.appendChild(doc.createTextNode(key));
			types.appendChild(name);
			rootElement.appendChild(types);
			
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(pPathFile));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

	}

}