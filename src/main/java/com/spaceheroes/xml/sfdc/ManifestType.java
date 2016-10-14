package com.spaceheroes.xml.sfdc;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="types")
public class ManifestType {

	public ManifestType() {
		super();
	}
	
	public ManifestType(String name) {
		super();
		this.name = name;
	}
	
	@ElementList(inline=true, name="members", required=false, entry="members")
	private List<String> members = new ArrayList<String>();
	
	@Element
	private String name = "Flow";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
	
	public void addMember(String member) {
		this.members.add(member);
	}
	

}
