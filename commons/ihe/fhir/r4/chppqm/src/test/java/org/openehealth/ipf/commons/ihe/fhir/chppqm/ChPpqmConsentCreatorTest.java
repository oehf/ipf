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
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3.ChPpq3Validator;

import java.util.Date;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*;

@Slf4j
public class ChPpqmConsentCreatorTest {

    private static final IParser PARSER = ChPpqmUtils.FHIR_CONTEXT.newJsonParser().setPrettyPrint(true);

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
    public void testConsent303Creation2() throws Exception {
        Consent consent = create303Consent(createUuid(), "123456789012345678", "rep123", null, null);
        doTest("303", consent, "PUT", "Resource ID must be present for UPDATE");
    }

}
