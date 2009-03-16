/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.lbs.http.builder;

import static org.openehealth.ipf.platform.camel.lbs.core.builder.RouteHelper.fetch;
import static org.openehealth.ipf.platform.camel.lbs.core.builder.RouteHelper.store;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler;

/**
 * @author Jens Riemschneider
 */
public class LbsHttpRouteBuilderJava extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        List<ResourceHandler> handlers = bean(List.class, "resourceHandlers");

        errorHandler(noErrorHandler());
        
        from("jetty:http://localhost:9452/lbstest_no_extract")
            .to("mock:mock");
        
        from("jetty:http://localhost:9452/lbstest_extract")
            .intercept(store().with(handlers))
            .to("mock:mock");
        
        from("jetty:http://localhost:9452/lbstest_ping")
            .intercept(store().with(handlers))
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    ResourceDataSource dataSource = 
                        exchange.getIn().getBody(ResourceDataSource.class);
                    exchange.getOut().setBody(dataSource);
                }
            })
            .to("mock:mock");
        
        // Note: This is not available via java, only groovy. Simply make the test work
        from("jetty:http://localhost:9452/lbstest_extract_factory_via_bean")
            .intercept(store().with(handlers))
            .to("mock:mock");
        
        from("jetty:http://localhost:9452/lbstest_extract_router")
            .intercept(store().with(handlers))
            .setHeader("tag", constant("I was here"))
            .intercept(fetch().with(handlers))
            .to("http://localhost:9452/lbstest_receiver");      

        from("direct:lbstest_send_only")
            .intercept(fetch().with(handlers))
            .to("http://localhost:9452/lbstest_receiver");
        
        
        from("direct:lbstest_non_http")
            .intercept(store().with(handlers))
            .to("mock:mock");

        from("jetty:http://localhost:9452/lbstest_receiver")
            .intercept(store().with(handlers))
            .to("mock:mock");
    }
}
