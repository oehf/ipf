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

import ca.uhn.fhir.context.FhirVersionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptions;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptionsProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti68bin.Iti68BinaryTransactionConfiguration;

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

    public enum QueryDocumentManifestInteractions implements FhirInteractionId<FhirQueryAuditDataset> {

        ITI_66;

        @Getter FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;

        @Override
        public void init(FhirTransactionOptionsProvider<FhirQueryAuditDataset, ? extends FhirTransactionOptions> optionsProvider,
                         List<? extends FhirTransactionOptions> options) {
            this.fhirTransactionConfiguration = new FhirTransactionConfiguration<>(
                    "mhd-iti66",
                    "Mobile Query for Existing Data",
                    true,
                    optionsProvider.getAuditStrategy(false),
                    optionsProvider.getAuditStrategy(true),
                    FhirVersionEnum.R4,
                    FhirTransactionOptions.concatProviders(options),
                    new Iti66ClientRequestFactory(),
                    FhirTransactionValidator.NO_VALIDATION);
        }
    }

    public enum QueryDocumentReferenceInteractions implements FhirInteractionId<FhirQueryAuditDataset> {

        ITI_67;

        @Getter FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;

        @Override
        public void init(FhirTransactionOptionsProvider<FhirQueryAuditDataset, ? extends FhirTransactionOptions> optionsProvider,
                         List<? extends FhirTransactionOptions> options) {
            this.fhirTransactionConfiguration = new FhirTransactionConfiguration<>(
                    "mhd-iti67",
                    "Mobile Query for Existing Data",
                    true,
                    optionsProvider.getAuditStrategy(false),
                    optionsProvider.getAuditStrategy(true),
                    FhirVersionEnum.R4,
                    FhirTransactionOptions.concatProviders(options),
                    new Iti67ClientRequestFactory(),
                    FhirTransactionValidator.NO_VALIDATION);
        }
    }

    @AllArgsConstructor
    public enum RetrieveInteractions implements InteractionId {
        ITI_68(ITI_68_CONFIG);

        @Getter
        TransactionConfiguration<Iti68AuditDataset> transactionConfiguration;
    }

    @AllArgsConstructor
    public enum RetrieveBinaryInteractions implements InteractionId {
        ITI_68_BIN(ITI_68_BIN_CONFIG);

        @Getter
        TransactionConfiguration<FhirAuditDataset> transactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        var interactions = new ArrayList<InteractionId>();
        interactions.addAll(Arrays.asList(SubmitInteractions.values()));
        interactions.addAll(Arrays.asList(QueryDocumentReferenceInteractions.values()));
        return Collections.unmodifiableList(interactions);
    }


    private static final Iti65TransactionConfiguration ITI_65_CONFIG = new Iti65TransactionConfiguration();
    private static final Iti66TransactionConfiguration ITI_66_CONFIG = new Iti66TransactionConfiguration();
    private static final Iti67TransactionConfiguration ITI_67_CONFIG = new Iti67TransactionConfiguration();
    private static final Iti68TransactionConfiguration ITI_68_CONFIG = new Iti68TransactionConfiguration();
    private static final Iti68BinaryTransactionConfiguration ITI_68_BIN_CONFIG = new Iti68BinaryTransactionConfiguration();
}
