/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti83;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Type;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpQueryInformationBuilder;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for auditing ITI-83 transactions
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti83AuditStrategy extends FhirQueryAuditStrategy {

    public Iti83AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new BalpQueryInformationBuilder(auditContext, auditDataset, FhirEventTypeCode.MobilePatientIdentifierCrossReferenceQuery)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters(
                        "PIXmQuery",
                        FhirParticipantObjectIdTypeCode.MobilePatientIdentifierCrossReferenceQuery,
                        auditDataset.getQueryString())

                .getMessages();
    }

    /**
     * Records the patient the query names and, if the request URL carried no query string, the query
     * itself.
     *
     * @param auditDataset audit dataset
     * @param request      request object
     * @param parameters   request parameters
     * @return enriched audit dataset
     */
    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        var params = (Parameters) request;
        if (params != null) {
            var sourceIdentifier = params.getParameter().stream()
                    .filter(ppc -> Constants.SOURCE_IDENTIFIER_NAME.equals(ppc.getName()))
                    .map(Parameters.ParametersParameterComponent::getValue)
                    .findFirst().orElseThrow(() -> new RuntimeException("No sourceIdentifier in PIX query"));

            dataset.getPatientIds().add(queryToken(sourceIdentifier));

            if (isBlank(dataset.getQueryString())) {
                dataset.setQueryString(queryOf(params));
            }
        }
        return dataset;
    }

    /**
     * Reconstructs the query of a PIXm request whose URL carried none.
     * <p>
     * A query may name the patient by resource id -- {@code GET [base]/Patient/[id]/$ihe-pix} -- instead
     * of by sourceIdentifier, and that URL has no query string. The PIXm audit profiles nevertheless
     * inherit the mandatory query entity of the BALP query pattern, so leaving it empty makes the record
     * non-conformant. {@code Iti83ResourceProvider} has by then turned both flavours into the same
     * {@code Parameters}, so the criteria the transaction actually ran can be rendered back into the
     * query the sourceIdentifier flavour would have carried, and the two audit the same way.
     *
     * @param params the query parameters of the transaction
     * @return the reconstructed query
     */
    private static String queryOf(Parameters params) {
        return params.getParameter().stream()
                .filter(ppc -> Constants.SOURCE_IDENTIFIER_NAME.equals(ppc.getName()) ||
                        Constants.TARGET_SYSTEM_NAME.equals(ppc.getName()))
                .filter(Parameters.ParametersParameterComponent::hasValue)
                .map(ppc -> ppc.getName() + "=" + queryToken(ppc.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
     * Renders a query parameter value the way it appears in the URL of the transaction, which is also the
     * form an audit record carries a patient participant object id in.
     * <p>
     * An {@link Identifier} becomes the {@code system|value} search token, but only when it has a system:
     * a query by resource id has no namespace to name, and formatting the missing one anyway would put
     * the string "null" into the record -- and, once the AuditEvent is built from it, into
     * {@code Identifier.system}, which has to be an absolute URI.
     *
     * @param value value of a query parameter
     * @return its rendering in the query
     */
    private static String queryToken(Type value) {
        if (value instanceof Identifier identifier) {
            return isBlank(identifier.getSystem()) ?
                    identifier.getValue() :
                    String.format("%s|%s", identifier.getSystem(), identifier.getValue());
        }
        if (value instanceof PrimitiveType<?> primitive) {
            return primitive.getValueAsString();
        }
        return value.toString();
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(FhirQueryAuditDataset auditDataset, Object response, AuditContext auditContext) {
        var result = super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
        if (auditContext.isIncludeParticipantsFromResponse()) {
        // TODO
        }
        return result;
    }
}
