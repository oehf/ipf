/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.extend

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.commons.ihe.xds.validate.ValidationProfile
import org.openehealth.ipf.commons.ihe.xds.validate.Actor
import org.openehealth.ipf.commons.ihe.xds.validate.requests.AdhocQueryRequestValidator
import org.openehealth.ipf.commons.ihe.xds.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.requests.RetrieveDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.responses.RetrieveDocumentSetResponseValidator;

import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLRegistryResponse30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLSubmitObjectsRequest30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLAdhocQueryRequest30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLQueryResponse30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLRetrieveDocumentSetRequest30
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLRetrieveDocumentSetResponse30

import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.lcm.SubmitObjectsRequest
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.rs.RegistryResponseType
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryRequest
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryResponse
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.RetrieveDocumentSetRequestType
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.RetrieveDocumentSetResponseType

/**
 * @author Jens Riemschneider
 */
class EbXML30Extensions {
     
    static void install() { 
        
        ValidatorAdapterDefinition.metaClass.iti42Request = { -> 
            delegate.setValidator(new SubmitObjectsRequestValidator())
            delegate.input {
                new EbXMLSubmitObjectsRequest30(it.in.getBody(SubmitObjectsRequest.class))
            }
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REGISTRY)) 
        }        

        ValidatorAdapterDefinition.metaClass.iti42Response = { -> 
            delegate.setValidator(new RegistryResponseValidator())
            delegate.input {
                new EbXMLRegistryResponse30(it.in.getBody(RegistryResponseType.class))
            }            
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REGISTRY)) 
        }
        
        ValidatorAdapterDefinition.metaClass.iti41Request = { -> 
            delegate.setValidator(new ProvideAndRegisterDocumentSetRequestValidator())
            delegate.input {
                new EbXMLProvideAndRegisterDocumentSetRequest30(it.in.getBody(ProvideAndRegisterDocumentSetRequestType.class))
            }            
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REPOSITORY)) 
        }        

        ValidatorAdapterDefinition.metaClass.iti41Response = { -> 
            delegate.setValidator(new RegistryResponseValidator())
            delegate.input {
                new EbXMLRegistryResponse30(it.in.getBody(RegistryResponseType.class))
            }            
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REPOSITORY)) 
        }    
        
        ValidatorAdapterDefinition.metaClass.iti43Request = { -> 
            delegate.setValidator(new RetrieveDocumentSetRequestValidator())
            delegate.input {
                new EbXMLRetrieveDocumentSetRequest30(it.in.getBody(RetrieveDocumentSetRequestType.class))
            }            
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REPOSITORY)) 
        }        

        ValidatorAdapterDefinition.metaClass.iti43Response = { -> 
            delegate.setValidator(new RetrieveDocumentSetResponseValidator())
            delegate.input {
                new EbXMLRetrieveDocumentSetResponse30(it.in.getBody(RetrieveDocumentSetResponseType.class))
            }            
            delegate.staticProfile(new ValidationProfile(false, true, Actor.REPOSITORY))
        }    
        
        ValidatorAdapterDefinition.metaClass.iti18Request = { -> 
            delegate.setValidator(new AdhocQueryRequestValidator())
            delegate.input {
                new EbXMLAdhocQueryRequest30(it.in.getBody(AdhocQueryRequest.class))
            }            
            delegate.staticProfile(new ValidationProfile(true, true, Actor.REGISTRY)) 
        }        

        ValidatorAdapterDefinition.metaClass.iti18Response = { -> 
            delegate.setValidator(new QueryResponseValidator())
            delegate.input {
                new EbXMLQueryResponse30(it.in.getBody(AdhocQueryResponse.class))
            }            
            delegate.staticProfile(new ValidationProfile(true, true, Actor.REGISTRY)) 
        }    
    }        
}
