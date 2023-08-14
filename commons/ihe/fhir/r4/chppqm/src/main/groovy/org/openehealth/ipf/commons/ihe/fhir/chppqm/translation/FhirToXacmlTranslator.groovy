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

import org.apache.velocity.VelocityContext
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Consent
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils
import org.openehealth.ipf.commons.ihe.xacml20.ChPpqMessageCreator
import org.openehealth.ipf.commons.ihe.xacml20.ChPpqPolicySetCreator
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AssertionBasedRequestType

import java.text.SimpleDateFormat

class FhirToXacmlTranslator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat('yyyy-MM-dd')

    private ChPpqMessageCreator ppqMessageCreator

    FhirToXacmlTranslator(String homeCommunityId) {
        this.ppqMessageCreator = new ChPpqMessageCreator(homeCommunityId)
    }

    static PolicySetType toPolicySet(Consent consent) {
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
     * Converts a CH:PPQ-4 request to a CH:PPQ-1 request.
     */
    AssertionBasedRequestType toPpq1Request(Bundle bundle) {
        if ((bundle == null) || bundle.entry.empty) {
            return null
        }
        def method = bundle.entry[0].request.method

        if (method == Bundle.HTTPVerb.DELETE) {
            return ppqMessageCreator.createDeletePolicyRequest(ChPpqmUtils.extractConsentIdsFromEntryUrls(bundle))
        }

        def policySets = bundle.entry.collect { toPolicySet(it.resource as Consent) }

        switch (method) {
            case Bundle.HTTPVerb.POST:
                return ppqMessageCreator.createAddPolicyRequest(policySets)
            case Bundle.HTTPVerb.PUT:
                return ppqMessageCreator.createUpdatePolicyRequest(policySets)
            default:
                throw new Exception('Unknown method: ' + method)
        }
    }

}