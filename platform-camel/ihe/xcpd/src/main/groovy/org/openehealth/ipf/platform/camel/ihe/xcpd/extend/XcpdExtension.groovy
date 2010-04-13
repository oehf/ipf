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
package org.openehealth.ipf.platform.camel.ihe.xcpd.extend;

import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Validator;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * XCPD DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} 
 * using the {@code use} keyword.
 * @author Dmytro Rud
 */
class XcpdExtension {
     
     /**
      * Validates an ITI-55 request
      * @ipfdoc IHE Support#validation xcpd
      * @dsl platform-camel-ihe-xcpd
      */
     public static ValidatorAdapterDefinition iti55Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 55, true);
     }
     
     /**
      * Validates an ITI-55 response
      * @ipfdoc IHE Support#validation xcpd
      * @dsl platform-camel-ihe-xcpd
      */
     public static ValidatorAdapterDefinition iti55Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 55, false);
     }

     /**
      * Validates an ITI-56 request
      * @ipfdoc IHE Support#validation xcpd
      * @dsl platform-camel-ihe-xcpd
      */
     public static ValidatorAdapterDefinition iti56Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 56, true);
     }
     
     /**
      * Validates an ITI-56 response
      * @ipfdoc IHE Support#validation xcpd
      * @dsl platform-camel-ihe-xcpd
      */
     public static ValidatorAdapterDefinition iti56Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 56, false);
     }

     
     private static ValidatorAdapterDefinition validationLogic(
             ValidatorAdapterDefinition self, 
             int transaction,
             boolean request) 
     {
         self.setValidator(new Hl7v3Validator());
         self.staticProfile(request ? 
                 Hl7v3ValidationProfiles.REQUEST_TYPES[transaction] : 
                 Hl7v3ValidationProfiles.RESPONSE_TYPES[transaction]);
         return (ValidatorAdapterDefinition)self.input {
             it.in.getBody(String.class)
         };
     }

}