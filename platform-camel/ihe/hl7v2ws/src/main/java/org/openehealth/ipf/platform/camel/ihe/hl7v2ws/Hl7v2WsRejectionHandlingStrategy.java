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

import org.apache.cxf.message.Exchange;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

/**
 * Rejection handling strategy base for HL7v2-based WS transactions.
 * @author Dmytro Rud
 */
abstract public class Hl7v2WsRejectionHandlingStrategy extends WsRejectionHandlingStrategy {

    /**
     * Specific rejection determination logic tor HL7v2-based WS transactions.
     * @param cxfExchange
     *      CXF exchange under consideration.
     * @return
     *      <code>true</code> when the exchange contains an outbound
     *      SOAP Fault or an HL7v2 NAK with code 'AR' or 'CR'.
     */
    @Override
    public boolean isRejected(Exchange cxfExchange) {
        // exchange contains SOAP fault
        if(super.isRejected(cxfExchange)) {
            return true;
        }

        // exchange does not contain a valid HL7v2 message at all
        // or does contain an HL7v2 NAK with code 'AR' or 'CR'
        String response = SoapUtils.extractOutgoingPayload(cxfExchange);
        try {
            int pos = response.indexOf("\r\nMSA");
            return ((pos < 0) || (response.charAt(pos + 7) == 'R'));
        } catch (Exception e) {
            return true;
        }
    }
}
