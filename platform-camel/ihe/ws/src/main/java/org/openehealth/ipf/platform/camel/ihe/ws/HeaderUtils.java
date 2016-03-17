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
package org.openehealth.ipf.platform.camel.ihe.ws;

import static org.apache.cxf.message.Message.PROTOCOL_HEADERS;

import java.util.*;

import javax.xml.namespace.QName;

import org.apache.camel.Message;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;

/**
 * Utilities for handling HTTP and SOAP headers in Web Service interactions.
 * @author Dmytro Rud
 */
abstract public class HeaderUtils {
    
    private HeaderUtils() {
        throw new IllegalStateException("Cannot instantiate utility class");
    }


    private static interface DefaultValueFactory<T> {
        T createDefaultValue();
    }

    private static final DefaultValueFactory<Map<String, List<String>>> HTTP_HEADERS_CONTAINER_FACTORY = 
        new DefaultValueFactory<Map<String, List<String>>>() {
            @Override
            public Map<String, List<String>> createDefaultValue() {
                return new HashMap<>();
            }
        };

    private static final DefaultValueFactory<List<Header>> SOAP_HEADERS_CONTAINER_FACTORY = 
        new DefaultValueFactory<List<Header>>() {
            @Override
            public List<Header> createDefaultValue() {
                return new ArrayList<>();
            }
        };


    public static void processIncomingHeaders(
            Map<String, Object> messageContext, 
            Message message) 
    {
        processIncomingHttpHeaders(messageContext, message);
        processIncomingSoapHeaders(messageContext, message);
    }


    public static void processUserDefinedOutgoingHeaders(
            Map<String, Object> messageContext, 
            Message message,
            boolean isRequest) 
    {
        processUserDefinedOutgoingHttpHeaders(messageContext, message, isRequest);
        processUserDefinedOutgoingSoapHeaders(messageContext, message, isRequest);
    }


    /**
     * Returns headers of the message represented by the given context.
     * 
     * @param <T>
     *      type of headers' container.
     * @param messageContext
     *      Web Service message context.
     * @param key
     *      key under which the headers reside in the message context. 
     * @param useInputMessage
     *      whether input message should the used.
     * @param needCreateWhenNotExist
     *      whether the headers' map should be created when it does 
     *      not exist.
     * @param defaultValueFactory
     *      factory for producing default values.
     * @return
     *      either the map of HTTP headers as found in the message context,
     *      or a newly created map when none found, or <code>null</code> 
     *      when creation of a new map is not allowed. 
     */
    @SuppressWarnings("unchecked")
    private static <T> T getHeaders(
            Map<String, Object> messageContext,
            String key,
            boolean useInputMessage,
            boolean needCreateWhenNotExist, 
            DefaultValueFactory<T> defaultValueFactory) 
    {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) messageContext;
        Map<String, Object> headersContainer = useInputMessage
            ? wrappedContext.getWrappedMap()
            : wrappedContext.getWrappedMessage().getExchange().getOutMessage();
        
        T headers = (T) headersContainer.get(key);
        if ((headers == null) && needCreateWhenNotExist) {
            headers = defaultValueFactory.createDefaultValue();
            headersContainer.put(key, headers);
        }
        return headers;
    }


    /**
     * Stores a map of incoming SOAP headers from the given  
     * Web Service message context into the Camel header
     * {@link AbstractWsEndpoint#INCOMING_SOAP_HEADERS}
     * of the given Camel message.
     * 
     * @param messageContext
     *      Web Service message contents.
     * @param message
     *      Camel message in whose headers the 
     *      SOAP headers should be stored.
     */
    private static void processIncomingSoapHeaders(
            Map<String, Object> messageContext, 
            Message message) 
    {
        HashMap<QName, Header> userHeaders = new HashMap<>();
        List<Header> soapHeaders = getHeaders(
                messageContext, Header.HEADER_LIST, true, false, null);
        if (soapHeaders != null) {
            for (Header soapHeader : soapHeaders) {
                userHeaders.put(soapHeader.getName(), soapHeader);
            }
        }
        message.setHeader(AbstractWsEndpoint.INCOMING_SOAP_HEADERS, userHeaders);
    }


    /**
     * Injects user-defined SOAP headers from the header 
     * {@link AbstractWsEndpoint#OUTGOING_SOAP_HEADERS}
     * of the given Camel message into the given Web Service 
     * message context.
     * 
     * @param messageContext
     *      Web Service message contents.
     * @param message
     *      Camel message from whose headers the 
     *      SOAP headers should be taken.
     * @param isRequest
     *      whether the Web Service message under consideration 
     *      is a request one (<code>false</code> on server side, 
     *      <code>true</code> on client side). 
     */
    private static void processUserDefinedOutgoingSoapHeaders(
            Map<String, Object> messageContext, 
            Message message,
            boolean isRequest) 
    {
        Collection<Header> userHeaders = null;
        Object o = message.getHeader(AbstractWsEndpoint.OUTGOING_SOAP_HEADERS);
        if (o instanceof Collection) {
            userHeaders = (Collection<Header>) o;
        } else if (o instanceof Map) {
            userHeaders = ((Map<QName, Header>) o).values();
        }

        if ((userHeaders != null) && ! userHeaders.isEmpty()) {
            List<Header> soapHeaders = getHeaders(
                    messageContext, Header.HEADER_LIST, isRequest, true, SOAP_HEADERS_CONTAINER_FACTORY);
            soapHeaders.addAll(userHeaders);
       }
    }


    /**
     * Stores a map of incoming HTTP headers from the given  
     * Web Service message context into the Camel header
     * {@link AbstractWsEndpoint#INCOMING_HTTP_HEADERS}
     * of the given Camel message.
     * 
     * @param messageContext
     *      Web Service message contents.
     * @param message
     *      Camel message in whose headers the 
     *      HTTP headers should be stored.
     */
    private static void processIncomingHttpHeaders(
            Map<String, Object> messageContext, 
            Message message) 
    {
        Map<String, String> userHeaders = new HashMap<>();
        Map<String, List<String>> httpHeaders = getHeaders(
                messageContext, PROTOCOL_HEADERS, true, false, null);
        if (httpHeaders != null) {
            for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
                userHeaders.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        message.setHeader(AbstractWsEndpoint.INCOMING_HTTP_HEADERS, userHeaders);
    }


    /**
     * Injects user-defined HTTP headers from the header 
     * {@link AbstractWsEndpoint#OUTGOING_HTTP_HEADERS}
     * of the given Camel message into the given Web Service 
     * message context.
     * 
     * @param messageContext
     *      Web Service message contents.
     * @param message
     *      Camel message from whose headers the 
     *      HTTP headers should be taken.
     * @param isRequest
     *      whether the Web Service message under consideration 
     *      is a request one (<code>false</code> on server side, 
     *      <code>true</code> on client side). 
     */
    private static void processUserDefinedOutgoingHttpHeaders(
            Map<String, Object> messageContext, 
            Message message,
            boolean isRequest) 
    {
        Map<String, String> headers = CastUtils.cast(
                message.getHeader(AbstractWsEndpoint.OUTGOING_HTTP_HEADERS, Map.class));
        
        if ((headers != null) && ! headers.isEmpty()) {
            Map<String, List<String>> httpHeaders = getHeaders(
                    messageContext, PROTOCOL_HEADERS, isRequest, true, HTTP_HEADERS_CONTAINER_FACTORY);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), Collections.singletonList(entry.getValue()));
            }
       }
    }
}
