/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.model.Message
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 *
 */
enum ResponseCase {

    OK(AcknowledgmentCode.AA, "OK") {
        @Override
        RSP_K21 doPopulateResponse(RSP_K21 rsp, String namespace, String oid) {

            rsp.QUERY_RESPONSE.with {
                PID[3](0)[1] = "4711"
                PID[3](0)[4][1] = namespace
                PID[3](0)[4][2] = oid
                PID[3](0)[4][3] = "ISO"
                PID[3](0)[5] = "PI"

                PID[5][1] = 'Test'
                PID[5][2] = 'Patient'
                PID[5][7] = 'L'
                PID[6][1] = 'Maiden'
                PID[6][7] = 'L'

                PID[7] = '19400101'
                PID[8] = 'M'
            }
            rsp
        }
    },

    OK_MANY(AcknowledgmentCode.AA, "OK") {
        @Override
        RSP_K21 doPopulateResponse(RSP_K21 rsp, String namespace, String oid) {

            rsp.QUERY_RESPONSE(0).with {
                PID[3](0)[1] = "4711"
                PID[3](0)[4][1] = namespace
                PID[3](0)[4][2] = oid
                PID[3](0)[4][3] = "ISO"
                PID[3](0)[5] = "PI"

                PID[5][1] = 'Test2'
                PID[5][2] = 'Zed'
                PID[5][7] = 'L'
                PID[6][1] = 'Maiden'
                PID[6][7] = 'L'

                PID[7] = '19400101'
                PID[8] = 'M'
            }
            rsp.QUERY_RESPONSE(1).with {
                PID[3](0)[1] = "0815"
                PID[3](0)[4][1] = namespace
                PID[3](0)[4][2] = oid
                PID[3](0)[4][3] = "ISO"
                PID[3](0)[5] = "PI"

                PID[5][1] = 'Test2'
                PID[5][2] = 'Hilmar'
                PID[5][7] = 'L'
                PID[6][1] = 'Maiden'
                PID[6][7] = 'L'

                PID[7] = '19400101'
                PID[8] = 'M'
            }
            rsp.QUERY_RESPONSE(2).with {
                PID[3](0)[1] = "9999"
                PID[3](0)[4][1] = namespace
                PID[3](0)[4][2] = oid
                PID[3](0)[4][3] = "ISO"
                PID[3](0)[5] = "PI"

                PID[5][1] = 'Test1'
                PID[5][2] = 'Peter'
                PID[5][7] = 'L'
                PID[6][1] = 'Maiden'
                PID[6][7] = 'L'

                PID[7] = '19400101'
                PID[8] = 'M'
            }
            rsp
        }
    },

    UNKNOWN_TARGET_AUTHORITY(AcknowledgmentCode.AE, "AE") {
        @Override
        RSP_K21 doPopulateResponse(RSP_K21 rsp, String namespace, String oid) {
            rsp.ERR[2][1] = "QPD"
            rsp.ERR[2][2] = "1"
            rsp.ERR[2][3] = "8"
            rsp.ERR[2][4] = "1"
            rsp.ERR[3] = "204"
            rsp.ERR[4] = "E"
            rsp.ERR[6] = "Unknown Key identifier"
            rsp
        }
    }

    static final String REQUESTED_OID = "1.2.3.4.5.6"
    static final String REQUESTED_NAMESPACE = "namespace"

    private AcknowledgmentCode ackCode
    private String responseCode

    ResponseCase(AcknowledgmentCode ackCode, String responseCode) {
        this.ackCode = ackCode
        this.responseCode = responseCode
    }

    RSP_K21 populateResponse(Message request) {
        populateResponse(request, REQUESTED_NAMESPACE, REQUESTED_OID)
    }

    RSP_K21 populateResponse(Message request, String namespace, String oid) {
        RSP_K21 rsp = MessageUtils.response(request, "RSP", "K22")
        rsp.MSA[1] = ackCode.name()
        rsp.QAK[1] = request.QPD[2].value ?: ''
        rsp.QAK[2] = responseCode
        rsp.QPD = request.QPD
        doPopulateResponse(rsp, namespace, oid)
    }

    abstract RSP_K21 doPopulateResponse(RSP_K21 rsp, String namespace, String oid)

}