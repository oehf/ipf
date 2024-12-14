/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.ihe.core.atna.event.DefaultQueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_QUERY;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_URL;

/**
 * Generic Audit Strategy for CMPD PHARM-5 query transactions.
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class Pharm5AuditStrategy extends FhirAuditStrategy<FhirQueryAuditDataset> {

    public Pharm5AuditStrategy(final boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(final AuditContext auditContext,
                                           final FhirQueryAuditDataset auditDataset) {
        var operation = "unknown";
        final var endpointUrl = auditDataset.getDestinationUserId();
        if (endpointUrl != null && endpointUrl.lastIndexOf("$") >= 0) {
            operation = endpointUrl.substring(endpointUrl.lastIndexOf("$"));
        }
        return new DefaultQueryInformationBuilder(auditContext, auditDataset, FhirEventTypeCode.QueryPharmacyDocumentsOverMhd)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters(
                        operation,
                        FhirParticipantObjectIdTypeCode.QueryPharmacyDocumentsOverMhd,
                        auditDataset.getQueryString(),
                        ParticipantObjectTypeCode.System,
                        ParticipantObjectTypeCodeRole.Query,
                        List.of(new TypeValuePairType("QueryEncoding", "UTF-8")))
                .getMessages();
    }

    /**
     * Further enrich the audit dataset: add query string and patient IDs in the search parameter (if available).
     *
     * @param auditDataset audit dataset
     * @param request      request object
     * @param parameters   request parameters
     * @return enriched audit dataset
     */
    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(final FhirQueryAuditDataset auditDataset,
                                                               final Object request,
                                                               final Map<String, Object> parameters) {
        final var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        final BiConsumer<String, String> addPatientId = (value, system) -> {
            system = (system.startsWith("urn:oid:")) ?
                    system.substring(8) :
                    system;
            dataset.getPatientIds().add(String.format("%s^^^&%s&ISO", value, system));
        };

        final var searchParameters = (Pharm5SearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
        if (searchParameters != null) {
            final var tokenParams = searchParameters.getPatientIdParam();
            if (tokenParams != null) {
                tokenParams.forEach(t -> addPatientId.accept(t.getValue(), t.getSystem()));
            }
        } else if (request instanceof Parameters bodyParameters) {
            final var patientIdentifier = bodyParameters.getParameterValues(Pharm5ResourceProvider.SP_PATIENT_IDENTIFIER);
            if (patientIdentifier instanceof StringType) {
                final var parts = ((StringType) patientIdentifier).getValue().split("\\|");
                addPatientId.accept(parts[1], parts[0]);
            }
        }

        if (parameters.containsKey(HTTP_QUERY)) {
            dataset.setQueryString(URLDecoder.decode((String) parameters.get(HTTP_QUERY), StandardCharsets.UTF_8));
        }
        if (parameters.containsKey(HTTP_URL)) {
            dataset.setServiceEndpointUrl(URLDecoder.decode((String) parameters.get(HTTP_URL), StandardCharsets.UTF_8));
        }

        return dataset;
    }

    @Override
    public FhirQueryAuditDataset createAuditDataset() {
        return new FhirQueryAuditDataset(isServerSide());
    }
}
