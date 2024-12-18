package org.openehealth.ipf.tutorials.config.base.route;

import org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder;

public class MockInterceptor extends CustomRouteBuilder {

    @Override
    public void configure() {
        interceptSendToEndpoint("direct:file-save").to("mock:file").stop();        
    }

}
