package com.spaceheroes.xml.sfdc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<String> members = new HashSet<String>();
	
	@Element
	private String name = "Flow";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}
	
	public void addMember(String member) {
		this.members.add(member);
	}
	
	public void addMembers(List<String> members) {
		if (members!=null && !members.isEmpty()) {
			for (String member: members) {
				addMember(member);
			}
		}
	}
	

}
