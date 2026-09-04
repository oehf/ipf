/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.OidType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.ietf.jgss.Oid;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveDocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveProvideDocumentBundle;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveSubmissionSetList;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the homeCommunityId extension introduced by MHD 4.2.4 with the Target Communities
 * Option (CP-ITI-1326-02).
 *
 * @author Christian Ohr
 */
public class HomeCommunityIdTest {

    private static final Logger log = LoggerFactory.getLogger(HomeCommunityIdTest.class);

    private static final String HOME_COMMUNITY_OID = "1.2.3.4.5";

    private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

    static {
        MhdProfile.registerDefaultTypes(FHIR_CONTEXT);
    }

    @Test
    public void testDocumentReferenceCarriesExtension() throws Exception {
        var documentReference = new ComprehensiveDocumentReference()
            .setHomeCommunityId(new Oid(HOME_COMMUNITY_OID));
        assertTrue(documentReference.hasHomeCommunityId());
        assertEquals("urn:oid:" + HOME_COMMUNITY_OID, documentReference.getHomeCommunityId().getValue());

        var encoded = FHIR_CONTEXT.newJsonParser().encodeResourceToString(documentReference);
        assertTrue(encoded.contains(MhdProfile.HOME_COMMUNITY_ID_PROFILE), encoded);
        assertTrue(encoded.contains("\"valueOid\":\"urn:oid:" + HOME_COMMUNITY_OID + "\""), encoded);

        var parsed = FHIR_CONTEXT.newJsonParser()
            .parseResource(ComprehensiveDocumentReference.class, encoded);
        assertEquals("urn:oid:" + HOME_COMMUNITY_OID, parsed.getHomeCommunityId().getValue());
    }

    @Test
    public void testListCarriesExtension() throws Exception {
        var list = new ComprehensiveSubmissionSetList()
            .setHomeCommunityId(new Oid(HOME_COMMUNITY_OID));
        assertTrue(list.hasHomeCommunityId());

        var encoded = FHIR_CONTEXT.newJsonParser().encodeResourceToString(list);
        assertTrue(encoded.contains(MhdProfile.HOME_COMMUNITY_ID_PROFILE), encoded);

        var parsed = FHIR_CONTEXT.newJsonParser()
            .parseResource(ComprehensiveSubmissionSetList.class, encoded);
        assertEquals("urn:oid:" + HOME_COMMUNITY_OID, parsed.getHomeCommunityId().getValue());
    }

    @Test
    public void testCopyPreservesHomeCommunityId() {
        var documentReference = new ComprehensiveDocumentReference()
            .setHomeCommunityId(new OidType("urn:oid:" + HOME_COMMUNITY_OID));
        assertEquals("urn:oid:" + HOME_COMMUNITY_OID, documentReference.copy().getHomeCommunityId().getValue());

        var list = new ComprehensiveSubmissionSetList()
            .setHomeCommunityId(new OidType("urn:oid:" + HOME_COMMUNITY_OID));
        assertEquals("urn:oid:" + HOME_COMMUNITY_OID,
            ((ComprehensiveSubmissionSetList) list.copy()).getHomeCommunityId().getValue());
    }

    @Test
    public void testEmptyWithoutHomeCommunityId() {
        assertFalse(new ComprehensiveDocumentReference().hasHomeCommunityId());
        assertFalse(new ComprehensiveSubmissionSetList().hasHomeCommunityId());
    }

    /**
     * The extension is only defined as of MHD 4.2.4, so it is validated against that version only.
     */
    @Test
    public void testValidatesAgainstMhd424() throws Exception {
        var bundle = MhdTestBundles.provideAndRegister();
        bundle.getEntry().stream()
            .map(ComprehensiveProvideDocumentBundle.BundleEntryComponent::getResource)
            .forEach(resource -> {
                if (resource instanceof ComprehensiveDocumentReference documentReference) {
                    documentReference.setHomeCommunityId(oid());
                } else if (resource instanceof ComprehensiveSubmissionSetList list) {
                    list.setHomeCommunityId(oid());
                }
            });

        try {
            new MhdValidator(FHIR_CONTEXT, MhdVersion.v424).validateRequest(bundle, Map.of(
                Constants.INTERACTION_REQUEST_VALIDATION_PROFILES,
                Set.of(MhdProfile.ITI65_COMPREHENSIVE_BUNDLE_PROFILE)));
        } catch (UnprocessableEntityException e) {
            var issues = ((OperationOutcome) e.getOperationOutcome()).getIssue();
            issues.forEach(issue -> FhirUtils.logValidationMessage(log, issue));
            assertFalse(issues.stream()
                    .anyMatch(i -> i.getSeverity() == OperationOutcome.IssueSeverity.ERROR),
                "There are validation errors in the bundle");
        }
    }

    private static Oid oid() {
        try {
            return new Oid(HOME_COMMUNITY_OID);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
