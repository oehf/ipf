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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.ws.BindingProvider;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

/**
 * The producer implementation for the ITI-15 component.
 */
public class Iti15Producer extends AbstractWsProducer<ProvideAndRegisterDocumentSetRequestType, RegistryResponse> {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public Iti15Producer(AbstractWsEndpoint endpoint, JaxWsClientFactory clientFactory) {
        super(endpoint, clientFactory, ProvideAndRegisterDocumentSetRequestType.class, RegistryResponse.class);
    }

    @Override
    protected RegistryResponse callService(Object client, ProvideAndRegisterDocumentSetRequestType request) {
        Map<String, DataHandler> attachments = new HashMap<String, DataHandler>();
        for (Document document : request.getDocument()) {
            attachments.put(document.getId(), document.getValue());
        }
        
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ProvidedAttachmentOutInterceptor.ATTACHMENTS, attachments);
        return ((Iti15PortType) client).documentRepositoryProvideAndRegisterDocumentSet(request.getSubmitObjectsRequest());
    }
}
