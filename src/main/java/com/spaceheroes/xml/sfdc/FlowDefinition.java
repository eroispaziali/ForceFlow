package com.spaceheroes.xml.sfdc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="FlowDefinition")
public class FlowDefinition {

	@Attribute
	private String xmlns="http://soap.sforce.com/2006/04/metadata";
	
	@Element
	private String activeVersionNumber = "0";
	

}
