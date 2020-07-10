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


import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.platform.camel.hl7.HL7v2
import org.openehealth.ipf.platform.camel.hl7.adapter.AcknowledgementAdapter
import org.openehealth.ipf.platform.camel.hl7.model.HapiAdapterDefinition

/**
 * HL7 DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 * Note: this extension will become obsolete as soon as there is no difference anymore between MessageAdapter and Message
 *
 * @DSL
 * 
 * @author Martin Krasser
 * @author Jens Riemschneider
 * @author Christian Ohr
 */
class Hl7ExtensionModule {

    /**
     * Returns acknowledgement message
     * FIXME cannot be used in local onException clauses!
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing
     *
     * @deprecated use {@link HL7v2#ack()}
     */
    static HapiAdapterDefinition ack(ProcessorDefinition self) {
        HapiAdapterDefinition answer = new HapiAdapterDefinition(new AcknowledgementAdapter())
        self.addOutput(answer)
        return answer
    }

}
