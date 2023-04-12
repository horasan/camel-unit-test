package com.horasan.routes;

import org.apache.camel.builder.RouteBuilder;

public abstract class CamelBaseConfiguration extends RouteBuilder {
	
	@Override
	public void configure() throws Exception{
		commonCamelConfiguration();
		specificCamelConfigurationForThisRoute();
	}
	
	public abstract void specificCamelConfigurationForThisRoute() throws Exception;
	
	public void commonCamelConfiguration() throws Exception {
		
	}

}
