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
package org.openehealth.ipf.platform.camel.ihe.ws;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.ws.HttpHeaderUtils.processIncomingHttpHeaders;
import static org.openehealth.ipf.platform.camel.ihe.ws.HttpHeaderUtils.processUserDefinedOutgoingHttpHeaders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import javax.xml.ws.BindingProvider;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * Camel producer used to make calls to a Web Service.
 * 
 * @param <InType>
 *            type of request messages (what the Camel route prepares).
 * @param <OutType>
 *            type of response messages (what the Camel route obtains after the call).
 * 
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiProducer<InType, OutType> extends DefaultProducer {
    private static final Log log = LogFactory.getLog(DefaultItiProducer.class);

    private final ItiClientFactory clientFactory;
    private final Class<InType> inTypeClass;
    private final boolean allowAsynchrony; 
    

    /**
     * Constructs the producer without support for asynchronous calls.
     * 
     * @param endpoint
     *          the endpoint that creates this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public DefaultItiProducer(DefaultItiEndpoint endpoint, ItiClientFactory clientFactory) {
        this(endpoint, clientFactory, false);
    }

    
    /**
     * Constructs the producer.
     * 
     * @param endpoint
     *          the endpoint that creates this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.
     * @param allowAsynchrony
     *          whether asynchronous calls should be supported.
     * @param needStoreRequestPayload
     *          whether request payload should be stored in the correlator
     *          when preparing asynchronous requests.
     */
    @SuppressWarnings("unchecked")
    public DefaultItiProducer(
            DefaultItiEndpoint endpoint, 
            ItiClientFactory clientFactory,
            boolean allowAsynchrony) 
    {
        super(endpoint);
        notNull(clientFactory, "clientFactory cannot be null");
        this.clientFactory = clientFactory;
        this.allowAsynchrony = allowAsynchrony;
        
        // get java.lang.Class object for the input type
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = genericSuperclass.getActualTypeArguments()[0];
        this.inTypeClass = (Class<InType>) type;
    }

    
    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Calling webservice on '" + getServiceInfo().getServiceName() + "' with " + exchange);
        
        // prepare
        InType body = exchange.getIn().getBody(inTypeClass);
        Object client = getClient();
        configureClient(client);
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        cleanRequestContext(requestContext);

        enrichRequestExchange(exchange, requestContext);
        processUserDefinedOutgoingHttpHeaders(requestContext, exchange.getIn(), true);
        
        // get and analyse WS-Addressing asynchrony configuration
        String replyToUri = 
            allowAsynchrony 
                    ? exchange.getIn().getHeader(DefaultItiEndpoint.WSA_REPLYTO_HEADER_NAME, String.class) 
                    : null; 
        if ((replyToUri != null) && replyToUri.trim().isEmpty()) {
            replyToUri = null;
        }

        // for asynchronous interaction: configure WSA headers and store correlation data
        if (replyToUri != null) {
            String messageId = UUID.randomUUID().toString();
            configureWSAHeaders(messageId, replyToUri, requestContext);
            
            DefaultItiEndpoint endpoint = (DefaultItiEndpoint) getEndpoint();
            String correlationKey = exchange.getIn().getHeader(
                    DefaultItiEndpoint.CORRELATION_KEY_HEADER_NAME, 
                    String.class);

            endpoint.getCorrelator().put(
                    messageId, 
                    endpoint.getEndpointUri(),
                    correlationKey);
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
            processIncomingHttpHeaders(responseContext, responseMessage);
        } 
    }


    /**
     * Sends the given request body to a Web Service via the given client proxy.
     */
    protected abstract OutType callService(Object client, InType body);

    
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
    private static void configureWSAHeaders(String messageId, String replyToUri, Map<String, Object> context) {
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
    
    
    /**
     * Retrieves the client stub for the Web Service.
     * <p>
     * This method caches the client stub instance and therefore requires thread
     * synchronization.
     * 
     * @return the client stub.
     */
    protected Object getClient() {
        return clientFactory.getClient();
    }


    /**
     * @return the info describing the Web Service.
     */
    public ItiServiceInfo getServiceInfo() {
        return clientFactory.getServiceInfo();
    }
}
