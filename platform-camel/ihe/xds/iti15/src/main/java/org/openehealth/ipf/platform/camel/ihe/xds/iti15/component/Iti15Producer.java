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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15.component;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.ws.BindingProvider;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.xds.Iti15PortType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ws.DefaultItiProducer;

/**
 * The producer implementation for the ITI-15 component.
 */
public class Iti15Producer extends DefaultItiProducer {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public Iti15Producer(Iti15Endpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint, clientFactory);
    }

    @Override
    protected void callService(Exchange exchange) {
        ProvideAndRegisterDocumentSetRequestType body =
                exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class);
        BindingProvider bindingProvider = (BindingProvider)getClient();
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        
        Map<String, DataHandler> attachments = new HashMap<String, DataHandler>();
        for (Document document : body.getDocument()) {
            attachments.put(document.getId(), document.getValue());
        }
        
        requestContext.put(ProvidedAttachmentOutInterceptor.ATTACHMENTS, attachments);
        
        RegistryResponse result = ((Iti15PortType) getClient()).documentRepositoryProvideAndRegisterDocumentSet(body.getSubmitObjectsRequest());
        Exchanges.resultMessage(exchange).setBody(result);
    }
}
