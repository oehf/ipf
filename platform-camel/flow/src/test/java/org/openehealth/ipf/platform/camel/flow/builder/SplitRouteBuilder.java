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

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * @author Jens Riemschneider
 */
public class SplitRouteBuilder extends BaseRouteBuilder {

    @Override
    public void configure() throws Exception {
        
        // --------------------------------------------------------------
        //  Split Flows (IPF splitter)
        // --------------------------------------------------------------
        
        from("direct:split-test-ipfsplit")
        .intercept(routeHelper.flowBegin("test-ipfsplit")
                .application("test")
                .outType(String.class))
        .intercept(routeHelper.split(new TestSplitRule()))
        .intercept(routeHelper.flowEnd())
        .to("mock:mock-1");
        

        from("direct:split-test-ipfsplit-agg")
        .to("direct:out-3")
        .to("direct:out-4");
        
        from("direct:out-3")
        .intercept(routeHelper.split(new TestSplitRule()))
        .to("mock:mock-1");
        
        from("direct:out-4")
        .to("mock:mock-2");
    }

    private static final class TestSplitRule implements Expression {
        @Override
        public Object evaluate(Exchange exchange) {
            String body = (String)exchange.getIn().getBody();
            String[] results = body.split(",");
            return Arrays.asList(results);
        }
    }
}
