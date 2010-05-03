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

import static org.openehealth.ipf.commons.ihe.xds.core.validate.Actor.*;

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RetrieveDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RetrieveDocumentSetResponseValidator;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLRegistryResponse21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLSubmitObjectsRequest21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLProvideAndRegisterDocumentSetRequest21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLAdhocQueryRequest21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLQueryResponse21;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLAdhocQueryRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRetrieveDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRetrieveDocumentSetResponse30;
import org.apache.camel.builder.RouteBuilder;

/**
 * XDS DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 *
 * @dsl
 *
 * @author Jens Riemschneider
 */
public class XDSExtension {
     // Aliases for EbXML 2.1 classes: 
     private static final Class Submit21 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;
     private static final Class RegResp21 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
     private static final Class Provide21 = org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType;
     private static final Class Query21 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryRequest;
     
     // Aliases for EbXML 3.0 classes: 
     private static final Class Submit30 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
     private static final Class RegResp30 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
     private static final Class Provide30 = org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
     private static final Class Retrieve30 = org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
     private static final Class RetrieveResp30 = org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
     private static final Class Query30 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
     private static final Class QueryResp30 = org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;

     /**
      * Validates an ITI-14 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti14Request(ValidatorAdapterDefinition self) {
         self.setValidator(new SubmitObjectsRequestValidator());
         self.input { new EbXMLSubmitObjectsRequest21(it.in.getBody(Submit21)) };
         return self.staticProfile(new ValidationProfile(false, false, REGISTRY));
     }
     
     /**
      * Validates an ITI-14 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti14Response(ValidatorAdapterDefinition self) {
         self.setValidator(new RegistryResponseValidator());
         self.input { new EbXMLRegistryResponse21(it.in.getBody(RegResp21)) };     
         return self.staticProfile(new ValidationProfile(false, false, REGISTRY));
     }
     
     /**
      * Validates an ITI-15 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti15Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new ProvideAndRegisterDocumentSetRequestValidator());
         self.input { new EbXMLProvideAndRegisterDocumentSetRequest21(it.in.getBody(Provide21)) };            
         return self.staticProfile(new ValidationProfile(false, false, REPOSITORY));
     }        
    
     /**
      * Validates an ITI-15 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti15Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new RegistryResponseValidator());
         self.input { new EbXMLRegistryResponse21(it.in.getBody(RegResp21)) };            
         return self.staticProfile(new ValidationProfile(false, false, REPOSITORY));
     }    
     
     /**
      * Validates an ITI-16 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti16Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new AdhocQueryRequestValidator());
         self.input { new EbXMLAdhocQueryRequest21(it.in.getBody(Query21)) };            
         return self.staticProfile(new ValidationProfile(true, false, REGISTRY));
     }        
    
     /**
      * Validates an ITI-16 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsa
      */
     public static ValidatorAdapterDefinition iti16Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new QueryResponseValidator());
         self.input { new EbXMLQueryResponse21(it.in.getBody(RegResp21)) };            
         return self.staticProfile(new ValidationProfile(true, false, REGISTRY)); 
     }         
     
     /**
      * Validates an ITI-18 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti18Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new AdhocQueryRequestValidator());
         self.input { new EbXMLAdhocQueryRequest30(it.in.getBody(Query30)) };            
         return self.staticProfile(new ValidationProfile(true, true, REGISTRY));
     }        
    
     /**
      * Validates an ITI-18 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti18Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new QueryResponseValidator());
         self.input { new EbXMLQueryResponse30(it.in.getBody(QueryResp30)) };
         return self.staticProfile(new ValidationProfile(true, true, REGISTRY));
     }    
     
     /**
      * Validates an ITI-41 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti41Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new ProvideAndRegisterDocumentSetRequestValidator());
         self.input { new EbXMLProvideAndRegisterDocumentSetRequest30(it.in.getBody(Provide30)) };            
         return self.staticProfile(new ValidationProfile(false, true, REPOSITORY)); 
     }        
    
     /**
      * Validates an ITI-41 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti41Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new RegistryResponseValidator());
         self.input { new EbXMLRegistryResponse30(it.in.getBody(RegResp30)) };            
         return self.staticProfile(new ValidationProfile(false, true, REPOSITORY)); 
     }    

     /**
      * Validates an ITI-42 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti42Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new SubmitObjectsRequestValidator());
         self.input {
             org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest req = it.in.getBody(Submit30)
             new EbXMLSubmitObjectsRequest30(req) };
         return self.staticProfile(new ValidationProfile(false, true, REGISTRY)); 
     }
    
     /**
      * Validates an ITI-42 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti42Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new RegistryResponseValidator());
         self.input { new EbXMLRegistryResponse30(it.in.getBody(RegResp30)) };            
         return self.staticProfile(new ValidationProfile(false, true, REGISTRY));
     }
     
     /**
      * Validates an ITI-43 request
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti43Request(ValidatorAdapterDefinition self) { 
         self.setValidator(new RetrieveDocumentSetRequestValidator());
         self.input { new EbXMLRetrieveDocumentSetRequest30(it.in.getBody(Retrieve30)) };           
         return self.staticProfile(new ValidationProfile(false, true, REPOSITORY)); 
     }        
    
     /**
      * Validates an ITI-43 response
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/IHE+support#IHEsupport-validationxdsb
      */
     public static ValidatorAdapterDefinition iti43Response(ValidatorAdapterDefinition self) { 
         self.setValidator(new RetrieveDocumentSetResponseValidator());
         self.input { new EbXMLRetrieveDocumentSetResponse30(it.in.getBody(RetrieveResp30)) };            
         return self.staticProfile(new ValidationProfile(false, true, REPOSITORY));
     }    
}
