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
package org.openehealth.ipf.platform.camel.test.transaction;

import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.test.processor.FailureProcessor;


/**
 * @author Martin Krasser
 */
public class TransactionalMessagingRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {

        from("amq:queue:transactional-messaging-input")
        .onException(Exception.class).to("mock:transactional-messaging-error").end()
        .multicast()
        .to("direct:intern-1")
        .to("direct:intern-2");
        
        from("direct:intern-1")
        .to("amq:queue:transactional-messaging-output-1");
        from("direct:intern-2")
        // the noErrorHandler() can be omitted here if an explicit
        // transactionErrorHandler(...) is configured for endpoint
        // amq:queue:transactional-messaging-input
        .errorHandler(noErrorHandler())
        .process(new FailureProcessor("blah"))
        .to("amq:queue:transactional-messaging-output-2"); 

        // blah-messages rolled back at both endpoints
        // other messages are committed at both endpoints
        
        from("amq:queue:transactional-messaging-output-1")
        .to("mock:transactional-messaging-mock");
        from("amq:queue:transactional-messaging-output-2")
        .to("mock:transactional-messaging-mock");
    
    }

}
