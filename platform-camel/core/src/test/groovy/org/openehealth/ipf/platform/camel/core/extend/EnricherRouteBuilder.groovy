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

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.support.transform.min.TestAggregator

/**
 * @author Martin Krasser
 */
class EnricherRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
       
        errorHandler(noErrorHandler())

        def aggregationStrategy1 = aggregationStrategy(new TestAggregator())
        def aggregationStrategy2 = aggregationStrategy('sampleAggregator')
        def aggregationStrategy3 = aggregationStrategy {originalInBody, resourceOutBody -> 
            originalInBody + ':' + resourceOutBody
        } 
        def aggregationStrategy4 = aggregationStrategy {originalInBody, resourceInBody -> 
            originalInBody + ':' + resourceInBody
        } 
        .aggregationInput {exchange -> exchange.in.body.substring(0, 2)}
        .input            {exchange -> exchange.in.body * 2}

        from('direct:input1')
            .enrich('direct:resource', aggregationStrategy1)
            .to('mock:output')

        from('direct:input2')
            .enrich('direct:resource', aggregationStrategy2)
            .to('mock:output')

        from('direct:input3')
            .enrich('direct:resource', aggregationStrategy3)
            .to('mock:output')
            
        from('direct:input4')
            .enrich('direct:resource', aggregationStrategy4)
            .to('mock:output')
            
        from('direct:input5')
            .enrich('direct:resource') {originalExchange, resourceExchange ->
                originalExchange.in.body += ':' + resourceExchange.in.body 
                originalExchange // return value is optional (see direct:input6)
            }
            .to('mock:output')
            
        from('direct:input6')
            .enrich('direct:resource') {originalExchange, resourceExchange ->
                originalExchange.in.body += ':' + resourceExchange.in.body 
            }
            .to('mock:output')
            
        from('direct:resource')
            .transmogrify { it.reverse() }
    
    }
    
}
