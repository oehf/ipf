/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.hl7.extend

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.ErrorCode
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.GenericParser
import org.apache.camel.component.hl7.HL7DataFormat
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.hl7.HL7v2

/**
 * @author Martin Krasser
 */
class AcknowledgementRouteBuilder extends SpringRouteBuilder {

    HL7DataFormat hl7 = new HL7DataFormat()
     
    void configure() {

        onException(Exception.class)
                .continued(true)
                .transform(HL7v2.ack(AcknowledgmentCode.AR, "Don't like it", ErrorCode.UNSUPPORTED_VERSION_ID))

        from("direct:input2")
            .unmarshal(hl7)
            .transform(HL7v2.ack())
            .to('mock:output')

        from("direct:input3")
                .unmarshal(hl7)
                .process { throw new Exception("Argh!") }
                .to('mock:output')

        from("direct:input4")
                .unmarshal(hl7)
                .transform(HL7v2.ack(AcknowledgmentCode.AR))
                .to('mock:output')
    }
    
}