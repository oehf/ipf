/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.cda.extend

import org.apache.camel.builder.DataFormatClause
import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openehealth.ipf.modules.cda.CDAR2Validator
import org.openehealth.ipf.platform.camel.cda.dataformat.MdhtDataFormat
import org.openehealth.ipf.platform.camel.core.extend.CoreExtension
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openhealthtools.mdht.uml.cda.ClinicalDocument

import static org.apache.camel.builder.Builder.bodyAs

/**
 * CDA DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @DSL
 * @author Christian Ohr
 */
public class MdhtExtension {
     /**
      * Defines marshalling between a standard CDA document and a ClinicalDocument object 
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ProcessorDefinition mdht(DataFormatClause self) {
         MdhtDataFormat dataFormat = new MdhtDataFormat();
         return CoreExtension.dataFormat(self, dataFormat);
     }         
 
     /**
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ValidatorAdapterDefinition mdht(ValidatorAdapterDefinition self) {
         self.validator = new CDAR2Validator();
         return (ValidatorAdapterDefinition)self.input(bodyAs(ClinicalDocument.class));
     }

}
