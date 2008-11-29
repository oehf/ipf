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
package org.openehealth.ipf.platform.camel.core.process.builder;

import org.apache.camel.Processor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilder;
import org.openehealth.ipf.platform.camel.test.transformer.ConstantTransformer;
import org.openehealth.ipf.platform.camel.test.transformer.FailureTransformer;


/**
 * @author Martin Krasser
 */
@SuppressWarnings("unchecked")
public class EnricherRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        // -------------------------------------------------------------
        //  InOnly routes
        // -------------------------------------------------------------
        
        from("direct:enricher-test-1")
        .process(enricher("testAggregator", "direct:enricher-constant-resource"))
        .to("mock:mock");

        from("direct:enricher-test-2")
        .process(enricher(aggregation(), "direct:enricher-constant-resource"))
        .to("mock:mock");
        
        from("direct:enricher-test-3")
        .process(enricher("testAggregator", "direct:enricher-fault-resource"))
        .to("mock:mock");
        
        from("direct:enricher-test-4")
        .errorHandler(noErrorHandler()) // avoid re-deliveries
        .process(enricher("testAggregator", "direct:enricher-error-resource"))
        .to("mock:mock");
        
        // -------------------------------------------------------------
        //  InOut routes
        // -------------------------------------------------------------
        
        from("direct:enricher-test-5")
        .process(enricher("testAggregator", "direct:enricher-constant-resource"));

        from("direct:enricher-test-6")
        .process(enricher(aggregation(), "direct:enricher-constant-resource"));

        from("direct:enricher-test-7")
        .process(enricher("testAggregator", "direct:enricher-fault-resource"));
        
        from("direct:enricher-test-8")
        //.errorHandler(noErrorHandler()) // avoid re-deliveries
        .process(enricher("testAggregator", "direct:enricher-fault-resource"));
        
        // -------------------------------------------------------------
        //  Enricher resources
        // -------------------------------------------------------------
        
        from("direct:enricher-constant-resource").errorHandler(noErrorHandler()).process(constantResource());
        from("direct:enricher-fault-resource").errorHandler(noErrorHandler()).process(failureResource(false));
        from("direct:enricher-error-resource").errorHandler(noErrorHandler()).process(failureResource(true));
        
    }

    private AggregationStrategy aggregation() {
        // read new input data from in message (not from out message by default)
        return aggregationStrategy("testAggregator").aggregationInput(body());
    }
    
    private Processor constantResource() {
        return new ConstantTransformer("blah");
    }
    
    private Processor failureResource(boolean error) {
        return new FailureTransformer(error);
    }
    
}
