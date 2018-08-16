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

import ca.uhn.hl7v2.model.Message
import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.Parameters
import org.hl7.fhir.instance.model.UriType
import org.hl7.fhir.instance.model.api.IBaseResource
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.PIX
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21

import static java.util.Objects.requireNonNull

/**
 * Translates a {@link IBaseResource} into a HL7v2 PIX Query message
 *
 * @author Christian Ohr
 * @since 3.1
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
     * @param pdqSupplierResourceIdentifierUri the URI of the resource identifier system
     */
    void setPixSupplierResourceIdentifierUri(String pixSupplierResourceIdentifierUri) {
        requireNonNull(pixSupplierResourceIdentifierUri, "Resource Identifier URI must not be null")
        this.pixSupplierResourceIdentifierUri = pixSupplierResourceIdentifierUri
    }

    @Override
    QBP_Q21 translateFhir(Object request, Map<String, Object> parameters) {
        Parameters inParams = (Parameters) request
        QBP_Q21 qry = PIX.QueryInteractions.ITI_9.hl7v2TransactionConfiguration.request('Q23')

        qry.MSH[3] = senderDeviceName
        qry.MSH[4] = senderFacilityName
        qry.MSH[5] = receiverDeviceName
        qry.MSH[6] = receiverFacilityName

        qry.MSH[7] = Utils.hl7Timestamp()
        qry.MSH[10] = UUID.randomUUID().toString()

        qry.QPD[1] = this.queryName
        qry.QPD[2] = UUID.randomUUID().toString()

        Map<String, Object> map = new HashMap<>()
        List<Parameters.ParametersParameterComponent> parts = inParams.parameter
        for (Parameters.ParametersParameterComponent part : parts) {
            map.put(part.name, part.value)
        }

        handleSourceIdentifier(qry, map[Constants.SOURCE_IDENTIFIER_NAME])

        UriType requestedDomain = map[Constants.TARGET_SYSTEM_NAME]
        if (requestedDomain) {
            if (!Utils.populateIdentifier(Utils.nextRepetition(qry.QPD[4]), uriMapper, requestedDomain.value)) {
                // UriMapper is not able to derive a PIX OID/Namespace for the target domain URI, Error Case 5
                throw Utils.unknownTargetDomainCode(requestedDomain.value)
            }
        }

        qry.RCP[1] = 'I'
        return qry
    }

    protected void handleSourceIdentifier(QBP_Q21 qry, Identifier sourceIdentifier) {
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