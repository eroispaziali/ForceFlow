package com.spaceheroes.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.spaceheroes.model.FlowFile;
import com.spaceheroes.xml.sfdc.FlowDefinition;
import com.spaceheroes.xml.sfdc.Manifest;
import com.spaceheroes.xml.sfdc.ManifestType;

public class FlowUtils {
	
	private static final String METADATA_TYPE_FLOW = "Flow";
	private static final String METADATA_TYPE_FLOWDEFINITION = "FlowDefinition";
	private static final String FLOW_FILENAME_PATTERN = ".+(-([0-9]+))?.flow";
	
	private static void copyFlowsAndIncreaseVersion(Manifest manifest, String srcPath, String destPath) {
		File dir = new File(srcPath);
		File[] directoryFiles = dir.listFiles();
		ManifestType manifestType = new ManifestType(METADATA_TYPE_FLOW);
		Map<String, FlowFile> versionMap = new HashMap<String, FlowFile>();
		if (directoryFiles != null) {
			for (File srcFile : directoryFiles) {
				String srcFilename = srcFile.getName();
				if (srcFilename.matches(FLOW_FILENAME_PATTERN)) {
					FlowFile ff = new FlowFile(srcFile);
					String key = ff.getName();
					FlowFile latestVersion = versionMap.getOrDefault(key, ff);
					if (ff.getVersion() > latestVersion.getVersion()) {
						versionMap.put(key, ff);
					} else {
						versionMap.put(key, latestVersion);
					}
				}
			}
		}
		
		for (FlowFile ff : versionMap.values()) {
			manifestType.addMember(ff.getFlowNameNextVersion());
			try {
				File destFile = new File(destPath + "/" + ff.getFilenameNextVersion());
				FileUtils.forceMkdirParent(destFile);
				FileUtils.copyFile(ff.getFile(), destFile);
			} catch (IOException e) {
				// ignore
				e.printStackTrace();
			}
		}
		manifest.addType(manifestType);
	}
	
	public static List<FlowFile> getFlowFiles(String srcPath) {
		File dir = new File(srcPath);
		File[] directoryFiles = dir.listFiles();
		List<FlowFile> flowFiles = new ArrayList<FlowFile>();
		if (directoryFiles != null) {
			for (File srcFile : directoryFiles) {
				String srcFilename = srcFile.getName();
				if (srcFilename.matches(FLOW_FILENAME_PATTERN)) {
					flowFiles.add(new FlowFile(srcFile));
				}
			}
		}
		return flowFiles;
	}
	
	public static void createFlowDeletionPack(String sourcePath, String outputPath) throws IOException {
		List<FlowFile> nextVersionFlows = FlowUtils.getFlowFiles(sourcePath + "/flows");
		FlowUtils.createFlowDeletionPack(outputPath, nextVersionFlows);
	}
	
	private static void createFlowInactivationPack(Manifest manifest, String path, List<FlowFile> flowFiles) throws IOException {
		File root = new File(path);
		createFlowDefinitionManifest(manifest , flowFiles);
		for (FlowFile flowName : flowFiles) {
			createInactiveDefinition(root, flowName);
		}
	}
	
	// public
	public static void createFlowDownloadAllPack(String path) throws IOException {
		File root = new File(path);
		createFlowManifest(root, "package.xml");
	}
	
	public static void createFlowInactivation(String srcPath, String outputPath, boolean createDestructiveChanges) throws IOException {
		String sourcePath = srcPath + "/flows";
		String flowsDestinationPath = outputPath + "/flows";
		List<FlowFile> flowFiles = FlowUtils.getFlowFiles(sourcePath);
		String manifestFilePath = outputPath + "/package.xml";
		File manifestFile = new File(manifestFilePath);
		Manifest manifest = new Manifest();
		copyFlowsAndIncreaseVersion(manifest, sourcePath, flowsDestinationPath);
		createFlowInactivationPack(manifest, outputPath, flowFiles);
		serializeXml(manifestFile, manifest);
		if (createDestructiveChanges) {
			createFlowDeletionPack(manifest, outputPath);
		}
	}
	
	private static void createFlowDeletionPack(Manifest existingManifest, String path) throws IOException {
		File root = new File(path);
		String filePath = root.getPath() + "/" + "destructiveChangesPost.xml";
		File manifestFile = new File(filePath);
		Manifest manifest = readOrCreateManifest(manifestFile);
		ManifestType manifestType = existingManifest.getType(METADATA_TYPE_FLOW);
		manifest.addType(manifestType);
		serializeXml(manifestFile, manifest);
		
		File packageFile = new File(path + "/" + "package.xml");
		if (!packageFile.exists()) {
			serializeXml(packageFile, new Manifest());
		}
	}
	
	private static void createFlowDeletionPack(String path, List<FlowFile> flowFiles) throws IOException {
		File root = new File(path);
		String filePath = root.getPath() + "/" + "destructiveChanges.xml";
		File manifestFile = new File(filePath);
		Manifest manifest = readOrCreateManifest(manifestFile);
		ManifestType manifestType = new ManifestType(METADATA_TYPE_FLOW);
		if (flowFiles!=null && flowFiles.size()>0) {
			for (FlowFile ff : flowFiles) {
				manifestType.addMember(ff.getFlowNameCurrentVersion());	
			}
		} 
		manifest.addType(manifestType);
		serializeXml(manifestFile, manifest);
		
		
		File packageFile = new File(path + "/" + "package.xml");
		if (!packageFile.exists()) {
			serializeXml(packageFile, new Manifest());
		}
		
	}
	
	private static void createFlowDefinitionManifest(Manifest manifest, List<FlowFile> flowFiles) throws IOException {
		ManifestType flowDefinitions = new ManifestType(METADATA_TYPE_FLOWDEFINITION);
		for (FlowFile ff : flowFiles) {
			flowDefinitions.addMember(ff.getName());	
		}
		manifest.addType(flowDefinitions);
	}
	
	public static File createFlowManifest(File root, String filename) throws IOException {
		return createFlowManifest(root, filename, null);
	}
	
	public static File createFlowManifest(File root, String filename, List<FlowFile> flowFiles) throws IOException {
		String filePath = root.getPath() + "/" + filename;
		File file = new File(filePath);
		Manifest manifest = readOrCreateManifest(file);
		ManifestType manifestType = new ManifestType(METADATA_TYPE_FLOW);
		if (flowFiles!=null && flowFiles.size()>0) {
			for (FlowFile ff : flowFiles) {
				manifestType.addMember(ff.getName());	
			}
		} else {
			manifestType.addMember("*");
		}
		manifest.addType(manifestType);
		return serializeXml(file, manifest);
	}
	
	public static File createInactiveDefinition(File root, FlowFile ff) throws IOException {
		String filename =  root.getPath() + "/flowDefinitions/" +  ff.getName() + ".flowDefinition";
		File f = new File(filename);
		FlowDefinition fd = new FlowDefinition();
		return serializeXml(f, fd);
	}
	
	private static File serializeXml(File output, Object object) throws IOException {
		Serializer serializer = new Persister();
		FileUtils.forceMkdirParent(output);
		try {
			serializer.write(object, output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Unable to serialize");
		}
	}
	
	public static Manifest readOrCreateManifest(File manifestFile) {
		Manifest manifest = new Manifest();
		if (manifestFile.exists()) {
			Serializer serializer = new Persister();
			try {
				manifest = serializer.read(Manifest.class, manifestFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return manifest;
	}

}
