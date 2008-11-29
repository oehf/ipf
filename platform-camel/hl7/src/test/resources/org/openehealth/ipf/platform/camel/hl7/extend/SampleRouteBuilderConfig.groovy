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

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

/**
 * @author Martin Krasser
 */
class SampleRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        
        builder
            .from("direct:input1")
            // create a message adapter from an HL7 string
            .unmarshal().ghl7()
            // transmogrifiers are passed in-message bodies
            // and message headers by default.
            .transmogrify { msg, headers -> 
                // set the MSH[5] field to whatever is 
                // contained in the foo message header
                // (using the HAPI DSL)
                msg.MSH[5] = headers.foo
                msg
            }
            .choice()
                // when-closures are passed messages 
                // exchanges by default. Here we make
                // routing decisions based in the MSH[5]
                // field value of the HL7 message (using
                // the HAPI DSL)
                .when { it.in.body.MSH[5].value == 'blah' }
                    .marshal().ghl7() // adapter -> string
                    .to('mock:output1')
                .when { it.in.body.MSH[5].value == 'blub' }
                    .marshal().ghl7() // adapter -> string
                    .to('mock:output2')
    }
    
}