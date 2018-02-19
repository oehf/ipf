/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;


/**
 * @author Christian Ohr
 * @since 3.5
 */
public class Pcc44AuditStrategy extends FhirQueryAuditStrategy {

    public Pcc44AuditStrategy(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new QueryInformationBuilder<>(auditContext, auditDataset, FhirEventTypeCode.MobileQueryExistingData)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters("MobileQueryExistingData",
                        FhirParticipantObjectIdTypeCode.MobileQueryExistingData,
                        auditDataset.getQueryString())
                .getMessages();
    }

}
