/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti104

import ca.uhn.fhir.rest.api.MethodOutcome
import ca.uhn.hl7v2.model.Message
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator

/**
 * Translates the acknowledgement of a HL7v2 PIX Feed [ITI-8] message into the {@link MethodOutcome} of a
 * PIXm Patient Identity Feed FHIR [ITI-104] request, or into the appropriate exception for the FHIR
 * framework in case of an error.
 * <p>
 * The patient counts as created if the ITI-8 event type was A01, A04 or A05. The event type is taken from
 * the {@link PixmFeedRequestToPixFeedTranslator#PIX_FEED_EVENT_TYPE} parameter, or else from MSH-9-2 of
 * the acknowledgement. As an acknowledgement carries no logical id of the patient, the outcome has none.
 *
 * @author Christian Ohr
 * @since 6.0
 */
class PixFeedResponseToPixmFeedResponseTranslator implements ToFhirTranslator<Message> {

    private static final Set<String> CREATE_EVENT_TYPES = ['A01', 'A04', 'A05'] as Set

    @Override
    MethodOutcome translateToFhir(Message message, Map<String, Object> parameters) {
        String ackCode = message.MSA[1].value
        switch (ackCode) {
            case 'AA':
            case 'CA':
                return handleRegularResponse(message, parameters)
            case 'AE':
            case 'CE':
                return handleErrorResponse(message)
            default:
                // AR, CR
                throw Utils.rejectedPatientFeed(errorText(message))
        }
    }

    protected static MethodOutcome handleRegularResponse(Message message, Map<String, Object> parameters) {
        String eventType = parameters?.get(PixmFeedRequestToPixFeedTranslator.PIX_FEED_EVENT_TYPE) ?: message.MSH[9][2]?.value
        MethodOutcome outcome = new MethodOutcome()
        outcome.setCreated(CREATE_EVENT_TYPES.contains(eventType))
        outcome
    }

    /**
     * Handles an error response from the Cross-reference Manager by analyzing the error location in
     * ERR-1 (HL7 v2.3.1) and throwing appropriate FHIR exceptions.
     */
    protected static MethodOutcome handleErrorResponse(Message message) {
        String segment = message.ERR[1][1]?.value
        String field = message.ERR[1][3]?.value
        if (segment == 'PID' && field == '3') {
            throw Utils.unknownIdentifierDomain()
        } else if (segment == 'MRG') {
            throw Utils.unknownSubsumedPatient()
        } else {
            throw Utils.rejectedPatientFeed(errorText(message))
        }
    }

    protected static String errorText(Message message) {
        message.MSA[3]?.value ?: message.ERR[1][4][2]?.value
    }
}
