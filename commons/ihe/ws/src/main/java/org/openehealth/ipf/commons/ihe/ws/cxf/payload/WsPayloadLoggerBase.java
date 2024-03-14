/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.wsdl.service.factory.ReflectionServiceFactoryBean;
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase;
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggingContext;
import org.openehealth.ipf.commons.ihe.ws.InterceptorUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.HTTP;


/**
 * Base class for CXF interceptors which store incoming and outgoing HTTP payload
 * into files with user-defined name patterns.
 * <p/>
 * It introduces an additional parameter available in SPEL file name expressions:
 * <ul>
 * <li><tt>partialResponse</tt>&nbsp;&mdash; returns <code>true</code>,
 * when the message under consideration is a WS-Addressing partial response.
 * </ul>
 *
 * @author Dmytro Rud
 */
public class WsPayloadLoggerBase
        extends PayloadLoggerBase<WsPayloadLoggerBase.WsPayloadLoggingContext> {


    public void logPayload(Message message) {
        Long sequenceId = InterceptorUtils.findContextualProperty(message, SEQUENCE_ID_PROPERTY_NAME);
        if (sequenceId == null) {
            sequenceId = getNextSequenceId();
            message.getExchange().put(SEQUENCE_ID_PROPERTY_NAME, sequenceId);
        }

        var spelContext = new WsPayloadLoggingContext(
                sequenceId,
                getInteractionId(message),
                Boolean.TRUE.equals(message.get(Message.PARTIAL_RESPONSE_MESSAGE)));

        var isOutbound = MessageUtils.isOutbound(message);

        doLogPayload(
                spelContext,
                (String) message.get(Message.ENCODING),
                isOutbound ? getOutboundMetadataPayload(message) : getInboundMetadataPayload(message),
                getHeadersPayload(message),
                isOutbound ? getOutboundBodyPayload(message) : getInboundBodyPayload(message));
    }


    /**
     * Appends generic HTTP headers to the given String builder.
     *
     * @param message CXF message which contains the headers.
     * @return message's HTTP headers as a String.
     */
    private static String getHeadersPayload(Message message) {
        var sb = new StringBuilder();

        var encoding = message.get(Message.ENCODING);
        if (encoding != null) {
            sb.append("Character set: ").append(encoding).append('\n');
        }
        sb.append('\n');

        var httpHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (httpHeaders != null) {
            for (var entry : httpHeaders.entrySet()) {
                for (var header : entry.getValue()) {
                    sb.append(entry.getKey()).append(": ").append(header).append('\n');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }


    private static String getInboundMetadataPayload(Message message) {
        var sb = new StringBuilder();

        if (Boolean.TRUE.equals(message.get(Message.REQUESTOR_ROLE))) {
            sb.append("HTTP response code: ").append(message.get(Message.RESPONSE_CODE)).append('\n');
        } else {
            sb.append("HTTP request: ")
                    .append(message.get(Message.HTTP_REQUEST_METHOD))
                    .append(' ')
                    .append(message.get(Message.REQUEST_URL))
                    .append('\n');
        }

        return sb.toString();
    }


    private static String getInboundBodyPayload(Message message) {
        var payloadHolder = message.getContent(StringPayloadHolder.class);
        return (payloadHolder != null) ? payloadHolder.get(HTTP) : "";
    }


    private static String getOutboundMetadataPayload(Message message) {
        var sb = new StringBuilder();

        var endpointAddress = message.get(Message.ENDPOINT_ADDRESS);
        if (endpointAddress != null) {
            sb.append("Target endpoint: ").append(endpointAddress).append('\n');
        } else {
            var response =
                    (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
            sb.append("HTTP response code: ")
                    .append((response != null) ? response.getStatus() : "unknown")
                    .append('\n');
        }

        return sb.toString();
    }


    private static String getOutboundBodyPayload(Message message) {
        var wrapper = OutStreamSubstituteInterceptor.getStreamWrapper(message);
        wrapper.deactivate();
        return wrapper.getCollectedPayload();
    }

    private static String getInteractionId(Message message) {
        var seiClass = (Class<?>) message.getContextualProperty(ReflectionServiceFactoryBean.ENDPOINT_CLASS);
        var name = seiClass.getSimpleName();
        return name.endsWith("PortType") ? name.substring(0, name.length() - 8) : name;
    }

    /**
     * SPEL evaluation context for patterns of file names where WS-based payload should be saved.
     */
    static class WsPayloadLoggingContext extends PayloadLoggingContext {
        private final boolean partialResponse;

        WsPayloadLoggingContext(long sequenceId, String interactionId, boolean partialResponse) {
            super(sequenceId, interactionId);
            this.partialResponse = partialResponse;
        }

        public boolean isPartialResponse() {
            return partialResponse;
        }
    }

}
