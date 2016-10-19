package com.spaceheroes.task.local;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.spaceheroes.util.FlowUtils;

public class CreateFlowInactivationsPackage extends Task {
	
	private String sourcePath;
	private String destinationPath;
	private Boolean destructiveChanges = Boolean.FALSE;

	@Override
	public void execute() throws BuildException {
		try {
			FlowUtils.createFlowInactivation(sourcePath, destinationPath, destructiveChanges);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		super.execute();
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public Boolean getDestructiveChanges() {
		return destructiveChanges;
	}

	public void setDestructiveChanges(Boolean destructiveChanges) {
		this.destructiveChanges = destructiveChanges;
	}
	
}
