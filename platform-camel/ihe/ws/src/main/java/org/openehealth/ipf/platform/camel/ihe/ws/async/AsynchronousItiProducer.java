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
package org.openehealth.ipf.platform.camel.ihe.ws.async;

import java.util.Map;
import java.util.UUID;

import javax.xml.ws.BindingProvider;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer;

/**
 * Producer implementation with support for WSA-based asynchronous interactions.
 * @author Dmytro Rud
 */
abstract public class AsynchronousItiProducer<InType, OutType> extends DefaultItiProducer<InType, OutType> {
    
    /**
     * Whether request payload should be stored in the asynchrony correlator. 
     */
    private final boolean needStoreRequestPayload;
    
    /**
     * Constructs the producer.
     * @param endpoint
     *          the asynchronous endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.
     * @param needStoreRequestPayload
     *          <code>true</code>, when payload of asynchronous requests 
     *          should be stored as part of correlation data.              
     */
    public AsynchronousItiProducer(
            AsynchronousItiEndpoint endpoint, 
            ItiClientFactory clientFactory,
            boolean needStoreRequestPayload) 
    {
        super(endpoint, clientFactory);
        this.needStoreRequestPayload = needStoreRequestPayload;
    }

    /**
     * Enriches the given request exchange from the request context data.
     */
    protected void enrichRequestExchange(Exchange exchange, Map<String, Object> requestContext) {
        // does nothing per default
    }

    /**
     * Enriches the given response message from the request context data.
     */
    protected void enrichResponseMessage(Message message, Map<String, Object> responseContext) {
        // does nothing per default
    }


    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {
        // prepare
        InType body = (InType) exchange.getIn().getBody();
        Object client = getClient();
        configureClient(client);
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        cleanRequestContext(requestContext);

        enrichRequestExchange(exchange, requestContext);
        
        // get and analyse WS-Addressing asynchrony configuration
        String replyToUri = exchange.getIn().getHeader(
                AsynchronousItiEndpoint.WSA_REPLYTO_HEADER_NAME, String.class);
        if ((replyToUri != null) && replyToUri.trim().isEmpty()) {
            replyToUri = null;
        }

        // for asynchronous interaction: configure WSA headers and store correlation data
        if (replyToUri != null) {
            String messageId = UUID.randomUUID().toString();
            configureWSAHeaders(messageId, replyToUri, requestContext);
            
            AsynchronousItiEndpoint endpoint = (AsynchronousItiEndpoint) getEndpoint();
            String correlationKey = exchange.getIn().getHeader(
                    AsynchronousItiEndpoint.CORRELATION_KEY_HEADER_NAME, 
                    String.class);

            // TODO: better body serialization instead of .toString()
            endpoint.getCorrelator().put(
                    messageId, 
                    endpoint.getEndpointUri(),
                    correlationKey,
                    needStoreRequestPayload ? body.toString() : null);
        }
        
        // invoke
        exchange.setPattern((replyToUri == null) ? ExchangePattern.InOut : ExchangePattern.InOnly);
        OutType result = callService(client, body);
        
        // for synchronous interaction: handle response
        if (replyToUri == null) {
            Message responseMessage = Exchanges.resultMessage(exchange);
            Map<String, Object> responseContext = bindingProvider.getResponseContext();
            enrichResponseMessage(responseMessage, responseContext);
            responseMessage.setBody(result);
        } 
    }


    /**
     * Sets thread safety & timeout options of the given CXF client.  
     */
    protected void configureClient(Object o) {
        ClientImpl client = (ClientImpl) ClientProxy.getClient(o);
        client.setThreadLocalRequestContext(true);
        client.setSynchronousTimeout(Integer.MAX_VALUE);
    }
    
    
    /**
     * Request context is shared among subsequent requests, so we have to clean it.
     */
    protected void cleanRequestContext(Map<String, Object> requestContext) {
        requestContext.remove(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);
    }
    
    
    /**
     * Initializes WS-Addressing headers MessageID and ReplyTo, 
     * and stores them into the given message context.
     */
    private void configureWSAHeaders(String messageId, String replyToUri, Map<String, Object> context) {
        // header container
        AddressingPropertiesImpl maps = new AddressingPropertiesImpl();
        context.put(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES, maps);

        // MessageID header
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(messageId);
        maps.setMessageID(uri);

        // ReplyTo header
        AttributedURIType uri2 = new AttributedURIType();
        uri2.setValue(replyToUri);
        EndpointReferenceType endpointReference = new EndpointReferenceType();
        endpointReference.setAddress(uri2);
        maps.setReplyTo(endpointReference);
    }
}
