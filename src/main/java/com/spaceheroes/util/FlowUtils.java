package com.spaceheroes.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.spaceheroes.xml.sfdc.FlowDefinition;
import com.spaceheroes.xml.sfdc.Manifest;
import com.spaceheroes.xml.sfdc.ManifestType;

public class FlowUtils {
	
	public static void createFlowInactivationPack(List<String> flowNames) throws IOException {
		createFlowDefinitionManifest(flowNames);
		for (String flowName : flowNames) {
			createInactiveDefinition(flowName);
		}
	}
	
	public static File createFlowDefinitionManifest(List<String> flowNames) throws IOException {
		Serializer serializer = new Persister();
		File source = new File("package.xml");
		FileUtils.forceMkdirParent(source);
		Manifest m = new Manifest();
		ManifestType mt = new ManifestType("FlowDefinition");
		for (String name : flowNames) {
			mt.addMember(name);
		}
		m.addType(mt);
		try {
			serializer.write(m, source);
			return source;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Unable to create package manifest");
		}
	}
	
	public static File createFlowManifest(String filename, List<String> flowNames) throws IOException {
		Serializer serializer = new Persister();
		File source = new File("package.xml");
		FileUtils.forceMkdirParent(source);
		Manifest m = new Manifest();
		ManifestType mt = new ManifestType("Flow");
		for (String name : flowNames) {
			mt.addMember(name);
		}
		m.addType(mt);
		try {
			serializer.write(m, source);
			return source;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Unable to create package manifest");
		}
	}
	
	public static File createInactiveDefinition(String flowName) throws IOException {
		String filename = "flowDefinitions/" +  flowName + ".flowDefinition";
		Serializer serializer = new Persister();
		File source = new File(filename);
		FileUtils.forceMkdirParent(source);
		FlowDefinition fd = new FlowDefinition();
		try {
			serializer.write(fd, source);
			return source;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Unable to create flow definition");
		}
	}

}
