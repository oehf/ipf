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

import ca.uhn.hl7v2.HapiContext
import org.apache.commons.lang3.Validate
import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.Parameters
import org.hl7.fhir.instance.model.UriType
import org.hl7.fhir.instance.model.api.IBaseResource
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.Utils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Translates a {@link IBaseResource} into a HL7v2 PIX Query message
 *
 * @since 3.1
 */
class PixmRequestToPixQueryTranslator implements TranslatorFhirToHL7v2 {

    /**
     * Predefined fix value of QPD-1 (as String)
     */
    String queryName = 'IHE PIX Query'
    String senderDeviceName = 'unknown'
    String senderFacilityName = 'unknown'
    String receiverDeviceName = 'unknown'
    String receiverFacilityName = 'unknown'

    private final UriMapper uriMapper;

    private static final HapiContext PIX_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pix", "2.5"),
            PixPdqTransactions.ITI9)

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PixmRequestToPixQueryTranslator(UriMapper uriMapper) {
        Validate.notNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    @Override
    QBP_Q21 translateFhirToHL7v2(IBaseResource request, Map<String, Object> parameters) {
        Parameters inParams = (Parameters)request;
        QBP_Q21 qry = MessageUtils.makeMessage(PIX_QUERY_CONTEXT, 'QBP', 'Q23', '2.5')

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

        Identifier sourceIdentifier = map[Constants.SOURCE_IDENTIFIER_NAME]
        populateIdentifier(qry.QPD[3], sourceIdentifier.system, sourceIdentifier.value)

        UriType requestedDomain = map[Constants.TARGET_SYSTEM_NAME]
        if (requestedDomain) {
            populateIdentifier(Utils.nextRepetition(qry.QPD[4]), requestedDomain.value)
        }

        qry.RCP[1] = 'I'
        return qry
    }

    protected void populateIdentifier(def cx, String uri, String identifier = null) {
        cx[1] = identifier ?: ''
        cx[4][2] = uriMapper.uriToOid(uri)
        cx[4][3] = 'ISO'
    }

}