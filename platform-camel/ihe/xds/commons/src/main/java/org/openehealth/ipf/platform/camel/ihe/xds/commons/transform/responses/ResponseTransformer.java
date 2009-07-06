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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;

/**
 * Transforms between {@link Response} and the ebXML representation.
 * @author Jens Riemschneider
 */
public class ResponseTransformer {    
    private final EbXMLFactory factory;
    
    /**
     * Constructs the transformer.
     * @param factory
     *          the factory for ebXML objects.
     */
    public ResponseTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    /**
     * Transforms a {@link Response} to a {@link RegistryResponse}.
     * @param response
     *          the source of the transformation.
     * @return 
     * @return
     *          the destination of the transformation. 
     */
    public RegistryResponse toEbXML(Response response) {
        notNull(response, "response cannot be null");
        
        RegistryResponse ebXML = factory.createRegistryResponse();
        
        ebXML.setStatus(response.getStatus());
        ebXML.setErrors(response.getErrors());
        
        return ebXML;
    }
    
    /**
     * Transforms a {@link RegistryResponse} to a {@link Response}.
     * @param ebXML
     *          the source of the transformation.
     * @return the destination of the transformation. 
     */
    public Response fromEbXML(RegistryResponse ebXML) {
        notNull(ebXML, "ebXML cannot be null");
        
        Response response = new Response();
        
        response.setStatus(ebXML.getStatus());
        response.getErrors().addAll(ebXML.getErrors());
        
        return response;
    }
}
