/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirValidator;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validator for ITI-65 transactions. One day we'll provide StructureDefinitions for the purpose of validation,
 * but for now
 */
public class Iti65Validator implements FhirValidator {

    @Override
    public void validateRequest(Object payload, Map<String, Object> headers) {
        Bundle transactionBundle = (Bundle) payload;
        validateTransactionBundle(transactionBundle);
    }

    @Override
    public void validateResponse(Object payload) {

    }

    /**
     * Validates bundle type and meta data
     *
     * @param bundle bundle
     */
    protected void validateTransactionBundle(Bundle bundle) {
        if (!Bundle.BundleType.TRANSACTION.equals(bundle.getType())) {
            unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null,
                    "Bundle type must be %s, but was %s",
                    Bundle.BundleType.TRANSACTION.toCode(), bundle.getType().toCode());
        }
        List<UriType> profiles = bundle.getMeta().getProfile();
        if (profiles.isEmpty() || !Constants.ITI65_TAG.getCode().equals(profiles.get(0).getValue())) {
            unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null,
                    "Request bundle must have profile",
                    Constants.ITI65_TAG.getCode());
        }
    }

    protected void validateDocumentReference(Bundle bundle) {

        for (DocumentReference documentReference : getBundleEntries(bundle, DocumentReference.class)) {


            if (documentReference.getMasterIdentifier() == null) {
                unprocessableEntity(
                        OperationOutcome.IssueSeverity.ERROR,
                        OperationOutcome.IssueType.REQUIRED,
                        null,
                        "MasterIdentifier in DocumentReference must be present"
                );
            }
            if (documentReference.getSubject() == null) {
                unprocessableEntity(
                        OperationOutcome.IssueSeverity.ERROR,
                        OperationOutcome.IssueType.REQUIRED,
                        ErrorCode.UNKNOWN_PATIENT_ID,
                        "Subject in DocumentReference must be present"
                );
            }
            if (documentReference.getAuthor().isEmpty()) {
                unprocessableEntity(
                        OperationOutcome.IssueSeverity.ERROR,
                        OperationOutcome.IssueType.REQUIRED,
                        null,
                        "Author in DocumentReference must be present"
                );
            }
        }
    }


    private void unprocessableEntity(OperationOutcome.IssueSeverity severity,
                                     OperationOutcome.IssueType type,
                                     ErrorCode xdsErrorCode,
                                     String msg,
                                     Object... args) {
        OperationOutcome operationOutcome = new OperationOutcome();
        CodeableConcept errorCode = null;
        if (xdsErrorCode != null) {
            errorCode = new CodeableConcept();
            errorCode.addCoding().setCode(xdsErrorCode.getOpcode());
        }
        operationOutcome.addIssue()
                .setSeverity(severity)
                .setCode(type)
                .setDetails(errorCode);
        throw new UnprocessableEntityException(String.format(msg, args), operationOutcome);
    }


    private static <T extends IBaseResource> List<T> getBundleEntries(Bundle bundle, Class<T> type) {
        List<T> entries = new ArrayList<>();
        for (Bundle.BundleEntryComponent component : bundle.getEntry()) {
            if (type.isAssignableFrom(component.getResource().getClass())) {
                entries.add(((T) component.getResource()));
            }
        }
        return entries;
    }
}
