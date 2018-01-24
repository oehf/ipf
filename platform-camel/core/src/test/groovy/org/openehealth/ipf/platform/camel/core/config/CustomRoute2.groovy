package org.openehealth.ipf.platform.camel.core.config

class CustomRoute2 extends CustomRouteBuilder{
	
	void configure() {		
		interceptFrom('direct:input2')
		  .log('Intercepted Input2')
		  .to('direct:bridge')

		direct('bridge')
		  .to('mock:output')
	}
}
