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

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.ErrorCode
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser
import ca.uhn.hl7v2.validation.impl.DefaultValidation
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.component.hl7.HL7
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.model.RouteDefinition
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator
import org.openehealth.ipf.platform.camel.core.extend.CoreExtensionModule
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.platform.camel.hl7.HL7v2
import org.openehealth.ipf.platform.camel.hl7.adapter.AcknowledgementAdapter
import org.openehealth.ipf.platform.camel.hl7.dataformat.Hl7DataFormat
import org.openehealth.ipf.platform.camel.hl7.expression.Hl7InputExpression
import org.openehealth.ipf.platform.camel.hl7.model.HapiAdapterDefinition
import org.openehealth.ipf.platform.camel.hl7.validation.ConformanceProfileValidators


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
public class Hl7ExtensionModule {

    /**
     * Returns acknowledgement message
     * FIXME cannot be used in local onException clauses!
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing
     */
    public static HapiAdapterDefinition ack(ProcessorDefinition self) {
        HapiAdapterDefinition answer = new HapiAdapterDefinition(new AcknowledgementAdapter());
        self.addOutput(answer);
        return answer;
    }

    /**
     * Validates a message using {@link ConformanceProfileValidators#validatingProcessor()}
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing
     */
    public static ProcessorDefinition validateMessage(ProcessorDefinition self) {
        return self.process(HL7v2.validatingProcessor());
    }

    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self) {
        return ghl7(self, null, null);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * via an HL7 parser
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, Parser parser) {
        return ghl7(self, parser, null);
    }

    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * via an parser provided by the specified {@link HapiContext}.
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, HapiContext context) {
        return ghl7(self, context, null);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, String charset) {
        return ghl7(self, (HapiContext)null, charset);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset and parser 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, HapiContext context, String charset) {
        Hl7DataFormat hl7DataFormat = new Hl7DataFormat();
        if (context) {
            hl7DataFormat.context = context;
        }
        if (charset) {
            hl7DataFormat.charset = charset;
        }
        CoreExtensionModule.dataFormat(self, hl7DataFormat);
    }

    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset and parser
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, Parser parser, String charset) {
        Hl7DataFormat hl7DataFormat = new Hl7DataFormat();
        if (parser) {
            hl7DataFormat.parser = parser;
        }
        if (charset) {
            hl7DataFormat.charset = charset;
        }
        CoreExtensionModule.dataFormat(self, hl7DataFormat);
    }

    /**
     * Configures a validator with HL7 support
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7messagevalidation
     */
    public static ValidatorAdapterDefinition ghl7(ValidatorAdapterDefinition self) { 
        self.setValidator(new HL7Validator());
        self.staticProfile(new DefaultValidation());
        return (ValidatorAdapterDefinition)self.input(new Hl7InputExpression());
    }

}
