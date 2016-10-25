package com.spaceheroes.task.local;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.spaceheroes.util.FlowUtils;

public class CreateFlowRetrieveManifestTask extends Task {
	
	private String destinationPath;

	@Override
	public void execute() throws BuildException {
		try {
			FlowUtils.createRetrieveAllFlowsManifest(destinationPath);
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
	
}
