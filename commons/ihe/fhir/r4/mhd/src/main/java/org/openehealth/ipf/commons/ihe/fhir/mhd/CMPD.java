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
package org.openehealth.ipf.commons.ihe.fhir.mhd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.pharm5.Pharm5TransactionConfiguration;

import java.util.List;

/**
 * Integration profile for MHD-based CMPD transactions.
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class CMPD implements IntegrationProfile {

    private static final Pharm5TransactionConfiguration PHARM_5_CONFIG = new Pharm5TransactionConfiguration();

    @Override
    public List<InteractionId> getInteractionIds() {
        return List.of(QueryPharmacyDocumentsInteractions.values());
    }

    @AllArgsConstructor
    public enum QueryPharmacyDocumentsInteractions implements FhirInteractionId<FhirQueryAuditDataset> {
        PHARM_5(PHARM_5_CONFIG);

        @Getter
        private final FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;
    }
}
