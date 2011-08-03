/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.commons.lang.Validate;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.List;

/**
 * @author Dmytro Rud
 */
public class Hl7v2WsFaultHandlerInterceptor extends AbstractSoapInterceptor {
    private final Hl7v2WsFailureHandler faultHandler;

    public Hl7v2WsFaultHandlerInterceptor(Hl7v2WsFailureHandler faultHandler) {
        super(Phase.MARSHAL);
        Validate.notNull(faultHandler);
        this.faultHandler = faultHandler;
    }

    /**
     * Returns Exception object from the outgoing fault message contained in the given
     * CXF exchange, or <code>null</code>, when no exception could be extracted.
     */
    public static Exception extractOutgoingException(Exchange exchange) {
        Message outFaultMessage = exchange.getOutFaultMessage();
        return (outFaultMessage != null) ? outFaultMessage.getContent(Exception.class) : null;
    }

    /**
     * Returns String payload of the outgoing message contained in the given
     * CXF exchange, or <code>null</code>, when no String payload could be extracted.
     */
    public static String extractOutgoingPayload(Exchange exchange) {
        try {
            return (String) exchange.getOutMessage().getContent(List.class).get(0);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Message wrappedMessage = message.getMessage();
        Exchange exchange = wrappedMessage.getExchange();

        // exchange contains SOAP fault
        boolean failed = (extractOutgoingException(exchange) != null);

        // exchange does not contain a valid HL7v2 message at all
        // or does contain an HL7v2 NAK with code 'AR' or 'CR'
        if (! failed) {
            String response = extractOutgoingPayload(exchange);
            try {
                int pos = response.indexOf("\r\nMSA" + response.charAt(3));
                failed = ((pos < 0) || (response.charAt(pos + 7) == 'R'));
            } catch (Exception e) {
                failed = true;
            }
        }

        // call the handler, if necessary
        if (failed) {
            faultHandler.handleFailedExchange(exchange);
        }
    }
}
