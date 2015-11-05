/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Message
import org.apache.camel.Exchange
import org.apache.camel.support.ExpressionAdapter
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23
import org.openehealth.ipf.modules.hl7.HL7v2Exception
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 *
 */
class Iti9Responder extends ExpressionAdapter {

    private final AcknowledgmentCode ackCode;
    private final String responseCode;

    static final String RESULT_VALUE = "4711";
    static final String REQUESTED_OID = "1.2.3.4.5.6";

    Iti9Responder(AcknowledgmentCode ackCode, String responseCode) {
        this.ackCode = ackCode
        this.responseCode = responseCode
    }

    @Override
    Object evaluate(Exchange exchange) {

        try {
            Message request = exchange.getIn().getBody(Message.class);
            RSP_K23 rsp = MessageUtils.response(request, "RSP", "K23");

            rsp.MSA[1] = ackCode.name();
            rsp.QAK[1] = request.QPD[2].value ?: ''
            rsp.QAK[2] = responseCode
            rsp.QPD = request.QPD

            if (ackCode != AcknowledgmentCode.AA) {
                // add ERR segment
            } else if ("OK".equals(responseCode)) {
                rsp.QUERY_RESPONSE.PID[3](0)[1] = RESULT_VALUE
                rsp.QUERY_RESPONSE.PID[3](0)[4][1] = "namespace"
                rsp.QUERY_RESPONSE.PID[3](0)[4][2] = REQUESTED_OID
                rsp.QUERY_RESPONSE.PID[3](0)[4][3] = "ISO"
                rsp.QUERY_RESPONSE.PID[3](0)[5] = "PI"

                rsp.QUERY_RESPONSE.PID.nrp(5)
                rsp.QUERY_RESPONSE.PID.nrp(5)
                rsp.QUERY_RESPONSE.PID[5](1)[7].value = 'S'
            }
            return rsp;
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }

    }
}
