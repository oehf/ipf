/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.builder

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import ca.uhn.hl7v2.validation.ValidationContext

/**
 * A set of rules that check all primitive type constraints defined in
 * the HL7v2 specifications.
 * 
 * @author Christian Ohr
 */
public class DefaultTypeRulesBuilder extends ValidationContextBuilder{

    public RuleBuilder forContext(ValidationContext context) {
        new RuleBuilder(context)
        .forVersion().before('2.3')
            .type('ST').omitLeadingWhitespace()
            .type('ID').omitLeadingWhitespace()
            .type('TX').omitTrailingWhitespace()
            .type('DT').matches(/(\d{4}[01]\d(\d{2})?/)
            .type('TM').matches(/([012]\d[0-5]\d([0-5]\d(\.\d{4})?)([+-]\d{4})?)/)
            .type('TSComponentOne').matches(/(\d{4}[01]\d\d{2}([012]\d[0-5]\d([0-5]\d(\.\d{4}?)?)?)([+-]\d{4})?)?/)
        .forVersion().asOf('2.3')
            .type('ST')[1..199].omitLeadingWhitespace()
            .type('ID')[1..199].omitLeadingWhitespace()
            .type('IS')[1..199].omitLeadingWhitespace()
            .type('TX')[1..65535].omitTrailingWhitespace()
            .type('DT').matches(/(\d{4}([01]\d(\d{2})?)?)?/)
            .type('TM').matches(/([012]\d([0-5]\d([0-5]\d(\.\d(\d(\d(\d)?)?)?)?)?)?)?([+-]\d{4})?/)
            .type('TSComponentOne').matches(/(\d{4}([01]\d(\d{2}([012]\d([0-5]\d([0-5]\d(\.\d(\d(\d(\d)?)?)?)?)?)?)?)?)?([+-]\d{4})?)?/)
        .forVersion().asOf('2.5')
            .type('DTM').matches(/(\d{4}([01]\d(\d{2}([012]\d([0-5]\d([0-5]\d(\.\d(\d(\d(\d)?)?)?)?)?)?)?)?)?([+-]\d{4})?)?/)
        .forAllVersions()
            .type('FT')[1..65535]
            .type('NM').isNumber()
            .type('SI').matches(/\d*/)
    }
    
}
