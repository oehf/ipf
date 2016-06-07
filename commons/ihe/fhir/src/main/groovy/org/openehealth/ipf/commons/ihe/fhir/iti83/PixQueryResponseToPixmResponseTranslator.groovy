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
package org.openehealth.ipf.commons.ihe.fhir.iti83

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException
import ca.uhn.hl7v2.model.Message
import org.apache.commons.lang3.Validate
import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.OperationOutcome
import org.hl7.fhir.instance.model.Parameters
import org.openehealth.ipf.commons.ihe.fhir.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23

/**
 * Translates HL7v2 PIX Query Response message into a {@link Parameters} resource
 * Also cares about error responses and throws the appropriate Exceptions for the
 * FHIR framework
 *
 * @author Christian Ohr
 * @since 3.1
 */
class PixQueryResponseToPixmResponseTranslator implements TranslatorHL7v2ToFhir {

    private final UriMapper uriMapper

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PixQueryResponseToPixmResponseTranslator(UriMapper uriMapper) {
        Validate.notNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    @Override
    Parameters translateHL7v2ToFhir(Message message, Map<String, Object> parameters) {
        String ackCode = message.QAK[2].value
        switch (ackCode) {
            case 'OK': return handleRegularResponse(message.QUERY_RESPONSE.PID[3]()) // Case 1
            case 'NF': return handleEmptyResponse()  // Case 2
            case 'AE': return handleErrorResponse(message) // Cases 3-5
            default: throw new InternalErrorException("Unexpected ack code " + ackCode)
        }
    }

    // Handle a regular response
    private Parameters handleRegularResponse(pid3collection) {
        Parameters parameters = new Parameters()
        if (pid3collection) {
            for (pid3 in pid3collection) {
                Identifier identifier = new Identifier()
                        .setSystem(uriMapper.oidToUri(pid3[4][2]?.value))
                        .setValue(pid3[1].value)
                        .setUse(Identifier.IdentifierUse.OFFICIAL)
                parameters.addParameter()
                        .setName('targetIdentifier')
                        .setValue(identifier)
            }
        }
        parameters
    }

    // Handle an empty response
    private Parameters handleEmptyResponse() {
        return handleRegularResponse(null)
    }

    // Handle an error response from the Cross-reference manager
    private Parameters handleErrorResponse(RSP_K23 message) {

        // Check error locations
        int errorField = message.ERR[2][3]?.value ? Integer.parseInt(message.ERR[2][3]?.value) : 0
        int errorComponent = message.ERR[2][5]?.value ? Integer.parseInt(message.ERR[2][5]?.value) : 0

        if (errorField == 3 && errorComponent == 1) {
            // Case 3: Patient ID not found
            throw Utils.unknownPatientId()
        } else if (errorField == 3 && errorComponent == 4) {
            // Case 4: Unknown Patient Domain
            throw Utils.unknownSourceDomainCode()
        } else if (errorField == 4) {
            // Case 5: Unknown Target Domain
            throw Utils.unknownTargetDomainCode()
        } else {
            throw Utils.unexpectedProblem();
        }

    }

}