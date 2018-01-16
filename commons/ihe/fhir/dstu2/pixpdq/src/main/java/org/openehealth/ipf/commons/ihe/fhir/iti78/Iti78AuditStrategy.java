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
package org.openehealth.ipf.commons.ihe.fhir.iti78;

import org.hl7.fhir.instance.model.IdType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

import java.util.Map;

/**
 * Strategy for auditing ITI-78 transactions
 *
 * @author Christian Ohr
 * @since 3.1
 */
class Iti78AuditStrategy extends FhirQueryAuditStrategy {

    protected Iti78AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new QueryInformationBuilder<>(auditContext, auditDataset, FhirEventTypeCodes.MobilePatientDemographicsQuery)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters(
                        "MobilePatientDemographicsQuery",
                        FhirParticipantObjectIdTypeCodes.MobilePatientDemographicsQuery,
                        auditDataset.getQueryString())

                .getMessages();
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        FhirQueryAuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        if (request instanceof IdType) {
            IdType idType = (IdType) request;
            dataset.getPatientIds().add(idType.getValue());
        }
        return dataset;
    }

}
