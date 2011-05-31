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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.extend;

import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Hl7TranslatorV2toV3;
import org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Hl7TranslatorV3toV2;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import static org.openehealth.ipf.platform.camel.core.process.ProcessorBasedExchangeValidator.definition;
import static org.openehealth.ipf.platform.camel.ihe.pixpdqv3.PixPdqV3CamelTranslators.translatorHL7v2toHL7v3;
import static org.openehealth.ipf.platform.camel.ihe.pixpdqv3.PixPdqV3CamelTranslators.translatorHL7v3toHL7v2;
import static org.openehealth.ipf.platform.camel.ihe.pixpdqv3.PixPdqV3CamelValidators.*;

/**
 * HL7 v3 DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} 
 * using the {@code use} keyword.
 * 
 * @DSL
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
class PixPdqV3Extension {

     /**
      * Validates an ITI-44 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti44Request(ValidatorAdapterDefinition self) {
         return definition(self, iti44RequestValidator());
     }
     
     /**
      * Validates an ITI-44 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti44Response(ValidatorAdapterDefinition self) {
         return definition(self, iti44ResponseValidator());
     }
     
     /**
      * Validates an ITI-45 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti45Request(ValidatorAdapterDefinition self) {
         return definition(self, iti45RequestValidator());
     }
     
     /**
      * Validates an ITI-45 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti45Response(ValidatorAdapterDefinition self) {
         return definition(self, iti45ResponseValidator());
     }
     
     /**
      * Validates an ITI-46 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti46Request(ValidatorAdapterDefinition self) {
         return definition(self, iti46RequestValidator());
     }
     
     /**
      * Validates an ITI-46 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti46Response(ValidatorAdapterDefinition self) {
         return definition(self, iti46ResponseValidator());
     }
     
     /**
      * Validates an ITI-47 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti47Request(ValidatorAdapterDefinition self) {
         return definition(self, iti47RequestValidator());
     }
     
     /**
      * Validates an ITI-47 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationpixpdq
      */
     public static ValidatorAdapterDefinition iti47Response(ValidatorAdapterDefinition self) {
         return definition(self, iti47ResponseValidator());
     }
     

     /**
      * Translates a HL7v3 message to HL7v2 using the given translator.
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-UsingDSLExtensions
      */
     public static ProcessorDefinition translateHL7v3toHL7v2(
             ProcessorDefinition self, 
             Hl7TranslatorV3toV2 translator) {
         return self.process(translatorHL7v3toHL7v2(translator));
     }
     
     /**
      * Translates a HL7v2 message to HL7v3 using the given translator.
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-UsingDSLExtensions
      */
     public static ProcessorDefinition translateHL7v2toHL7v3(
             ProcessorDefinition self, 
             Hl7TranslatorV2toV3 translator) {
         return self.process(translatorHL7v2toHL7v3(translator));
     }
}
