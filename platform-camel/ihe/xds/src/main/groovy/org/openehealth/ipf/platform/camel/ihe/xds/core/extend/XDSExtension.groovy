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
package org.openehealth.ipf.platform.camel.ihe.xds.core.extend;

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import static org.openehealth.ipf.platform.camel.core.process.ProcessorBasedExchangeValidator.definition;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*;

/**
 * XDS DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 *
 * @DSL
 *
 * @author Jens Riemschneider
 */
public class XDSExtension {

     /**
      * Validates an ITI-14 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti14Request(ValidatorAdapterDefinition self) {
         return definition(self, iti14RequestValidator());
     }
     
     /**
      * Validates an ITI-14 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti14Response(ValidatorAdapterDefinition self) {
         return definition(self, iti14ResponseValidator());
     }
     
     /**
      * Validates an ITI-15 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti15Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti15RequestValidator());
     }        
    
     /**
      * Validates an ITI-15 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti15Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti15ResponseValidator());
     }    
     
     /**
      * Validates an ITI-16 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti16Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti16RequestValidator());
     }        
    
     /**
      * Validates an ITI-16 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti16Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti16ResponseValidator());
     }         
     
     /**
      * Validates an ITI-18 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti18Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti18RequestValidator());
     }        
    
     /**
      * Validates an ITI-18 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti18Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti18ResponseValidator());
     }    
     
     /**
      * Validates an ITI-41 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti41Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti41RequestValidator());
     }        
    
     /**
      * Validates an ITI-41 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti41Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti41ResponseValidator());
     }    

     /**
      * Validates an ITI-42 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti42Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti42RequestValidator());
     }
    
     /**
      * Validates an ITI-42 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti42Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti42ResponseValidator());
     }
     
     /**
      * Validates an ITI-43 request
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti43Request(ValidatorAdapterDefinition self) { 
         return definition(self, iti43RequestValidator());
     }        
    
     /**
      * Validates an ITI-43 response
      * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti43Response(ValidatorAdapterDefinition self) { 
         return definition(self, iti43ResponseValidator());
     }    
}
