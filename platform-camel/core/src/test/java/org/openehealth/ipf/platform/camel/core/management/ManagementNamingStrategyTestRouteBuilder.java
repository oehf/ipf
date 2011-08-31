package org.openehealth.ipf.platform.camel.core.management;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;

public class ManagementNamingStrategyTestRouteBuilder extends
		SpringRouteBuilder {

	@Override
	public void configure() throws Exception {

		from("direct:input")
			.routeId("namingStrategyRoute")
			.process(
				new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

					}
				})
			.id("namingStrategyProcessor")
			.end();
	}

}
