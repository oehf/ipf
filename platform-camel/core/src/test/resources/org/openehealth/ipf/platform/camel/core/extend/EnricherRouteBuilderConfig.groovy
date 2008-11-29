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
package org.openehealth.ipf.platform.camel.core.extend

import static org.apache.camel.builder.Builder.*

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
import org.openehealth.ipf.platform.camel.test.transform.min.TestAggregator

import org.apache.camel.builder.RouteBuilder

/**
 * @author Martin Krasser
 */
class EnricherRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
       
        builder.errorHandler(builder.noErrorHandler())
       
        def aggregationStrategy1 = builder.aggregationStrategy(new TestAggregator())
        def aggregationStrategy2 = builder.aggregationStrategy('sampleAggregator') 
        def aggregationStrategy3 = builder.aggregationStrategy {originalInBody, resourceOutBody -> 
            originalInBody + ':' + resourceOutBody
        } 
        def aggregationStrategy4 = builder.aggregationStrategy {originalInBody, resourceInBody -> 
            originalInBody + ':' + resourceInBody
        } 
        .input            {exchange -> exchange.in.body * 2}
        .aggregationInput {exchange -> exchange.out.body.substring(0, 2)}
        
        builder
            .from('direct:input1')
            .enrich('direct:resource', aggregationStrategy1)
            .to('mock:output')

        builder
            .from('direct:input2')
            .enrich('direct:resource', aggregationStrategy2)
            .to('mock:output')

        builder
            .from('direct:input3')
            .enrich('direct:resource', aggregationStrategy3)
            .to('mock:output')
            
        builder
            .from('direct:input4')
            .enrich('direct:resource', aggregationStrategy4)
            .to('mock:output')
            
        builder
            .from('direct:input5')
            .enrich('direct:resource') {originalExchange, resourceExchange ->
                originalExchange.in.body += ':' + resourceExchange.out.body 
                originalExchange // return value is optional (see direct:input6)
            }
            .to('mock:output')
            
        builder
            .from('direct:input6')
            .enrich('direct:resource') {originalExchange, resourceExchange ->
                originalExchange.in.body += ':' + resourceExchange.out.body 
            }
            .to('mock:output')
            
        builder
            .from('direct:resource')
            .transmogrify { it.reverse() }
    
    }
    
}
