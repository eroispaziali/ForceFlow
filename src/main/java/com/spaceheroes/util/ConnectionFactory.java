package com.spaceheroes.util;

import com.sforce.soap.apex.Connector;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ConnectionFactory {

	private static final String ENDPOINT_SOAP = "https://%s/services/Soap/u/%s/";
	
	public static PartnerConnection getPartnerConnection(SalesforceConfig config) throws ConnectionException {
		ConnectorConfig cc = new ConnectorConfig();
		cc.setUsername(config.getUsername());
		cc.setPassword(config.getPassword());
		cc.setManualLogin(false);
		String authEndpoint = String.format(ENDPOINT_SOAP, config.getServerUrl(), config.getApiVersion());
		cc.setAuthEndpoint(authEndpoint);
		PartnerConnection partnerConnection = com.sforce.soap.partner.Connector.newConnection(cc);
		partnerConnection.login(config.getUsername(), config.getPassword());
		return partnerConnection;
	}
	
	public static MetadataConnection getMetadataConnection(PartnerConnection pc, SalesforceConfig config) throws ConnectionException {
		LoginResult lr = pc.login(config.getUsername(), config.getPassword());
		ConnectorConfig cc = new ConnectorConfig();
		cc.setUsername(config.getUsername());
		cc.setPassword(config.getPassword());
		cc.setSessionId(lr.getSessionId());
		cc.setServiceEndpoint(lr.getMetadataServerUrl());
		cc.setManualLogin(false);
		MetadataConnection connection = com.sforce.soap.metadata.Connector.newConnection(cc);
		return connection;
	}
	
	public static MetadataConnection getMetadataConnection(SalesforceConfig config) throws ConnectionException {
		PartnerConnection pc = getPartnerConnection(config);
		return getMetadataConnection(pc, config);
	}
	
	public static SoapConnection getToolingConnection(PartnerConnection pc, SalesforceConfig config) throws ConnectionException {
		LoginResult lr = pc.login(config.getUsername(), config.getPassword());
		ConnectorConfig toolingConfig = new ConnectorConfig();
		toolingConfig.setSessionId(pc.getSessionHeader().getSessionId());
		toolingConfig.setServiceEndpoint(lr.getServerUrl().replace("Soap/u/", "Soap/s/"));
		toolingConfig.setManualLogin(false);
		SoapConnection soapConnection = Connector.newConnection(toolingConfig);
		return soapConnection;
	}
	
	public static SoapConnection getToolingConnection(SalesforceConfig config) throws ConnectionException {
		PartnerConnection pc = getPartnerConnection(config);
		return getToolingConnection(pc, config);
	}
}
