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

import ca.uhn.fhir.rest.api.MethodOutcome
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import org.apache.cxf.binding.soap.Soap12
import org.apache.cxf.binding.soap.SoapFault
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.Soap12FaultOutInterceptor
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Consent
import org.hl7.fhir.r4.model.IdType
import org.hl7.fhir.r4.model.OperationOutcome
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AssertionBasedRequestType
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLPolicyStatementType
import org.openehealth.ipf.commons.xml.XmlUtils

import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*

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
        for (subjectMatch in subjectMatches) {
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
        return policyIdReference.contains('delegation')
            ? create304Consent(id, eprSpid, gln, policyIdReference, startDate, endDate)
            : create301Consent(id, eprSpid, gln, policyIdReference, startDate, endDate)
    }

    private static Date parseDate(GPathResult xacml, String matchId) {
        def s = xacml.Target.Environments.Environment.EnvironmentMatch.find { it.@MatchId.text() == matchId }.AttributeValue.text()
        return s ? DATE_FORMAT.parse(s) : null
    }

    private static String extractAttributeValue(GPathResult subjectMatches, String designator) {
        return subjectMatches.find { it.SubjectAttributeDesignator.@AttributeId.text() == designator }.AttributeValue.text()
    }

    private static OperationOutcome positiveOperationOutcome() {
        return new OperationOutcome(
                issue: [
                        new OperationOutcome.OperationOutcomeIssueComponent(
                                severity: OperationOutcome.IssueSeverity.INFORMATION,
                                code: OperationOutcome.IssueType.INFORMATIONAL,
                        ),
                ],
        )
    }

    private static OperationOutcome negativeOperationOutcome() {
        return new OperationOutcome(
                issue: [
                        new OperationOutcome.OperationOutcomeIssueComponent(
                                severity: OperationOutcome.IssueSeverity.ERROR,
                                code: OperationOutcome.IssueType.PROCESSING,
                        ),
                ],
        )
    }

    /**
     * Translates a CH:PPQ-1 response into a CH:PPQ-3 response.
     */
    static MethodOutcome translatePpq1To3Response(
            Consent ppq3Request,
            AssertionBasedRequestType ppq1Request,
            EprPolicyRepositoryResponse ppq1Response)
    {
        if (ppq1Response.status == PpqConstants.StatusCode.SUCCESS) {
            return new MethodOutcome(
                    id: new IdType(UUID.randomUUID().toString()),
                    responseStatusCode: (ppq1Request instanceof AddPolicyRequest) ? 201 : 200,
                    resource: ppq3Request,
                    created: (ppq1Request instanceof AddPolicyRequest),
                    operationOutcome: positiveOperationOutcome(),
            )
        } else {
            throw new UnclassifiedServerFailureException(400, 'PPQ-1 request failed', negativeOperationOutcome())
        }
    }

    static final Map<String, OperationOutcome.IssueType> SOAP_FAULT_CODE_TO_FHIR_ISSUE_TYPE_CODE_MAPPING = [
            'VersionMismatch'    : OperationOutcome.IssueType.STRUCTURE,
            'MustUnderstand'     : OperationOutcome.IssueType.NOTSUPPORTED,
            'DataEncodingUnknown': OperationOutcome.IssueType.STRUCTURE,
            'Sender'             : OperationOutcome.IssueType.INVALID,
            'Receiver'           : OperationOutcome.IssueType.TRANSIENT,
    ]

    static final Map<String, Integer> SOAP_FAULT_CODE_TO_HTTP_STATUS_CODE_MAPPING = [
            'VersionMismatch'    : 500,
            'MustUnderstand'     : 500,
            'DataEncodingUnknown': 500,
            'Sender'             : 400,
            'Receiver'           : 503,
    ]

    private static String serializeSoapFault(SoapFault soapFault) {
        // inspired by org.apache.cxf.binding.soap.interceptor.SoapFaultSerializerTest
        SoapMessage soapMessage = new SoapMessage(Soap12.instance)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream)
        soapMessage.setContent(XMLStreamWriter.class, writer)
        soapMessage.setContent(Exception.class, soapFault)
        new Soap12FaultOutInterceptor.Soap12FaultOutInterceptorInternal().handleMessage(soapMessage)
        writer.close()
        return outputStream.toString(StandardCharsets.UTF_8)
    }

    private static translateNegativeResponse(
            String key,
            String message,
            Map<String, Integer> statusCodeMap,
            Map<String, OperationOutcome.IssueType> issueTypeMap,
            String diagnostics)
    {
        throw new UnclassifiedServerFailureException(
                statusCodeMap.getOrDefault(key, 500),
                message,
                new OperationOutcome(
                        issue: [
                                new OperationOutcome.OperationOutcomeIssueComponent(
                                        severity: OperationOutcome.IssueSeverity.ERROR,
                                        code: issueTypeMap.getOrDefault(key, OperationOutcome.IssueType.UNKNOWN),
                                        diagnostics: diagnostics,
                                ),
                        ],
                )
        )
    }

    /**
     * Rethrows a generic SOAP Fault as a FHIR exception.
     */
    static void translateSoapFault(SoapFault soapFault) {
        translateNegativeResponse(
                soapFault.faultCode.localPart,
                soapFault.reason,
                SOAP_FAULT_CODE_TO_HTTP_STATUS_CODE_MAPPING,
                SOAP_FAULT_CODE_TO_FHIR_ISSUE_TYPE_CODE_MAPPING,
                serializeSoapFault(soapFault))
    }

    /**
     * Rethrows a UnknownPolicySetIdFault as a FHIR exception.
     */
    static void translateUnknownPolicySetIdFault(UnknownPolicySetIdFaultMessage fault) {
        throw new ResourceNotFoundException("Unknown policy set " + fault.getFaultInfo().getMessage());
    }

    /**
     * Translates a CH:PPQ-1 response into a CH:PPQ-4 response.
     */
    static Bundle translatePpq1To4Response(
            Bundle ppq4Request,
            AssertionBasedRequestType ppq1Request,
            EprPolicyRepositoryResponse ppq1Response,
            String baseUrl = "http://localhost/fhir")
    {
        if (ppq1Response.status == 'urn:e-health-suisse:2015:response-status:success') {
            Bundle ppq4Response = new Bundle(
                    id: UUID.randomUUID().toString(),
                    type: Bundle.BundleType.TRANSACTIONRESPONSE,
            )
            for (ppq4RequestEntry in ppq4Request.entry) {
                def consentId
                if (ppq4RequestEntry.hasResource()) {
                    def consent = (Consent) ppq4RequestEntry.getResource()
                    consentId = ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID)
                } else {
                    consentId = ChPpqmUtils.extractConsentIdFromUrl(ppq4RequestEntry.request.url)
                }
                ppq4Response.entry << new Bundle.BundleEntryComponent(
                        fullUrl: "$baseUrl/Consent?identifier=$consentId",
                        response: new Bundle.BundleEntryResponseComponent(
                                status: (ppq1Request instanceof AddPolicyRequest) ? '201' : '200',
                        ),
                )
            }
            return ppq4Response
        } else {
            throw new UnclassifiedServerFailureException(400, 'PPQ-1 request failed', negativeOperationOutcome())
        }
    }

    static final Map<String, OperationOutcome.IssueType> SAML_STATUS_CODE_TO_FHIR_ISSUE_TYPE_CODE_MAPPING = [
            (Xacml20Status.REQUESTER_ERROR.code) : OperationOutcome.IssueType.INVALID,
            (Xacml20Status.RESPONDER_ERROR.code) : OperationOutcome.IssueType.INVALID,
            (Xacml20Status.VERSION_MISMATCH.code): OperationOutcome.IssueType.STRUCTURE,
    ]

    static final Map<String, Integer> SAML_STATUS_CODE_TO_HTTP_STATUS_CODE_MAPPING = [
            (Xacml20Status.REQUESTER_ERROR.code) : 400,
            (Xacml20Status.RESPONDER_ERROR.code) : 500,
            (Xacml20Status.VERSION_MISMATCH.code): 500,
    ]

    /**
     * Translates a CH:PPQ-2 response into a CH:PPQ-5 response.
     */
    static List<Consent> translatePpq2To5Response(ResponseType ppq2Response) {
        def statusCode = ppq2Response.status.statusCode.value
        if (statusCode == Xacml20Status.SUCCESS.code) {
            def assertion = ppq2Response.assertionOrEncryptedAssertion[0] as AssertionType
            def statement = assertion.statementOrAuthnStatementOrAuthzDecisionStatement[0] as XACMLPolicyStatementType
            return statement.policyOrPolicySet.collect { toConsent(it as PolicySetType) }
        } else {
            translateNegativeResponse(
                    statusCode,
                    ppq2Response.status.statusMessage ?: 'PPQ-2 request failed',
                    SAML_STATUS_CODE_TO_HTTP_STATUS_CODE_MAPPING,
                    SAML_STATUS_CODE_TO_FHIR_ISSUE_TYPE_CODE_MAPPING,
                    null)
        }
    }

}
