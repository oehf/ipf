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
package org.openehealth.ipf.platform.camel.hl7.extend

import ca.uhn.hl7v2.parser.GenericParser

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

/**
 * @author Martin Krasser
 */
class Ghl7RouteBuilderConfig implements RouteBuilderConfig {
    
    def parser = new GenericParser()
     
    void apply(RouteBuilder builder) {
        
        builder.from("direct:input1")
            .marshal().ghl7()
            .to('mock:output')
        
        builder.from("direct:input2")
            .marshal().ghl7('UTF-8')
            .to('mock:output')
        
        builder.from("direct:input3")
            .unmarshal().ghl7(parser, 'UTF-8')
            .to('mock:output')
            
        builder.from("direct:input4")
            .unmarshal().ghl7()
            .validate().ghl7()
            .to('mock:output')

    }
    
}