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

import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.Parser
import ca.uhn.hl7v2.validation.impl.DefaultValidation
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.component.hl7.HL7DataFormat
import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator
import org.openehealth.ipf.platform.camel.core.extend.CoreExtensionModule
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.platform.camel.hl7.HL7v2
import org.openehealth.ipf.platform.camel.hl7.adapter.AcknowledgementAdapter
import org.openehealth.ipf.platform.camel.hl7.expression.Hl7InputExpression
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

    /**
     * Validates a message using {@link HL7v2#validatingProcessor()}
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing
     *
     * @deprecated use {@link HL7v2#validatingProcessor()}
     */
    static ProcessorDefinition validateMessage(ProcessorDefinition self) {
        return self.process(HL7v2.validatingProcessor())
    }

    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     *
     * @deprecated use Camel HL7DataModel
     */
    static ProcessorDefinition ghl7(DataFormatClause self) {
        return ghl7(self, (HapiContext)null)
    }


    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset and parser
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     *
     * @deprecated use Camel HL7DataModel
     */
    static ProcessorDefinition ghl7(DataFormatClause self, Parser parser) {
        HL7DataFormat hl7DataFormat = new HL7DataFormat()
        if (parser) {
            hl7DataFormat.parser = parser
        }
        CoreExtensionModule.dataFormat(self, hl7DataFormat)
    }

    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset and parser
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     *
     * @deprecated use Camel HL7DataModel
     */
    static ProcessorDefinition ghl7(DataFormatClause self, HapiContext hapiContext) {
        HL7DataFormat hl7DataFormat = new HL7DataFormat()
        if (hapiContext) {
            hl7DataFormat.hapiContext = hapiContext
        }
        CoreExtensionModule.dataFormat(self, hl7DataFormat)
    }

    /**
     * Configures a validator with HL7 support, executing a default validation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7messagevalidation
     *
     * @deprecated use {@link #hl7(org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition)}
     */
    static ValidatorAdapterDefinition ghl7(ValidatorAdapterDefinition self) {
        self.setValidator(new HL7Validator())
        self.staticProfile(new DefaultValidation())
        return (ValidatorAdapterDefinition)self.input(new Hl7InputExpression())
    }

    /**
     * Configures a validator with HL7 support, executing a validation based on the {@link HapiContext}
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7messagevalidation
     *
     * @deprecated use {@link HL7v2#validatingProcessor()}
     */
    static ValidatorAdapterDefinition hl7(ValidatorAdapterDefinition self) {
        self.setValidator(new HL7Validator())
        self.profile { exchange ->
            Message msg = HL7v2.bodyMessage(exchange)
            msg.parser.hapiContext
        }
        return (ValidatorAdapterDefinition)self.input(new Hl7InputExpression())
    }

}
