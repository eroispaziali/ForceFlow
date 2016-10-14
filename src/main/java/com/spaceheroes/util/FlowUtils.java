package com.spaceheroes.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.spaceheroes.xml.sfdc.FlowDefinition;
import com.spaceheroes.xml.sfdc.Manifest;
import com.spaceheroes.xml.sfdc.ManifestType;

public class FlowUtils {
	
	public static void createFlowInactivationPack(String path, List<String> flowNames) throws IOException {
		File root = new File(path);
		createFlowDefinitionManifest(root, flowNames);
		for (String flowName : flowNames) {
			createInactiveDefinition(root, flowName);
		}
	}
	
	public static void createFlowDownloadAllPack(String path) throws IOException {
		File root = new File(path);
		createFlowManifest(root);
	}
	
	public static void createFlowDeletionPack(String path, List<String> flowNames) throws IOException {
		File root = new File(path);
		createFlowManifest(root, flowNames);
	}
	
	private static File createFlowDefinitionManifest(File root, List<String> flowNames) throws IOException {
		Serializer serializer = new Persister();
		String filename = root.getPath() + "/" + "package.xml";
		File source = new File(filename);
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
	
	private static File createFlowManifest(File root) throws IOException {
		List<String> names = new ArrayList<String>();
		names.add("*");
		return createFlowManifest(root, names);
	}
	
	private static File createFlowManifest(File root, List<String> flowNames) throws IOException {
		Serializer serializer = new Persister();
		String filename = root.getPath() + "/" + "package.xml";
		File source = new File(filename);
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
	
	public static File createInactiveDefinition(File root, String flowName) throws IOException {
		String filename =  root.getPath() + "/flowDefinitions/" +  flowName + ".flowDefinition";
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
