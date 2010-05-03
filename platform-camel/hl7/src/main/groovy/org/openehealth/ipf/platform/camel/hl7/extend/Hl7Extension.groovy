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
package org.openehealth.ipf.platform.camel.hl7.extend;


import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.builder.DataFormatClause;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext;
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator;
import org.openehealth.ipf.platform.camel.core.extend.CoreExtension;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import org.openehealth.ipf.platform.camel.hl7.dataformat.Hl7DataFormat;
import org.openehealth.ipf.platform.camel.hl7.expression.Hl7InputExpression;

/**
 * HL7 DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @dsl
 * 
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
public class Hl7Extension {
     
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self) {
        return ghl7(self, null, null);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * via an HL7 parser
     * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, Parser parser) {
        return ghl7(self, parser, null);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset 
     * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, String charset) {
        return ghl7(self, null, charset);
    }
    
    /**
     * Defines marshaling between a standard HL7 message and a
     * <a href="http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling">MessageAdapter</a>
     * using the given charset and parser 
     * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7adapter%28un%29marshalling
     */
    public static ProcessorDefinition ghl7(DataFormatClause self, Parser parser, String charset) {
        Hl7DataFormat hl7DataFormat = new Hl7DataFormat();
        if (parser) {
            hl7DataFormat.parser = parser;
        }
        if (charset) {
            hl7DataFormat.charset = charset;
        }
        CoreExtension.dataFormat(self, hl7DataFormat);
    }

    /**
     * Configures a validator with HL7 support
     * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/HL7+processing#HL7processing-HL7messagevalidation
     */
    public static ValidatorAdapterDefinition ghl7(ValidatorAdapterDefinition self) { 
        self.setValidator(new HL7Validator());
        self.staticProfile(new DefaultValidationContext()); 
        return (ValidatorAdapterDefinition)self.input(new Hl7InputExpression());
    }
}
