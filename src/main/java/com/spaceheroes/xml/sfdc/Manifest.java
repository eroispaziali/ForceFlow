package com.spaceheroes.xml.sfdc;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Package")
public class Manifest {
	
	@Attribute
	private String xmlns="http://soap.sforce.com/2006/04/metadata";
	
	@ElementList(inline=true, name="types", required=false)
	private List<ManifestType> types = new ArrayList<ManifestType>();
	
	@Element
	private String version = "37.0";

	public Manifest() {
		super();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ManifestType> getTypes() {
		return types;
	}

	public void setTypes(List<ManifestType> types) {
		this.types = types;
	}
	
	public void addType(ManifestType type) {
		this.types.add(type);
	}
	
}
