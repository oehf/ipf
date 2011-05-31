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
package org.openehealth.ipf.platform.camel.cda.extend;

import java.util.Map;
import org.apache.camel.builder.DataFormatClause;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.modules.cda.CDAR2Constants;
import org.openehealth.ipf.platform.camel.cda.dataformat.CDADataFormat;
import org.openehealth.ipf.platform.camel.core.extend.CoreExtension;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

/**
 * CDA DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @dsl
 *
 * @author Jens Riemschneider
 */
public class CDAExtension {
     /**
      * Defines marshalling between a standard CDA document and a POCDMT000040ClinicalDocument object 
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ProcessorDefinition cdar2(DataFormatClause self) {
         CDADataFormat dataFormat = new CDADataFormat();
         return CoreExtension.dataFormat(self, dataFormat);
     }         
 
     /**
      * Parameterizes the xsd() validator to validate against a W3C CDA Schema 
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ValidatorAdapterDefinition cdar2(ValidatorAdapterDefinition self) { 
         return self.staticProfile(CDAR2Constants.CDAR2_SCHEMA);
     }

     /**
      * fill me
      * @param parameters
      * 		fill me 
      * @ipfdoc fill me
      */
     public static ValidatorAdapterDefinition ccd(ValidatorAdapterDefinition self, Map parameters) {
         return self.staticProfile(new SchematronProfile(CDAR2Constants.CCD_SCHEMATRON_RULES, parameters));
     }
     
     /**
      * fill me 
      * @ipfdoc fill me
      */
     public static ValidatorAdapterDefinition ccd(ValidatorAdapterDefinition self) { 
         return self.staticProfile(new SchematronProfile(CDAR2Constants.CCD_SCHEMATRON_RULES));
     }
}
