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

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterType
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.requests.AdhocQueryRequestValidator
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.responses.RegistryResponseValidator;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLRegistryResponse21
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLSubmitObjectsRequest21
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLProvideAndRegisterDocumentSetRequest21
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLAdhocQueryRequest21
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLQueryResponse21

import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest

/**
 * @author Jens Riemschneider
 */
class EbXML21Extensions {
     
    static void install() { 
        
        ValidatorAdapterType.metaClass.iti14Request = { -> 
            delegate.setValidator(new SubmitObjectsRequestValidator())
            delegate.input {
                new EbXMLSubmitObjectsRequest21(it.in.getBody(SubmitObjectsRequest.class))
            }            
        }        

        ValidatorAdapterType.metaClass.iti14Response = { -> 
            delegate.setValidator(new RegistryResponseValidator())
            delegate.input {
                new EbXMLRegistryResponse21(it.in.getBody(RegistryResponse.class))
            }            
        }
        
        ValidatorAdapterType.metaClass.iti15Request = { -> 
            delegate.setValidator(new ProvideAndRegisterDocumentSetRequestValidator())
            delegate.input {
                new EbXMLProvideAndRegisterDocumentSetRequest21(it.in.getBody(ProvideAndRegisterDocumentSetRequestType.class))
            }            
        }        

        ValidatorAdapterType.metaClass.iti15Response = { -> 
            delegate.setValidator(new RegistryResponseValidator())
            delegate.input {
                new EbXMLRegistryResponse21(it.in.getBody(RegistryResponse.class))
            }            
        }    
        
        ValidatorAdapterType.metaClass.iti16Request = { -> 
            delegate.setValidator(new AdhocQueryRequestValidator())
            delegate.input {
                new EbXMLAdhocQueryRequest21(it.in.getBody(AdhocQueryRequest.class))
            }            
        }        

        ValidatorAdapterType.metaClass.iti16Response = { -> 
            delegate.setValidator(new QueryResponseValidator())
            delegate.input {
                new EbXMLQueryResponse21(it.in.getBody(RegistryResponse.class))
            }            
        }    
    }        
}
