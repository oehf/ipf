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
import org.openehealth.ipf.platform.camel.test.transform.min.TestPredicate

import org.apache.camel.builder.RouteBuilder

/**
 * @author Martin Krasser
 */
class FilterRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
       
        builder.errorHandler(builder.noErrorHandler())
       
        def predicate1 = builder.predicate(new TestPredicate('blub'))
        def predicate2 = builder.predicate('samplePredicate') 
        def predicate3 = builder.predicate { body -> body == 'blub'} 
        
        builder
            .from('direct:input1')
            .filter(predicate1)
            .to('mock:output')

        builder
            .from('direct:input2')
            .filter(predicate2)
            .to('mock:output')

        builder
            .from('direct:input3')
            .filter(predicate3)
            .to('mock:output')
            
        builder
            .from('direct:input4')
            .filter {exchange -> exchange.in.body == 'blub'}
            .to('mock:output')
            
    }
    
}
