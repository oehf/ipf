package org.openehealth.ipf.platform.camel.core.config

import org.apache.camel.builder.RouteBuilder

class Route extends RouteBuilder {
	
	void configure() {
	    from('direct:input')
				.log('Inside Input')
				.to('mock:output')
	}
}
