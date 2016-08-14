package com.lg.resources;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class APIApplication extends ResourceConfig {
	
	public APIApplication() {
		// 加载Resource
		register(UserResource.class);
		register(LoggingFeature.class);
	}
}
