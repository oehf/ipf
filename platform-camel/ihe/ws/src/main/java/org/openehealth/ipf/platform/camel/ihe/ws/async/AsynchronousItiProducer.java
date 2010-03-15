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
abstract public class AsynchronousItiProducer extends DefaultItiProducer {
    
    /**
     * Name of incoming Camel header where the user should store the URL
     * of asynchronous response endpoint (WS-Addressing header "ReplyTo").  
     */
    public static final String WSA_REPLYTO_HEADER_NAME = "ipf.wsa.ReplyTo";
    
    /**
     * Name of incoming Camel header where the user should store 
     * the optional correlation key.  
     */
    public static final String CORRELATION_KEY_HEADER_NAME = "ipf.correlation.key";
    
    
    /**
     * Constructs the producer.
     * @param endpoint
     *          the asynchronous endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public AsynchronousItiProducer(AsynchronousItiEndpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint, clientFactory);
    }
    
    /**
     * Returns unique ID of the given message for using in correlation.
     */
    abstract protected String getMessageIdForCorrelation(String body);

    /**
     * Enriches the given request exchange from the request context data.
     */
    abstract protected  void enrichRequestExchange(Exchange exchange, Map<String, Object> requestContext);
    
    /**
     * Calls the remote service.
     */
    abstract protected String call(Object client, String body);
    
    /**
     * Enriches the given response message from the request context data.
     */
    abstract protected void enrichResponseMessage(Message message, Map<String, Object> responseContext);
    

    @Override
    protected void callService(Exchange exchange) {
        // prepare
        String body = exchange.getIn().getBody(String.class);
        Object client = getClient();
        configureClient(client);
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        cleanRequestContext(requestContext);

        enrichRequestExchange(exchange, requestContext);
        
        // get and analyse WS-Addressing asynchrony configuration
        String replyToUri = exchange.getIn().getHeader(WSA_REPLYTO_HEADER_NAME, String.class);
        boolean async = (replyToUri != null) && (! replyToUri.isEmpty());

        // for asynchronous interaction: prepare WSA headers
        if (async) {
            setWSAReplyToHeader(replyToUri, requestContext);
            
            AsynchronousItiEndpoint endpoint = (AsynchronousItiEndpoint) getEndpoint();
            String correlationKey = 
                exchange.getIn().getHeader(CORRELATION_KEY_HEADER_NAME, String.class);
            endpoint.getCorrelator().put(
                    getMessageIdForCorrelation(body), 
                    endpoint.getEndpointUri(),
                    correlationKey);
        }
        
        // invoke
        exchange.setPattern(async ? ExchangePattern.InOnly : ExchangePattern.InOut);
        String result = call(client, body);
        
        // for synchronous interaction: handle response
        if (! async) {
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
     * @param requestContext
     */
    protected void cleanRequestContext(Map<String, Object> requestContext) {
        requestContext.remove(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);
    }
    
    
    /**
     * Initializes a WS-Addressing ReplyTo header and stores it into the given message context.
     * 
     * @param replyToUri
     *          URI to which the response should be sent  
     * @param context
     *          SOAP message context 
     */
    protected void setWSAReplyToHeader(String replyToUri, Map<String, Object> context) {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(replyToUri);
        EndpointReferenceType endpointReference = new EndpointReferenceType();
        endpointReference.setAddress(uri);
        AddressingPropertiesImpl maps = new AddressingPropertiesImpl();
        maps.setReplyTo(endpointReference);
        context.put(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES, maps);
    }
}
