/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Christian Ohr
 */
public class MhdValidatorTest {

    private static final Logger log = LoggerFactory.getLogger(MhdValidatorTest.class);

    private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

    static {
        MhdProfile.registerDefaultTypes(FHIR_CONTEXT);
    }

    @Test
    public void testBundle() throws Exception {
        assertNoValidationErrors(new MhdValidator(FHIR_CONTEXT));
    }

    /**
     * The resources built by the model classes must remain valid against MHD 4.2.3, which requires
     * the {@code Identifier.use}, as well as against MHD 4.2.4, which requires the
     * {@code Identifier.type} instead (CP-ITI-1328-01).
     */
    @Test
    public void testBundleConformsToBothMhdVersions() throws Exception {
        assertNoValidationErrors(new MhdValidator(FHIR_CONTEXT, MhdVersion.v423));
        assertNoValidationErrors(new MhdValidator(FHIR_CONTEXT, MhdVersion.v424));
    }

    private void assertNoValidationErrors(MhdValidator mhdValidator) throws Exception {
        var bundle = MhdTestBundles.provideAndRegister();
        try {
            mhdValidator.validateRequest(bundle, Map.of(
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
}
