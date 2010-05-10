package org.openehealth.ipf.platform.camel.core.config;


class CustomRoute1 extends CustomRouteBuilder{
	
	void configure() {		
		interceptFrom('direct:input1').log('Intercepted Input1').to('mock:output')
		
        intercept().log('Intercepting all')
	}
}
