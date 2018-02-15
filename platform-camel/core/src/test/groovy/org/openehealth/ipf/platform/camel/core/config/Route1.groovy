package org.openehealth.ipf.platform.camel.core.config


class Route1 extends CustomRouteBuilder{
	
	void configure() {		
		from('direct:input1').log('Inside Input1').to('mock:output')    
	}
}
