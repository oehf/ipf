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
package org.openehealth.ipf.platform.camel.cda.extend

import org.openehealth.ipf.commons.xml.SchematronProfile
import org.openehealth.ipf.modules.cda.CDAR2Constants
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition

/**
 * CDA DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @DSL
 * @author Jens Riemschneider
 */
public class CDAExtensionModule {

 
     /**
      * Parameterizes the xsd() validator to validate against a W3C CDA Schema 
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ValidatorAdapterDefinition cdar2(ValidatorAdapterDefinition self) { 
         return self.staticProfile(CDAR2Constants.CDAR2_SCHEMA)
     }

    /**
     * Parameterizes the xsd() validator to validate against a W3C CCDA Schema
     * (CDA schema with SDTC extensions)
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition ccda_schema(ValidatorAdapterDefinition self) {
        return self.staticProfile(CDAR2Constants.CCDA_SCHEMA)
    }

    /**
     * Parameterizes the xsd() validator to validate against a W3C HITSPC32 v2.5 Schema
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition hitspc32_schema(ValidatorAdapterDefinition self) {
        return self.staticProfile(CDAR2Constants.HITSP_32_2_5_SCHEMA)
    }

     /**
      * Parameterizes the schematron() validator to validate against the CCD 1.0
      * schematron rules.
      * @param parameters schematron parameters
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ValidatorAdapterDefinition ccd(ValidatorAdapterDefinition self, Map parameters) {
         return self.staticProfile(new SchematronProfile(CDAR2Constants.CCD_SCHEMATRON_RULES, parameters))
     }
     
     /**
      * Parameterizes the schematron() validator to validate against the CCD 1.0
      * schematron rules.
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
      */
     public static ValidatorAdapterDefinition ccd(ValidatorAdapterDefinition self) { 
         return self.staticProfile(new SchematronProfile(CDAR2Constants.CCD_SCHEMATRON_RULES))
     }

    /**
     * Parameterizes the schematron() validator to validate against the CCD 1.0
     * schematron rules.
     * @param parameters schematron parameters
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition hitspc32(ValidatorAdapterDefinition self, Map parameters) {
        return self.staticProfile(new SchematronProfile(CDAR2Constants.HITSP_32_2_5_SCHEMATRON_RULES, parameters))
    }

    /**
     * Parameterizes the schematron() validator to validate against the CCD 1.0
     * schematron rules.
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition hitspc32(ValidatorAdapterDefinition self) {
        return self.staticProfile(new SchematronProfile(CDAR2Constants.HITSP_32_2_5_SCHEMATRON_RULES))
    }

    /**
     * Parameterizes the schematron() validator to validate against the CCDA
     * schematron rules.
     * @param parameters schematron parameters
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition ccda(ValidatorAdapterDefinition self, Map parameters) {
        return self.staticProfile(new SchematronProfile(CDAR2Constants.CCDA_SCHEMATRON_RULES, parameters))
    }

    /**
     * Parameterizes the schematron() validator to validate against the CCD 1.0
     * schematron rules.
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/CDA+DSL+Extensions
     */
    public static ValidatorAdapterDefinition ccda(ValidatorAdapterDefinition self) {
        return self.staticProfile(new SchematronProfile(CDAR2Constants.CCDA_SCHEMATRON_RULES))
    }
}
