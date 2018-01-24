package org.openehealth.ipf.platform.camel.core.config


class Route2 extends CustomRouteBuilder{
	
	void configure() {
	    direct('input2').log('Inside Input2').to('mock:output')    
	}
}
