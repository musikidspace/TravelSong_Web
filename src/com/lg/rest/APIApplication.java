package com.lg.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class APIApplication extends ResourceConfig {
	
	public APIApplication() {
		// 加载Resource
		register(UserResource.class);
		register(LoggingFilter.class);
	}
}
