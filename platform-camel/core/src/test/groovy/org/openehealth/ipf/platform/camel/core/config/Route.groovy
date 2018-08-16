package org.openehealth.ipf.platform.camel.core.config

import org.apache.camel.spring.SpringRouteBuilder

class Route extends SpringRouteBuilder {
	
	void configure() {
	    from('direct:input').log('Inside Input').to('mock:output')
	}
}
