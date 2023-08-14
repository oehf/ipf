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

import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.hl7.fhir.r4.model.Consent
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants
import org.openehealth.ipf.commons.xml.XmlUtils

import java.text.SimpleDateFormat

class XacmlToFhirTranslator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat('yyyy-MM-dd')

    static Consent toConsent(PolicySetType policySet) {
        String xacmlString = XmlUtils.renderJaxb(Xacml20Utils.JAXB_CONTEXT, policySet, false)
        GPathResult xacml = new XmlSlurper(false, true).parseText(xacmlString)

        String id = xacml.@PolicySetId.text().trim()
        String policyIdReference = xacml.PolicySetIdReference.text().trim()
        String eprSpid = xacml.Target.Resources.Resource.ResourceMatch
                .find { it.@MatchId == 'urn:hl7-org:v3:function:II-equal' }
                .AttributeValue.InstanceIdentifier.@extension.text().trim()

        Date startDate = parseDate(xacml, 'urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal')
        Date endDate = parseDate(xacml, 'urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal')

        if (xacml.Target.Subjects.Subject.size() == 3) {
            return create203Consent(id, eprSpid, policyIdReference)
        }

        GPathResult subjectMatches = xacml.Target.Subjects.Subject.SubjectMatch
        for (subjectMatch in (subjectMatches)) {
            def cv = subjectMatch.AttributeValue.CodedValue

            if ((cv.@codeSystem.text() == PpqConstants.CodingSystemIds.SWISS_PURPOSE_OF_USE) && (cv.@code.text() == 'EMER')) {
                return create202Consent(id, eprSpid, policyIdReference)
            }

            if (cv.@codeSystem.text() == PpqConstants.CodingSystemIds.SWISS_SUBJECT_ROLE) {
                if (cv.@code.text() == 'PAT') {
                    return create201Consent(id, eprSpid)
                }
                if (cv.@code.text() == 'REP') {
                    String representativeId = extractAttributeValue(subjectMatches, 'urn:oasis:names:tc:xacml:1.0:subject:subject-id')
                    return create303Consent(id, eprSpid, representativeId, startDate, endDate)
                }
            }
        }

        def groupId = extractAttributeValue(subjectMatches, 'urn:oasis:names:tc:xspa:1.0:subject:organization-id')
        if (groupId) {
            return create302Consent(id, eprSpid, groupId, policyIdReference, startDate, endDate)
        }

        String gln = extractAttributeValue(subjectMatches, 'urn:oasis:names:tc:xacml:1.0:subject:subject-id')
        return create301Consent(id, eprSpid, gln, policyIdReference, startDate, endDate)
    }

    private static Date parseDate(GPathResult xacml, String matchId) {
        def s = xacml.Target.Environments.Environment.EnvironmentMatch.find { it.@MatchId.text() == matchId }.AttributeValue.text()
        return s ? DATE_FORMAT.parse(s) : null
    }

    private static String extractAttributeValue(GPathResult subjectMatches, String designator) {
        return subjectMatches.find { it.SubjectAttributeDesignator.@AttributeId.text() == designator }.AttributeValue.text()
    }

}
