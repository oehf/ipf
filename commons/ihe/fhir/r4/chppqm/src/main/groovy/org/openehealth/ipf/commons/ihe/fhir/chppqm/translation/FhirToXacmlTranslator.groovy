/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm.translation

import org.apache.commons.lang3.StringUtils
import org.apache.http.NameValuePair
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.velocity.VelocityContext
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Consent
import org.hl7.fhir.r4.model.Patient
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils
import org.openehealth.ipf.commons.ihe.xacml20.ChPpqMessageCreator
import org.openehealth.ipf.commons.ihe.xacml20.ChPpqPolicySetCreator
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AssertionBasedRequestType
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType

import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

class FhirToXacmlTranslator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat('yyyy-MM-dd')

    private final ChPpqMessageCreator ppqMessageCreator

    FhirToXacmlTranslator(ChPpqMessageCreator ppqMessageCreator) {
        this.ppqMessageCreator = ppqMessageCreator
    }

    /**
     * Translates a single FHIR Consent to a XACML Policy Set.
     */
    PolicySetType toPolicySet(Consent consent) {
        VelocityContext substitutions = new VelocityContext()
        substitutions.put('id', consent.identifier[0].value)
        substitutions.put('eprSpid', consent.patient.identifier.value)
        substitutions.put('policyIdReference', consent.policyRule.coding[0].code)

        def startDate = consent.provision.period?.start
        if (startDate) {
            substitutions.put('startDate', DATE_FORMAT.format(startDate))
        }

        def endDate = consent.provision.period?.end
        if (endDate) {
            substitutions.put('endDate', DATE_FORMAT.format(endDate))
        }

        def templateId = ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.TEMPLATE_ID)
        switch (templateId) {
            case '301':
            case '304':
                substitutions.put('gln', consent.provision.actor[0].reference.identifier.value)
                break
            case '302':
                substitutions.put('groupId', consent.provision.actor[0].reference.identifier.value)
                break
            case '303':
                substitutions.put('representativeId', consent.provision.actor[0].reference.identifier.value)
                break
        }

        return ChPpqPolicySetCreator.createPolicySet(templateId, substitutions)
    }

    /**
     * Translates a CH:PPQ-3 request into a CH:PPQ-1 request.
     *
     * @param request Consent instance for PUT and POST, String for DELETE
     * @param method HTTP method
     * @return PPQ-1 request POJO
     */
    <T extends AssertionBasedRequestType> T translatePpq3To1Request(Object request, String method) {
        if (method == 'DELETE') {
            return (T) ppqMessageCreator.createDeletePolicyRequest([request as String])
        }
        def policySet = toPolicySet(request as Consent)
        switch (method) {
            case 'POST':
                return (T) ppqMessageCreator.createAddPolicyRequest([policySet])
            case 'PUT':
                return (T) ppqMessageCreator.createUpdatePolicyRequest([policySet])
            default:
                throw new Exception('Unsupported method: ' + method)
        }
    }

    /**
     * Translates a CH:PPQ-4 request into a CH:PPQ-1 request.
     *
     * @param bundle request bundle
     * @return PPQ-1 request POJO
     */
    <T extends AssertionBasedRequestType> T translatePpq4To1Request(Bundle bundle) {
        if ((bundle == null) || bundle.entry.empty) {
            return null
        }
        def method = bundle.entry[0].request.method
        if (method == Bundle.HTTPVerb.DELETE) {
            return (T) ppqMessageCreator.createDeletePolicyRequest(ChPpqmUtils.extractConsentIdsFromEntryUrls(bundle))
        }
        def policySets = bundle.entry.collect { toPolicySet(it.getResource() as Consent) }
        switch (method) {
            case Bundle.HTTPVerb.POST:
                return (T) ppqMessageCreator.createAddPolicyRequest(policySets)
            case Bundle.HTTPVerb.PUT:
                return (T) ppqMessageCreator.createUpdatePolicyRequest(policySets)
            default:
                throw new Exception('Unsupported method: ' + method)
        }
    }

    /**
     * Translates a CH:PPQ-5 request into a CH:PPQ-2 request.
     *
     * @param httpQuery HTTP query string
     * @return PPQ-2 request POJO
     */
    XACMLPolicyQueryType translatePpq5To2Request(String httpQuery) {
        List<NameValuePair> params = URLEncodedUtils.parse(httpQuery, StandardCharsets.UTF_8)
        def policySetId = params.find { it.name == Consent.SP_IDENTIFIER }?.value
        if (policySetId) {
            return ppqMessageCreator.createPolicyQuery([policySetId])
        }
        def patientId = params.find { it.name == Consent.SP_PATIENT + ':' + Patient.SP_IDENTIFIER }?.value
        if (patientId) {
            def fragments = StringUtils.split(patientId, '|' as char)
            return ppqMessageCreator.createPolicyQuery(new II(
                    extension: fragments[0],
                    root: fragments[1].substring(8),
            ))
        }
        throw new Exception('Either policy set ID or patient ID shall be specified')
    }


}