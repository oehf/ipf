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
package org.openehealth.ipf.modules.hl7.validation

import ca.uhn.hl7v2.validation.ValidationContext

/**
 * A factory that constructs default instances of {@link org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext}.
 * To use the constant in a Spring context, use the <util:constant> tag:
 * <pre>
 *   ...
 *   <property name="validationContext">
 *       <util:constant static-field="org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext.DEFAULT_TYPE_RULES"/>
 *    </property>
 *   ...
 * </pre>
 * Note that {@link org.openehealth.ipf.modules.hl7.parser.PipeParser} 
 * uses {link #DEFAULT_TYPE_RULES} as its default {@link ValidationContext} without 
 * further configuration.
 * 
 * @author Christian Ohr
 */
public class ValidationContextFactory{
     
     /**
      * @return a set of rules that verify the restrictions of primitive types
      */
     static final ValidationContext DEFAULT_TYPE_RULES =
         new DefaultValidationContext().builder()
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
         .context
         
}
