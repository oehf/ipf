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
package org.openehealth.ipf.platform.camel.ihe.mllp.extend;

import org.apache.camel.Exchange;

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

import org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.MessageAdapterValidator;


/**
 * MLLP DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 * @author Jens Riemschneider
 */
public class MllpExtension {
     /**
      * Validates an ITI-8 request
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti8Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 8);
     }
     
     /**
      * Validates an ITI-8 response
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti8Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 8);
     }
     
     /**
      * Validates an ITI-9 request
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti9Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 9);
     }
     
     /**
      * Validates an ITI-9 response
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti9Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 9);
     }
     
     /**
      * Validates an ITI-10 request
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti10Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 10);
     }
     
     /**
      * Validates an ITI-10 response
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti10Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 10);
     }
     
     /**
      * Validates an ITI-21 request
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti21Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 21);
     }

     /**
      * Validates an ITI-21 response
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti21Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 21);
     }

     /**
      * Validates an ITI-22 request
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti22Request(ValidatorAdapterDefinition self) {
         return validationLogic(self, 22);
     }
     
     /**
      * Validates an ITI-22 response
      * @ipfdoc IHE Support#validation mllp
      * @dsl platform-camel-ihe-mllp-core
      */
     public static ValidatorAdapterDefinition iti22Response(ValidatorAdapterDefinition self) {
         return validationLogic(self, 22);
     }
     
     
     
     private static final Map classMap = 
         [
               8 : 'org.openehealth.ipf.platform.camel.ihe.pixpdq.iti8.Iti8Component', 
               9 : 'org.openehealth.ipf.platform.camel.ihe.pixpdq.iti9.Iti9Component', 
              10 : 'org.openehealth.ipf.platform.camel.ihe.pixpdq.iti10.Iti10Component', 
              21 : 'org.openehealth.ipf.platform.camel.ihe.pixpdq.iti21.Iti21Component', 
              22 : 'org.openehealth.ipf.platform.camel.ihe.pixpdq.iti22.Iti22Component', 
         ];
     
     private static ValidatorAdapterDefinition validationLogic(ValidatorAdapterDefinition self, int transaction) {
         self.setValidator(new MessageAdapterValidator());
         return self.input { 
             final String className = 
                 "org.openehealth.ipf.platform.camel.ihe.pixpdq.iti${transaction}.Iti${transaction}Component";
             MllpMarshalUtils.extractMessageAdapter(
                 it.in,
                 it.getProperty(Exchange.CHARSET_NAME),
                 Class.forName(className).newInstance().parser);               
         };
     }
}