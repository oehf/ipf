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

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
import org.openehealth.ipf.platform.camel.test.processor.FailureProcessor

/**
 * @author Martin Krasser
 */
class ExpressionCLauseRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        
        // access exception message 
        builder.from("direct:input1")
            .onException(Exception.class).setHeader('foo').exceptionObject().to('mock:output').end()
            .process(new FailureProcessor('blah'))
        
        builder.from("direct:input2")
            .onException(Exception.class).transform().exceptionMessage().to('mock:output').end()
            .process(new FailureProcessor('blah'))
            
    }
    
}