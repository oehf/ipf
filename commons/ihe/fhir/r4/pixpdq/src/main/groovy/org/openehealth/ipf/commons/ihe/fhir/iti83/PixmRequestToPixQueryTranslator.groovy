/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti83

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.hl7v2.model.Message
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.r4.model.StringType
import org.hl7.fhir.r4.model.UriType
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersIn
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.PIX
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21

import static java.util.Objects.requireNonNull

/**
 * Translates a {@link org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersIn} into a HL7v2 PIX Query message
 *
 * @author Christian Ohr
 * @since 3.6
 */
class PixmRequestToPixQueryTranslator implements FhirTranslator<Message> {

    /**
     * Predefined fix value of QPD-1 (as String)
     */
    String queryName = 'IHE PIX Query'
    String senderDeviceName = 'unknown'
    String senderFacilityName = 'unknown'
    String receiverDeviceName = 'unknown'
    String receiverFacilityName = 'unknown'

    String pixSupplierResourceIdentifierUri

    private final UriMapper uriMapper

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PixmRequestToPixQueryTranslator(UriMapper uriMapper) {
        requireNonNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    /**
     * @param pixSupplierResourceIdentifierUri the URI of the resource identifier system
     */
    void setPixSupplierResourceIdentifierUri(String pixSupplierResourceIdentifierUri) {
        requireNonNull(pixSupplierResourceIdentifierUri, "Resource Identifier URI must not be null")
        this.pixSupplierResourceIdentifierUri = pixSupplierResourceIdentifierUri
    }

    @Override
    QBP_Q21 translateFhir(Object request, Map<String, Object> parameters) {
        PixmQueryParametersIn inParams = convertToPixmQueryParametersIn(request)
        QBP_Q21 qry = PIX.QueryInteractions.ITI_9.hl7v2TransactionConfiguration.request('Q23')

        qry.MSH[3] = senderDeviceName
        qry.MSH[4] = senderFacilityName
        qry.MSH[5] = receiverDeviceName
        qry.MSH[6] = receiverFacilityName

        qry.MSH[7] = Utils.hl7Timestamp()
        qry.MSH[10] = UUID.randomUUID().toString()

        qry.QPD[1] = this.queryName
        qry.QPD[2] = UUID.randomUUID().toString()

        handleSourceIdentifier(qry, inParams.sourceIdentifierAsIdentifier)

        var targetSystems = inParams.targetSystems
        if (targetSystems && !targetSystems.isEmpty()) {
            targetSystems.eachWithIndex { targetSystem, index ->
                if (!Utils.populateIdentifier(Utils.nextRepetition(qry.QPD[4]), uriMapper, targetSystem)) {
                    // UriMapper is not able to derive a PIX OID/Namespace for the target domain URI, Error Case 5
                    throw Utils.unknownTargetDomainCode(targetSystem)
                }
            }
        }

        qry.RCP[1] = 'I'
        return qry
    }

    /**
     * Converts a generic Parameters object or PixmQueryParametersIn to PixmQueryParametersIn.
     * This allows backward compatibility with code that uses generic Parameters.
     *
     * @param request the request object (Parameters or PixmQueryParametersIn)
     * @return PixmQueryParametersIn instance
     */
    protected PixmQueryParametersIn convertToPixmQueryParametersIn(Object request) {
        if (request instanceof PixmQueryParametersIn) {
            return (PixmQueryParametersIn) request
        }
        
        if (request instanceof Parameters) {
            Parameters params = (Parameters) request
            PixmQueryParametersIn pixmParams = new PixmQueryParametersIn()
            
            // Extract sourceIdentifier parameter
            params.parameter.each { param ->
                if (PixmQueryParametersIn.SOURCE_IDENTIFIER == param.name) {
                    if (param.value instanceof StringType) {
                        pixmParams.setSourceIdentifier(((StringType) param.value).value)
                    } else if (param.value instanceof Identifier) {
                        pixmParams.setSourceIdentifier((Identifier) param.value)
                    }
                } else if (PixmQueryParametersIn.TARGET_SYSTEM == param.name) {
                    if (param.value instanceof UriType) {
                        pixmParams.addTargetSystem(((UriType) param.value).value)
                    }
                } else if (PixmQueryParametersIn._FORMAT == param.name) {
                    if (param.value instanceof StringType) {
                        pixmParams.setFormat(((StringType) param.value).value)
                    }
                }
            }
            
            return pixmParams
        }
        
        throw new InvalidRequestException("Request must be either Parameters or PixmQueryParametersIn, but was: ${request?.class?.name}")
    }

    protected void handleSourceIdentifier(QBP_Q21 qry, Identifier sourceIdentifier) {
        if (!sourceIdentifier) {
            throw new InvalidRequestException("sourceIdentifier parameter is required")
        }
        Identifier id = sourceIdentifier.copy()

        if (!id.system) {
            // No system provided? Assume that we are looking for PIX Manager identifiers
            id.system = pixSupplierResourceIdentifierUri
        }
        if (!id.value) {
            // No value provided? Patient cannot be found, Error Case 3
            throw Utils.unknownPatientId()
        }
        if (!Utils.populateIdentifier(qry.QPD[3], uriMapper, id.system, id.value)) {
            // UriMapper is not able to derive a PIX OID/Namespace for the patient domain URI, Error Case 4
            throw Utils.unknownSourceDomainCode(id.system)
        }

        id
    }

}