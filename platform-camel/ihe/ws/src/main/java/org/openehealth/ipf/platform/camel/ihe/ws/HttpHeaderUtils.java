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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;

/**
 * Utilities for handling HTTP headers in Web Service interactions.
 * @author Dmytro Rud
 */
abstract public class HttpHeaderUtils {
    
    private HttpHeaderUtils() {
        throw new IllegalStateException("Cannot instantiate utility class");
    }
    
    /**
     * Returns HTTP headers of the message represented 
     * by the given context.
     * 
     * @param messageContext
     *      Web Service message context. 
     * @param useInputMessage
     *      whether input message should the used.
     * @param needCreateWhenNotExist
     *      whether the headers' map should be created when it does 
     *      not exist.
     * @return
     *      either the map of HTTP headers as found in the message context,
     *      or a newly created map when none found, or <code>null</code> 
     *      when creation of a new map is not allowed. 
     */
    static Map<String, List<String>> getHttpHeaders(
            Map<String, Object> messageContext,
            boolean useInputMessage,
            boolean needCreateWhenNotExist) 
    {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) messageContext;
        Map<String, Object> headersContainer = useInputMessage
            ? wrappedContext.getWrappedMap()
            : wrappedContext.getWrappedMessage().getExchange().getOutMessage();
        
        Map<String, List<String>> headers = 
            CastUtils.cast((Map<?, ?>) headersContainer.get(PROTOCOL_HEADERS));
        if ((headers == null) && needCreateWhenNotExist) {
            headers = new HashMap<String, List<String>>();
            headersContainer.put(PROTOCOL_HEADERS, headers);
        }
        return headers;
    }


    /**
     * Stores a map of incoming HTTP headers from the given  
     * Web Service message context into the Camel header
     * {@link DefaultItiEndpoint#INCOMING_HTTP_HEADERS} 
     * of the given Camel message.
     * 
     * @param messageContext
     *      Web Service message contents.
     * @param message
     *      Camel message in whose headers the 
     *      HTTP headers should be stored.
     */
    static void processIncomingHttpHeaders(
            Map<String, Object> messageContext, 
            Message message) 
    {
        Map<String, String> userHeaders = new HashMap<String, String>();
        Map<String, List<String>> httpHeaders = getHttpHeaders(messageContext, true, false);
        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            userHeaders.put(entry.getKey(), entry.getValue().get(0));
        }
        message.setHeader(DefaultItiEndpoint.INCOMING_HTTP_HEADERS, userHeaders);
    }


    /**
     * Injects user-defined HTTP headers from the header 
     * {@link DefaultItiEndpoint#OUTGOING_HTTP_HEADERS} 
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
    static void processUserDefinedOutgoingHttpHeaders(
            Map<String, Object> messageContext, 
            Message message,
            boolean isRequest) 
    {
        Map<String, String> headers = CastUtils.cast(
                message.getHeader(DefaultItiEndpoint.OUTGOING_HTTP_HEADERS, Map.class));
        
        if ((headers != null) && ! headers.isEmpty()) {
            Map<String, List<String>> httpHeaders = getHttpHeaders(messageContext, isRequest, true);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), Collections.singletonList(entry.getValue()));
            }
       }
    }
}
