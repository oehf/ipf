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
package org.openehealth.ipf.platform.camel.flow.extend

import static org.apache.camel.builder.Builder.*

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.SerializationDataFormat

import java.util.Arrays

/**
 * @author Martin Krasser
 */
class RenderRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
    
        def dlc = builder.deadLetterChannel('direct:err').maximumRedeliveries(0)
        
        // --------------------------
        //  Default route
        // --------------------------
                
        builder
            .from('direct:render-test')
            .errorHandler(dlc)
            .initFlow('test-1')
                .renderer('initRenderer')
                .application("test")
                .outType(String.class)
            .validate { body ->
                if (body == 'error') {
                    throw new Exception('message rejected')
                }
            }
            .to('mock:out')
            .ackFlow().renderer('ackRenderer')
        
        // --------------------------
        //  Error route
        // --------------------------
        
        builder
            .from('direct:err')
            .nakFlow().renderer('nakRenderer')
            .to('mock:err')

    }

}