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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55

import groovy.util.slurpersupport.GPathResult

import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Helper class for ITI-55 XCPD transaction.
 * @author Dmytro Rud
 */
abstract class Iti55Utils {
    private static final transient Logger LOG = LoggerFactory.getLogger(Iti55Utils.class)

    private Iti55Utils() {
        throw new IllegalStateException('cannot istantiate helper class')
    }


    /**
     * Returns processing mode code of the given ITI-55 request.
     * @param requestXml
     *      PRPA_IN201305UV02 request as GPath object.
     * @return
     *      request processing code
     *      ('I' for immediate, 'D' for deferred, something else otherwise).
     */
    static String processingMode(GPathResult requestXml) {
        return requestXml.controlActProcess.queryByParameter.responsePriorityCode.@code.text()
    }


    /**
     * Returns deferred response URI specified in the given ITI-55 request.
     * @param requestXml
     *      PRPA_IN201305UV02 request as GPath object.
     * @return
     *      deferred response URI, when the corresponding request attribute
     *      is present; <code>null</code> otherwise.
     */
    static String deferredResponseUri(GPathResult requestXml) {
        return requestXml.respondTo[0].telecom.@value.text().trim() ?: null
    }


    /**
     * Returns deferred response URI specified in the given ITI-55 request,
     * normalized to IPF endpoint URI format.
     * @param requestXml
     *      PRPA_IN201305UV02 request as GPath object.
     * @return
     *      deferred response URI, when the corresponding request attribute
     *      is present and could be normalized; <code>null</code> otherwise.
     */
    static String normalizedDeferredResponseUri(GPathResult requestXml) {
        String uri = deferredResponseUri(requestXml)
        if (! uri) {
            return null
        }

        final String prefix = 'xcpd-iti55-deferred-response://'
        if (uri.toLowerCase().startsWith('http://')) {
            return "${prefix}${uri.substring(7)}"
        } else if (uri.toLowerCase().startsWith('https://')) {
            String separator = uri.contains('?') ? '&' : '?'
            return "${prefix}${uri.substring(8)}${separator}secure=true"
        } else {
            return null
        }
    }


    /**
     * Determines whether the given response message represents a positive ACK.
     * Positive ACKs should be ignored in the context of both ATNA auditing and
     * asynchrony correlation.
     *
     * @param responseString
     *      response message as XML string.
     * @return
     *      <code>true</code> when the given message uis a positive MCCI ACK.
     */
    static boolean isMcciAck(String responseString) {
        GPathResult responseXml = Hl7v3Utils.slurp(responseString)
        return (responseXml.name() == 'MCCI_IN000002UV01') &&
               (responseXml.acknowledgement.typeCode.@code.text() in ['AA', 'CA'])
    }


    /**
     * @param requestXml
     *      parsed request message.
     * @return
     *      query ID from the request.
     */
    static String requestQueryId(GPathResult requestXml) {
        return Hl7v3Utils.idString(requestXml.controlActProcess.queryByParameter.queryId)
    }


    /**
     * @param responseXml
     *      parsed response message.
     * @return
     *      query ID from the response.
     */
    static String responseQueryId(GPathResult responseXml) {
        return Hl7v3Utils.idString(responseXml.controlActProcess.queryAck.queryId)
    }
}
