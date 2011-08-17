/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.extend;

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.*;
import static org.openehealth.ipf.platform.camel.core.process.ProcessorBasedExchangeValidator.definition;

/**
 * XCPD DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} 
 * using the {@code use} keyword.
 *
 * @DSL
 *
 * @author Dmytro Rud
 */
class XcpdExtension {
     
     /**
      * Validates an ITI-55 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-XCPD
      */
     public static ValidatorAdapterDefinition iti55Request(ValidatorAdapterDefinition self) {
         return definition(self, iti55RequestValidator());
     }

     /**
      * Validates an ITI-55 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-XCPD
      */
     public static ValidatorAdapterDefinition iti55Response(ValidatorAdapterDefinition self) {
         return definition(self, iti55ResponseValidator());
     }

     /**
      * Validates an ITI-56 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-XCPD
      */
     public static ValidatorAdapterDefinition iti56Request(ValidatorAdapterDefinition self) {
         return definition(self, iti56RequestValidator());
     }

     /**
      * Validates an ITI-56 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-XCPD
      */
     public static ValidatorAdapterDefinition iti56Response(ValidatorAdapterDefinition self) {
         return definition(self, iti56ResponseValidator());
     }

}