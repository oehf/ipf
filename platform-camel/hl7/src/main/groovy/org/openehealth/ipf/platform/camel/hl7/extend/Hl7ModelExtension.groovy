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

import ca.uhn.hl7v2.parser.Parser

import org.apache.camel.builder.DataFormatClause

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterType
import org.openehealth.ipf.platform.camel.hl7.dataformat.Hl7DataFormat
import org.openehealth.ipf.platform.camel.hl7.expression.Hl7InputExpression

/**
 * @author Martin Krasser
 */
class Hl7ModelExtension {
     
    static extensions = { 

        // ----------------------------------------------------------------
        //  Adapter Extensions for DataFormatClause
        // ----------------------------------------------------------------
        
        DataFormatClause.metaClass.ghl7 = { ->
            delegate.ghl7(null, null)
        }
        
        DataFormatClause.metaClass.ghl7 = { Parser parser ->
            delegate.ghl7(parser, null)
        }
        
        DataFormatClause.metaClass.ghl7 = { String charset ->
            delegate.ghl7(null, charset)
        }
        
        DataFormatClause.metaClass.ghl7 = { Parser parser, String charset ->
            Hl7DataFormat hl7DataFormat = new Hl7DataFormat()
            if (parser) {
                hl7DataFormat.parser = parser
            }
            if (charset) {
                hl7DataFormat.charset = charset
            }
            delegate.dataFormat(hl7DataFormat)
        }
    
        // ----------------------------------------------------------------
        //  IPF model class extensions
        // ----------------------------------------------------------------
        
        ValidatorAdapterType.metaClass.ghl7 = {-> 
            delegate.setValidator(new HL7Validator())
            delegate.staticProfile(new DefaultValidationContext()) 
            delegate.input(new Hl7InputExpression())
        }
        
    }
        
}