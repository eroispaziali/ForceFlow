package com.spaceheroes.model;

import java.io.File;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class FlowFile {
	
	private File file;
	private String name;
	private Integer version;
	
	public FlowFile(File f) {
		this.file = Objects.requireNonNull(f);
		String srcFileNameNoExt = StringUtils.substringBeforeLast(f.getName(), ".");
		this.name = StringUtils.substringBeforeLast(srcFileNameNoExt, "-");
		String flowNumber = StringUtils.substringAfterLast(srcFileNameNoExt, "-");
		this.version = StringUtils.isNumeric(flowNumber) ? Integer.valueOf(flowNumber) : 1;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFilename(Integer version) {
		String destFilename = 
				(version>0) 
				? String.format("%s-%d.flow", name, version) 
				: String.format("%s.flow", name) ;
		return destFilename;
	}
	
	public String getFilenameCurrentVersion() {
		return getFilename(version);
	}
	
	public String getFilenameNextVersion() {
		return getFilename(version+1);
	}

	public Integer getVersion() {
		return version;
	}

	public File getFile() {
		return file;
	}

}
