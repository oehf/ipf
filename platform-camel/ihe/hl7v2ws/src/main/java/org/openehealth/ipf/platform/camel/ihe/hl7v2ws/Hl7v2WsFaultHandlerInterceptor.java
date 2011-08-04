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
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;


/**
 * CXF interceptor which sends all failed CXF exchanges and all
 * exchanges which contain HL7v2 NAKs with code 'AR' or 'CR'
 * to the user-defined failure handler.
 * <p>
 * Usable in outgoing chains (normal and fault) on server side.
 *
 * @author Dmytro Rud
 */
public class Hl7v2WsFaultHandlerInterceptor extends AbstractSoapInterceptor {
    private final Hl7v2WsFailureHandler faultHandler;

    public Hl7v2WsFaultHandlerInterceptor(Hl7v2WsFailureHandler faultHandler) {
        super(Phase.MARSHAL);
        Validate.notNull(faultHandler);
        this.faultHandler = faultHandler;
    }


    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Message wrappedMessage = message.getMessage();
        Exchange exchange = wrappedMessage.getExchange();

        // exchange contains SOAP fault
        boolean failed = (SoapUtils.extractOutgoingException(exchange) != null);

        // exchange does not contain a valid HL7v2 message at all
        // or does contain an HL7v2 NAK with code 'AR' or 'CR'
        if (! failed) {
            String response = SoapUtils.extractOutgoingPayload(exchange);
            try {
                int pos = response.indexOf("\r\nMSA");
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
