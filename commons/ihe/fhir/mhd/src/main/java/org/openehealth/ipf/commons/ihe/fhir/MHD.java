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

package org.openehealth.ipf.commons.ihe.fhir;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.InteractionProfile;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67TransactionConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class MHD implements InteractionProfile {


    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements FhirInteractionId {

        ITI_65("mhd-iti65",
                "Provide Document Bundle",
                false,
                ITI65_CONFIGURATION) {
            @Override
            public FhirAuditStrategy<Iti65AuditDataset> getClientAuditStrategy() {
                return Iti65ClientAuditStrategy.getInstance();
            }

            @Override
            public FhirAuditStrategy<Iti65AuditDataset> getServerAuditStrategy() {
                return Iti65ServerAuditStrategy.getInstance();
            }
        },
        ITI_66("mhd-iti66",
                "Find Document Manifests",
                true,
                ITI66_CONFIGURATION) {
            @Override
            public  FhirAuditStrategy<FhirQueryAuditDataset> getClientAuditStrategy() {
                return Iti66ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<FhirQueryAuditDataset> getServerAuditStrategy() {
                return Iti66ServerAuditStrategy.getInstance();
            }
        },
        ITI_67("mhd-iti67",
                "Find Document References",
                true,
                ITI67_CONFIGURATION) {
            @Override
            public  FhirAuditStrategy<FhirQueryAuditDataset> getClientAuditStrategy() {
                return Iti67ClientAuditStrategy.getInstance();
            }

            @Override
            public FhirAuditStrategy<FhirQueryAuditDataset> getServerAuditStrategy() {
                return Iti67ServerAuditStrategy.getInstance();
            }
        },
        ITI_68("mhd-iti68",
                "Retrieve Document",
                false,
                null);

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter FhirTransactionConfiguration fhirTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }


    private static final Iti65TransactionConfiguration ITI65_CONFIGURATION = new Iti65TransactionConfiguration();
    private static final Iti66TransactionConfiguration ITI66_CONFIGURATION = new Iti66TransactionConfiguration();
    private static final Iti67TransactionConfiguration ITI67_CONFIGURATION = new Iti67TransactionConfiguration();
}
