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
package org.openehealth.ipf.commons.ihe.fhir.mhd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68TransactionConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class MHD implements IntegrationProfile {

    @AllArgsConstructor
    public enum SubmitInteractions implements FhirInteractionId<Iti65AuditDataset> {
        ITI_65(ITI_65_CONFIG);

        @Getter
        FhirTransactionConfiguration<Iti65AuditDataset> fhirTransactionConfiguration;
    }

    @AllArgsConstructor
    public enum QueryInteractions implements FhirInteractionId<FhirQueryAuditDataset> {
        ITI_66(ITI_66_CONFIG),
        ITI_67(ITI_67_CONFIG);

        @Getter FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;
    }

    @AllArgsConstructor
    public enum RetrieveInteractions implements InteractionId {
        ITI_68(ITI_68_CONFIG);

        @Getter
        TransactionConfiguration<Iti68AuditDataset> transactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        List<InteractionId> interactions = new ArrayList<>();
        interactions.addAll(Arrays.asList(SubmitInteractions.values()));
        interactions.addAll(Arrays.asList(QueryInteractions.values()));
        return Collections.unmodifiableList(interactions);
    }


    private static final Iti65TransactionConfiguration ITI_65_CONFIG = new Iti65TransactionConfiguration();
    private static final Iti66TransactionConfiguration ITI_66_CONFIG = new Iti66TransactionConfiguration();
    private static final Iti67TransactionConfiguration ITI_67_CONFIG = new Iti67TransactionConfiguration();
    private static final Iti68TransactionConfiguration ITI_68_CONFIG = new Iti68TransactionConfiguration();
}
