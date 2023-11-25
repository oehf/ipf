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

package org.openehealth.ipf.commons.ihe.fhir.chppqm;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapFault;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3.ChPpq3Validator;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4.ChPpq4Validator;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.translation.FhirToXacmlTranslator;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.translation.XacmlToFhirTranslator;
import org.openehealth.ipf.commons.ihe.xacml20.*;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*;


@Slf4j
public class TranslationTest {

    private static final IParser PARSER = ChPpqmUtils.FHIR_CONTEXT.newJsonParser().setPrettyPrint(true);

    private static final ChPpqMessageCreator PPQ_MESSAGE_CREATOR = new ChPpqMessageCreator("urn:oid:1.2.3");
    private static final FhirToXacmlTranslator FHIR_TO_XACML_TRANSLATOR = new FhirToXacmlTranslator(PPQ_MESSAGE_CREATOR);

    @BeforeAll
    public static void beforeAll() {
        Xacml20Utils.initializeHerasaf();
    }

    private static void doTest(String templateId, Consent consent, String httpMethod, String expectedError) throws Exception {
        log.debug("Consent:\n{}", PARSER.encodeResourceToString(consent));

        consent.getIdentifier().stream()
                .filter(id -> "templateId".equals(id.getType().getCoding().get(0).getCode()) && templateId.equals(id.getValue()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Expected template ID " + templateId));

        try {
            new ChPpq3Validator(ChPpqmUtils.FHIR_CONTEXT).validateRequest(consent, Map.of(Constants.HTTP_METHOD, httpMethod));
            if (expectedError != null) {
                throw new IllegalStateException("Expected error: " + expectedError);
            }

            PolicySetType policySet = FHIR_TO_XACML_TRANSLATOR.toPolicySet(consent);
            Consent consent2 = XacmlToFhirTranslator.toConsent(policySet);
            assertEquals(
                    ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.TEMPLATE_ID),
                    ChPpqmUtils.extractConsentId(consent2, ChPpqmUtils.ConsentIdTypes.TEMPLATE_ID));

            new ChPpq3Validator(ChPpqmUtils.FHIR_CONTEXT).validateRequest(consent2, Map.of(Constants.HTTP_METHOD, httpMethod));

        } catch (UnprocessableEntityException e) {
            OperationOutcome outcome = (OperationOutcome) e.getOperationOutcome();
            log.error("OperationOutcome:\n{}", PARSER.encodeResourceToString(outcome));
            if (expectedError == null) {
                throw e;
            } else {
                outcome.getIssue().stream()
                        .filter(issue ->
                                (issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR) &&
                                        issue.getDiagnostics().contains(expectedError))
                        .findAny()
                        .orElseThrow(() -> e);
            }
        }
    }

    @Test
    public void testConsent201Creation1() throws Exception {
        Consent consent = create201Consent(createUuid(), "123456789012345678");
        doTest("201", consent, "POST", null);
    }

    @Test
    public void testConsent202Creation1() throws Exception {
        Consent consent = create202Consent(createUuid(), "123456789012345678",
                "urn:e-health-suisse:2015:policies:access-level:normal");
        doTest("202", consent, "POST", null);
    }

    @Test
    public void testConsent203Creation1() throws Exception {
        Consent consent = create203Consent(createUuid(), "123456789012345678",
                "urn:e-health-suisse:2015:policies:provide-level:restricted");
        doTest("203", consent, "POST", null);
    }

    @Test
    public void testConsent301Creation1() throws Exception {
        Consent consent = create301Consent(createUuid(), "123456789012345678", "3210987654321",
                "urn:e-health-suisse:2015:policies:access-level:delegation-and-normal", null, new Date());
        doTest("301", consent, "POST", null);
    }

    @Test
    public void testConsent302Creation1() throws Exception {
        Consent consent = create302Consent(createUuid(), "123456789012345678", "urn:oid:1.2.3.4.5",
                "urn:e-health-suisse:2015:policies:access-level:restricted", null, new Date());
        doTest("302", consent, "POST", null);
    }

    @Test
    public void testConsent303Creation1() throws Exception {
        Consent consent = create303Consent(createUuid(), "123456789012345678", "rep123", null, null);
        doTest("303", consent, "POST", null);
    }

    @Test
    public void testPpq3To1RequestTranslation() {
        Consent consent = create303Consent(createUuid(), "123456789012345678", "rep123", null, null);

        AddPolicyRequest addRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq3To1Request(consent, "POST");
        Xacml20MessageValidator.validateChPpq1Request(addRequest);

        UpdatePolicyRequest updateRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq3To1Request(consent, "PUT");
        Xacml20MessageValidator.validateChPpq1Request(updateRequest);

        DeletePolicyRequest deleteRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq3To1Request(consent, "DELETE");
        Xacml20MessageValidator.validateChPpq1Request(deleteRequest);

        log.info("");
    }

    @Test
    public void testPpq1To3ResponseTranslation() {
        Consent consent = create303Consent(createUuid(), "123456789012345678", "rep123", null, null);
        PolicySetType policySet = FHIR_TO_XACML_TRANSLATOR.toPolicySet(consent);
        AddPolicyRequest addRequest = PPQ_MESSAGE_CREATOR.createAddPolicyRequest(Collections.singletonList(policySet));

        EprPolicyRepositoryResponse ppq1Response = new EprPolicyRepositoryResponse();
        ppq1Response.setStatus(PpqConstants.StatusCode.SUCCESS);
        MethodOutcome methodOutcome = XacmlToFhirTranslator.translatePpq1To3Response(consent, addRequest, ppq1Response);

        log.info("");
    }

    @Test
    public void testPpq4To1RequestTranslation() {
        List<Consent> consents = List.of(
                create201Consent(createUuid(), "123456789012345678"),
                create202Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:access-level:normal"),
                create203Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:provide-level:restricted"));

        {
            Bundle ppq4Request = ChPpqmUtils.createPpq4SubmitRequestBundle(consents, Bundle.HTTPVerb.POST);
            assert ppq4Request.getMeta().getProfile().stream()
                    .anyMatch(canonicalType -> ChPpqmUtils.Profiles.FEED_REQUEST_BUNDLE.equals(canonicalType.getValue()));
            new ChPpq4Validator(ChPpqmUtils.FHIR_CONTEXT).validateRequest(ppq4Request, null);
            AddPolicyRequest addRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq4To1Request(ppq4Request);
            Xacml20MessageValidator.validateChPpq1Request(addRequest);
        }
        {
            Bundle ppq4Request = ChPpqmUtils.createPpq4SubmitRequestBundle(consents, Bundle.HTTPVerb.PUT);
            assert ppq4Request.getMeta().getProfile().stream()
                    .anyMatch(canonicalType -> ChPpqmUtils.Profiles.FEED_REQUEST_BUNDLE.equals(canonicalType.getValue()));
            new ChPpq4Validator(ChPpqmUtils.FHIR_CONTEXT).validateRequest(ppq4Request, null);
            UpdatePolicyRequest updateRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq4To1Request(ppq4Request);
            Xacml20MessageValidator.validateChPpq1Request(updateRequest);
        }
        {
            Set<String> consentIds = consents.stream()
                    .map(consent -> ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID))
                    .collect(Collectors.toSet());
            Bundle ppq4Request = ChPpqmUtils.createPpq4DeleteRequestBundle(consentIds);
            assert ppq4Request.getMeta().getProfile().stream()
                    .anyMatch(canonicalType -> ChPpqmUtils.Profiles.FEED_REQUEST_BUNDLE.equals(canonicalType.getValue()));
            new ChPpq4Validator(ChPpqmUtils.FHIR_CONTEXT).validateRequest(ppq4Request, null);
            DeletePolicyRequest deleteRequest = FHIR_TO_XACML_TRANSLATOR.translatePpq4To1Request(ppq4Request);
            Xacml20MessageValidator.validateChPpq1Request(deleteRequest);
        }

        log.info("");
    }

    @Test
    public void testPpq1To4ResponseTranslation() {
        List<Consent> consents = List.of(
                create201Consent(createUuid(), "123456789012345678"),
                create202Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:access-level:normal"),
                create203Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:provide-level:restricted"));

        Bundle ppq4Request = ChPpqmUtils.createPpq4SubmitRequestBundle(consents, Bundle.HTTPVerb.POST);
        AddPolicyRequest ppq1Request = FHIR_TO_XACML_TRANSLATOR.translatePpq4To1Request(ppq4Request);
        EprPolicyRepositoryResponse ppq1Response = new EprPolicyRepositoryResponse();
        ppq1Response.setStatus(PpqConstants.StatusCode.SUCCESS);
        Bundle ppq4Response = XacmlToFhirTranslator.translatePpq1To4Response(ppq4Request, ppq1Request, ppq1Response);
        new ChPpq4Validator(ChPpqmUtils.FHIR_CONTEXT).validateResponse(ppq4Response, null);
        log.info("");
    }

    @Test
    public void testPpq5To2RequestTranslation() {
        XACMLPolicyQueryType ppq2Request1 = FHIR_TO_XACML_TRANSLATOR.translatePpq5To2Request(
                Consent.SP_IDENTIFIER + '=' + createUuid());
        Xacml20MessageValidator.validateChPpq2Request(ppq2Request1);

        XACMLPolicyQueryType ppq2Request2 = FHIR_TO_XACML_TRANSLATOR.translatePpq5To2Request(
                Consent.SP_PATIENT + ':' + Patient.SP_IDENTIFIER + '=' +
                        PpqConstants.CodingSystemIds.SWISS_PATIENT_ID + "|123456789012345678");
        Xacml20MessageValidator.validateChPpq2Request(ppq2Request2);

        log.info("");
    }

    @Test
    public void testPpq2To5ResponseTranslation1() {
        List<Consent> consents = List.of(
                create201Consent(createUuid(), "123456789012345678"),
                create202Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:access-level:normal"),
                create203Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:provide-level:restricted"));
        List<PolicySetType> policySets = consents.stream().map(FHIR_TO_XACML_TRANSLATOR::toPolicySet).collect(Collectors.toList());
        ResponseType ppq2Response = PPQ_MESSAGE_CREATOR.createPositivePolicyQueryResponse(policySets);
        List<Consent> ppq5Response = XacmlToFhirTranslator.translatePpq2To5Response(ppq2Response);
        assertEquals(consents.size(), ppq5Response.size());
    }

    @Test
    public void testPpq2To5ResponseTranslation2() {
        boolean correct = false;
        try {
            ResponseType ppq2Response = PPQ_MESSAGE_CREATOR.createNegativeQueryResponse(new Xacml20Exception(Xacml20Status.REQUESTER_ERROR));
            XacmlToFhirTranslator.translatePpq2To5Response(ppq2Response);
        } catch (UnclassifiedServerFailureException e) {
            assertEquals(400, e.getStatusCode());
            correct = true;
        }
        assertTrue(correct);
    }

    @Test
    public void testSoapFaultTranslation() {
        boolean correct = false;
        try {
            SoapFault soapFault = new SoapFault("Error1", new QName(Soap12.SOAP_NAMESPACE, "Receiver"));
            XacmlToFhirTranslator.translateSoapFault(soapFault);
        } catch (UnclassifiedServerFailureException e) {
            OperationOutcome operationOutcome = (OperationOutcome) e.getOperationOutcome();
            assertEquals(1, operationOutcome.getIssue().size());
            assertTrue(operationOutcome.getIssue().get(0).getDiagnostics().startsWith("<ns1:Fault"));
            assertEquals(503, e.getStatusCode());
            correct = true;
        }
        assertTrue(correct);
    }

}