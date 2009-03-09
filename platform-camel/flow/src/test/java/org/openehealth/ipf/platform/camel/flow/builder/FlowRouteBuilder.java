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
package org.openehealth.ipf.platform.camel.flow.builder;

import org.apache.camel.impl.SerializationDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * @author Martin Krasser
 */
public class FlowRouteBuilder extends BaseRouteBuilder {

    private DataFormat serialization = new SerializationDataFormat();
    
    @Override
    public void configure() throws Exception {
        
        // --------------------------------------------------------------
        //  Linear Flows
        // --------------------------------------------------------------
        
        from("direct:flow-test-1")
        .intercept(routeHelper.flowBegin("test-1")
                .application("test")
                .outType(String.class))
        .setHeader("foo", constant("test-1"))
        .to("mock:mock")
        .intercept(routeHelper.flowEnd());

        from("direct:flow-test-2")
        .intercept(routeHelper.flowBegin("test-2")
                .application("test")
                .inFormat(serialization)
                .outFormat(serialization))
        .setHeader("foo", constant("test-2"))
        .to("mock:mock")
        .intercept(routeHelper.flowEnd());

        from("direct:flow-test-3")
        .intercept(routeHelper.flowBegin("test-3")
                .application("test")
                .inFormat(serialization)
                .outConversion(false))
        .setHeader("foo", constant("test-3"))
        .to("mock:mock")
        .intercept(routeHelper.flowEnd());
    
        from("direct:flow-test-4")
        .intercept(routeHelper.flowBegin("test-4")
                .application("test")
                .outType(String.class))
        .setHeader("foo", constant("test-4"))
        .filter(routeHelper.dedupe())
        .to("mock:mock")
        .intercept(routeHelper.flowEnd());

        from("direct:flow-test-5")
        .errorHandler(noErrorHandler())
        .intercept(routeHelper.flowBegin("test-5")
                .application("test")
                .outType(String.class))
        .throwFault("unhandled fault")
        .to("mock:mock");

        from("direct:flow-test-6")
        .errorHandler(noErrorHandler())
        .intercept(routeHelper.flowBegin("test-6")
                .replayErrorHandler("mock:error")
                .application("test")
                .outType(String.class))
        .throwFault("handled fault")
        .to("mock:mock");

        // --------------------------------------------------------------
        //  Split Flows (original Camel splitter)
        // --------------------------------------------------------------
        
        from("direct:flow-test-split")
        .intercept(routeHelper.flowBegin("test-split")
                .application("test")
                .outType(String.class))
        .multicast()
        .to("direct:out-1")
        .to("direct:out-2");

        from("direct:out-1")
        .to("mock:mock-1")
        .intercept(routeHelper.flowEnd());

        from("direct:out-2")
        .to("mock:mock-2")
        .intercept(routeHelper.flowEnd());
        
        // --------------------------------------------------------------
        //  Pipe Flows
        // --------------------------------------------------------------
        
        from("direct:flow-test-pipe")
        .intercept(routeHelper.flowBegin("test-pipe")
                .application("test")
                .outType(String.class))
        .to("direct:out-1")
        .to("direct:out-2");

    }
}
