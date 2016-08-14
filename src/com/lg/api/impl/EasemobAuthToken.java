package com.lg.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lg.api.AuthTokenAPI;
import com.lg.api.comm.BodyWrapper;
import com.lg.api.comm.EasemobRestAPI;
import com.lg.api.comm.HTTPMethod;
import com.lg.api.comm.HeaderHelper;
import com.lg.api.comm.HeaderWrapper;
import com.lg.api.comm.body.AuthTokenBody;

public class EasemobAuthToken extends EasemobRestAPI implements AuthTokenAPI{
	
	public static final String ROOT_URI = "/token";
	
	private static final Logger log = LoggerFactory.getLogger(EasemobAuthToken.class);
	
	@Override
	public String getResourceRootURI() {
		return ROOT_URI;
	}

	public Object getAuthToken(String clientId, String clientSecret) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
		BodyWrapper body = new AuthTokenBody(clientId, clientSecret);
		HeaderWrapper header = HeaderHelper.getDefaultHeader();
		
		return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
	}
}
