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
package org.openehealth.ipf.tutorials.hl7.route

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.hl7.HL7DataFormat

import java.util.function.Function

import static org.apache.camel.component.hl7.HL7.messageConforms

/**
 * @author Christian Ohr
 */
class SampleRouteBuilder extends RouteBuilder {

    void configure() {

        // Set up HL7 context with the custom validation rules
        HapiContext context = new DefaultHapiContext(getContext().getRegistry().lookupByName('myCustomRules'))
        context.getParserConfiguration().setValidating(false)
        HL7DataFormat hl7 = new HL7DataFormat()
        hl7.setHapiContext(context)

        from('file:target/input')
                .convertBodyTo(String)
                .to('direct:input')

        from('direct:input')
                .unmarshal(hl7)
                .validate(messageConforms())  // validate that the message conforms to the rules defined in the context
                .transmogrify { msg ->
                    msg.PV1[3][2] = '' // clear room nr.
                    msg.PV1[3][3] = '' // clear bed nr.
                    msg.PID[7][1] = msg.PID[7][1].value.substring(0, 8) // format birth date
                    msg.PID[8] = msg.PID[8].mapGender()                 // map gender
                    msg
                }
                .setHeader(Exchange.FILE_NAME).exchange({exchange ->
                        exchange.in.body.MSH[4].value + '.hl7'
                } as Function)
                .convertBodyTo(String)
                .to('file:target/output')

    }

}
