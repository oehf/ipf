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
package org.openehealth.ipf.platform.camel.core.camel.exception;

import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.support.processor.FailureProcessor;


/**
 * @author Martin Krasser
 */
public class ErrorHandlingRouteBuilder extends SpringRouteBuilder {

    private FailureProcessor failure = new FailureProcessor("blah");
    
    @Override
    public void configure() throws Exception {

        // global error handler
        errorHandler(noErrorHandler());
        
        from("direct:input-1")
        .to("mock:inter")   // inherits global error handler (step in pipeline)
        .process(failure)   // inherits global error handler (step in pipeline)
        .to("mock:output"); // inherits global error handler (step in pipeline)
        
        from("direct:input-2")
        // defines local error handler
        .errorHandler(deadLetterChannel("mock:error").maximumRedeliveries(2))
        
        // ------------------------------------------------------------------
        //  IMPORTANT: We have to multicast() here, otherwise we have a 
        //             pipeline where each step has its own error handler
        //             (dead letter channel) and mock:inter won't receive
        //             messages on redelivery
        // ------------------------------------------------------------------
        
        .multicast()
        .to("mock:inter")   // no error handler here (installed before multicast)
        .to("direct:temp"); // no error handler here (installed before multicast)
        
        from("direct:temp") 
        .process(failure)    // inherits global error handler (step in pipeline)
        .to("mock:output");  // inherits global error handler (step in pipeline)
    }
    
}
