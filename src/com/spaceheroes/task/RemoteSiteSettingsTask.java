package com.spaceheroes.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.RemoteSiteSetting;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.ws.ConnectionException;
import com.spaceheroes.util.ConnectionFactory;

public class RemoteSiteSettingsTask extends SalesforceTask {
	
	private String name;
	private String description;
	private String url;
	private Boolean disableProtocolSecurity = Boolean.FALSE;
	private Boolean active = Boolean.TRUE;
	
	@Override
	public void execute() throws BuildException {
		RemoteSiteSetting rss = new RemoteSiteSetting();
		rss.setFullName(name);
		rss.setDescription(description);
		rss.setDisableProtocolSecurity(disableProtocolSecurity);
		rss.setIsActive(active);
		rss.setUrl(url);
		try {
			MetadataConnection mc = ConnectionFactory.getMetadataConnection(getConfig());
			log(String.format("Updating Remote Site Settings (%s)...", name));
			
			// delete (if present)
			mc.deleteMetadata("RemoteSiteSetting", new String[] { name });
			
			// create new setting
			SaveResult[] results = mc.createMetadata(new Metadata[] {rss});
			for (SaveResult result : results) {
				if (!result.isSuccess()) {
					List<String> errors = new ArrayList<String>();
					for (com.sforce.soap.metadata.Error e : result.getErrors()) {
						errors.add(e.getMessage());
					}
					BuildException e = new BuildException(StringUtils.join(errors, "; "));
					handleException(e);
				}
			}
		} catch (ConnectionException e) {
			handleException(e);
		}
	}
	
	public void addText(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getDisableProtocolSecurity() {
		return disableProtocolSecurity;
	}

	public void setDisableProtocolSecurity(Boolean disableProtocolSecurity) {
		this.disableProtocolSecurity = disableProtocolSecurity;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
