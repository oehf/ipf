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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.extend;

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Validator

/**
 * HL7 v3 DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class Hl7v3Extension {
     /**
      * Validates an ITI-44 request
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti44Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 44, true);
     }
     
     /**
      * Validates an ITI-44 response
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti442Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 44, false);
     }
     
     /**
      * Validates an ITI-45 request
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti45Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 45, true);
     }
     
     /**
      * Validates an ITI-45 response
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti452Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 45, false);
     }
     
     /**
      * Validates an ITI-46 request
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti46Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 46, true);
     }
     
     /**
      * Validates an ITI-46 response
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti462Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 46, false);
     }
     
     /**
      * Validates an ITI-47 request
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti47Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 47, true);
     }
     
     /**
      * Validates an ITI-47 response
      * @ipfdoc IHE Support#validation hl7v3
      * @dsl platform-camel-ihe-hl7v3ws
      */
     public static ValidatorAdapterDefinition iti472Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 47, false);
     }
     
     
     
     private static final Map REQUEST_TYPES = [
          44 : ['PRPA_IN201301UV02', 'PRPA_IN201302UV02', 'PRPA_IN201304UV02'],
          45 : ['PRPA_IN201309UV02'],
          46 : ['PRPA_IN201302UV02'],
          47 : ['PRPA_IN201305UV02', 'QUQI_IN000003UV01', 'QUQI_IN000003UV01_Cancel'],
     ];
     
     private static final Map RESPONSE_TYPES = [
          44 : ['MCCI_IN000002UV01'],
          45 : ['PRPA_IN201310UV02'],
          46 : ['MCCI_IN000002UV01'],
          47 : ['PRPA_IN201306UV02', 'MCCI_IN000002UV01'],
     ];

     
     private static ValidatorAdapterDefinition validationLogic(
             ValidatorAdapterDefinition self, 
             int transaction,
             boolean request) 
     {
         self.setValidator(new Hl7v3Validator());
         self.staticProfile(request ? REQUEST_TYPES[transaction] : RESPONSE_TYPES[transaction]);
         return self.input {
             it.in.getBody(String.class)
         };
     }
}