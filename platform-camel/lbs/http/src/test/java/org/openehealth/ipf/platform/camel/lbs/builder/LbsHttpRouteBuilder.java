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
package org.openehealth.ipf.platform.camel.lbs.builder;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;
import org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler;

/**
 * @author Jens Riemschneider
 */
public class LbsHttpRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        List<AttachmentHandler> handlers = bean(List.class, "attachmentHandlers");

        errorHandler(deadLetterChannel().maximumRedeliveries(2).initialRedeliveryDelay(0));
        
        from("jetty:http://localhost:8080/lbstest_no_extract")
            .to("mock:mock");
        
        from("jetty:http://localhost:8080/lbstest_extract")
            .intercept(store().with(handlers))
            .to("mock:mock");
        
        from("jetty:http://localhost:8080/lbstest_ping")
            .intercept(store().with(handlers))
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    AttachmentDataSource dataSource = 
                        exchange.getIn().getBody(AttachmentDataSource.class);
                    exchange.getOut().setBody(dataSource);
                }
            })
            .to("mock:mock");
        
        // Note: This is not available via java, only groovy. Simply make the test work
        from("jetty:http://localhost:8080/lbstest_extract_factory_via_bean")
            .intercept(store().with(handlers))
            .to("mock:mock");
        
        from("jetty:http://localhost:8080/lbstest_extract_router")
            .intercept(store().with(handlers))
            .setHeader("tag", constant("I was here"))
            .intercept(fetch().with(handlers))
            .to("http://localhost:8080/lbstest_receiver");      

        from("direct:lbstest_send_only")
            .intercept(fetch().with(handlers))
            .to("http://localhost:8080/lbstest_receiver");
        
        
        from("direct:lbstest_non_http")
            .intercept(store().with(handlers))
            .to("mock:mock");

        from("jetty:http://localhost:8080/lbstest_receiver")
            .intercept(store().with(handlers))
            .to("mock:mock");
    }
}
