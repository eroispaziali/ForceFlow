package com.spaceheroes.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.spaceheroes.xml.sfdc.FlowDefinition;
import com.spaceheroes.xml.sfdc.Manifest;
import com.spaceheroes.xml.sfdc.ManifestType;

public class FlowUtils {
	
	public static void copyFlowsAndIncreaseVersion(String srcPath, String destPath) {
		File dir = new File(srcPath);
		File[] directoryFiles = dir.listFiles();
		if (directoryFiles != null) {
			for (File srcFile : directoryFiles) {
				String srcFilename = srcFile.getName();
				if (srcFilename.matches(".+(-([0-9]+))?.flow")) {
					String srcFileNameNoExt = StringUtils.substringBeforeLast(srcFilename, ".");
					String flowName = StringUtils.substringBeforeLast(srcFileNameNoExt, "-");
					String flowNumber = StringUtils.substringAfterLast(srcFileNameNoExt, "-");
					Integer flowVersion = StringUtils.isNumeric(flowNumber) ? Integer.valueOf(flowNumber) : 1;
					String destFilename = 
							(flowVersion>0) 
							? String.format("%s-%d.flow", flowName, flowVersion+1) 
							: String.format("%s.flow", flowName) ;
					try {
						File destFile = new File(destPath + "/" + destFilename);
						FileUtils.forceMkdirParent(destFile);
						FileUtils.copyFile(srcFile, destFile);
						System.out.println(destFile.getAbsolutePath().toString());
					} catch (IOException e) {
						// ignore
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static List<String> getFlowsNames(String srcPath) {
		File dir = new File(srcPath);
		File[] directoryFiles = dir.listFiles();
		List<String> flowNames = new ArrayList<String>();
		if (directoryFiles != null) {
			for (File srcFile : directoryFiles) {
				String srcFilename = srcFile.getName();
				if (srcFilename.matches(".+(-([0-9]+))?.flow")) {
					String srcFileNameNoExt = StringUtils.substringBeforeLast(srcFilename, ".");
					String flowName = StringUtils.substringBeforeLast(srcFileNameNoExt, "-");
					flowNames.add(flowName);
				}
			}
		}
		return flowNames;
	}
	
	public static void createFlowInactivationPack(String flowPath, String outputPath) throws IOException {
		List<String> flowNames = getFlowsNames(flowPath);
		File outputRoot = new File(outputPath);
		createFlowDefinitionManifest(outputRoot, flowNames);
		for (String flowName : flowNames) {
			createInactiveDefinition(outputRoot, flowName);
		}
	}
	
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
		String filename = root.getPath() + "/" + "package.xml";
		Manifest manifest = new Manifest();
		ManifestType manifestType = new ManifestType("FlowDefinition");
		manifestType.addMembers(flowNames);
		manifest.addType(manifestType);
		return serializeXml(filename, manifest);
	}
	
	private static File createFlowManifest(File root) throws IOException {
		List<String> names = new ArrayList<String>();
		names.add("*");
		return createFlowManifest(root, names);
	}
	
	private static File createFlowManifest(File root, List<String> flowNames) throws IOException {
		String filename = root.getPath() + "/" + "package.xml";
		Manifest manifest = new Manifest();
		ManifestType manifestType = new ManifestType("Flow");
		manifestType.addMembers(flowNames);
		manifest.addType(manifestType);
		return serializeXml(filename, manifest);
	}
	
	public static File createInactiveDefinition(File root, String flowName) throws IOException {
		String filename =  root.getPath() + "/flowDefinitions/" +  flowName + ".flowDefinition";
		FlowDefinition fd = new FlowDefinition();
		return serializeXml(filename, fd);
	}
	
	private static File serializeXml(String filename, Object object) throws IOException {
		Serializer serializer = new Persister();
		File output = new File(filename);
		FileUtils.forceMkdirParent(output);
		try {
			serializer.write(object, output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Unable to serialize");
		}
	}

}
