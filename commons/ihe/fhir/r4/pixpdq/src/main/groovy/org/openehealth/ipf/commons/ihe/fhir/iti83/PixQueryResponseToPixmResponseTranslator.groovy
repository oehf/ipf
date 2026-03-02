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
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Reference
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersOut
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23

import static java.util.Objects.requireNonNull

/**
 * Translates HL7v2 PIX Query Response message into a {@link org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersOut} resource.
 * Also cares about error responses and throws the appropriate Exceptions for the
 * FHIR framework.
 *
 * @author Christian Ohr
 * @since 3.6
 */
class PixQueryResponseToPixmResponseTranslator implements ToFhirTranslator<Message> {

    private final UriMapper uriMapper
    String pixSupplierResourceIdentifierUri

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PixQueryResponseToPixmResponseTranslator(UriMapper uriMapper) {
        requireNonNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    @Override
    PixmQueryParametersOut translateToFhir(Message message, Map<String, Object> parameters) {
        String ackCode = message.QAK[2].value
        switch (ackCode) {
            case 'OK': return handleRegularResponse(message.QUERY_RESPONSE.PID[3]()) // Case 1
            case 'NF': return handleEmptyResponse()  // Case 2
            case 'AE': return handleErrorResponse(message) // Cases 3-5
            default: throw new InternalErrorException("Unexpected ack code " + ackCode)
        }
    }

    /**
     * Handles a regular response by converting PID-3 identifiers to target identifiers.
     *
     * @param pid3collection collection of PID-3 identifier components
     * @return PixmQueryParametersOut with target identifiers and optional target IDs
     */
    protected PixmQueryParametersOut handleRegularResponse(pid3collection) {
        PixmQueryParametersOut parametersOut = new PixmQueryParametersOut()
        if (pid3collection) {
            for (pid3 in pid3collection) {
                Identifier identifier = new Identifier()
                        .setSystem(uriMapper.oidToUri(pid3[4][2]?.value))
                        .setValue(pid3[1].value)
                        .setUse(Identifier.IdentifierUse.OFFICIAL)
                
                parametersOut.addTargetIdentifier(identifier)
                
                // If this identifier matches the resource identifier system, also add a targetId reference
                if (pixSupplierResourceIdentifierUri && identifier.system == pixSupplierResourceIdentifierUri) {
                    parametersOut.addTargetId(new Reference("Patient/${identifier.value}"))
                }
            }
        }
        parametersOut
    }

    /**
     * Handles an empty response (no matching identifiers found).
     *
     * @return empty PixmQueryParametersOut
     */
    protected PixmQueryParametersOut handleEmptyResponse() {
        return handleRegularResponse(null)
    }

    /**
     * Handles an error response from the Cross-reference manager by analyzing
     * the ERR segment and throwing appropriate FHIR exceptions.
     *
     * @param message the RSP_K23 error response message
     */
    protected static PixmQueryParametersOut handleErrorResponse(RSP_K23 message) {
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
            throw Utils.unexpectedProblem()
        }
    }

}