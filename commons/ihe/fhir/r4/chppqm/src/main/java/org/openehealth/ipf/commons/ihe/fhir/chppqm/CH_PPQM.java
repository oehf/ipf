/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.fhir.BatchTransactionClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.BatchTransactionResourceProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3.*;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4.ChPpq4ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4.ChPpq4ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4.ChPpq4Validator;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5.ChPpq5AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5.ChPpq5ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5.ChPpq5ResourceProvider;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5.ChPpq5Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CH_PPQM implements IntegrationProfile {

    @AllArgsConstructor
    public enum SubmitInteractions implements FhirInteractionId<ChPpqmAuditDataset> {
        CH_PPQ_3(CH_PPQ_3_CONFIG),
        CH_PPQ_4(CH_PPQ_4_CONFIG);

        @Getter
        private final FhirTransactionConfiguration<ChPpqmAuditDataset> fhirTransactionConfiguration;
    }

    @AllArgsConstructor
    public enum QueryInteractions implements FhirInteractionId<FhirQueryAuditDataset> {
        CH_PPQ_5(CH_PPQ_5_CONFIG);

        @Getter
        private final FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        List<InteractionId> result = new ArrayList<>();
        CollectionUtils.addAll(result, SubmitInteractions.values());
        CollectionUtils.addAll(result, QueryInteractions.values());
        return Collections.unmodifiableList(result);
    }

    private static final FhirTransactionConfiguration<ChPpqmAuditDataset> CH_PPQ_3_CONFIG =
            new FhirTransactionConfiguration<>(
                    "ch-ppq3",
                    "Mobile Privacy Policy Feed",
                    false,
                    new ChPpq3ClientAuditStrategy(),
                    new ChPpq3ServerAuditStrategy(),
                    ChPpqmUtils.FHIR_CONTEXT,
                    new ChPpq3ResourceProvider(),
                    new ChPpq3RequestFactory(),
                    ChPpq3Validator::new);

    private static final FhirTransactionConfiguration<ChPpqmAuditDataset> CH_PPQ_4_CONFIG =
            new FhirTransactionConfiguration<>(
                    "ch-ppq4",
                    "Mobile Privacy Policy Bundle Feed",
                    false,
                    new ChPpq4ClientAuditStrategy(),
                    new ChPpq4ServerAuditStrategy(),
                    ChPpqmUtils.FHIR_CONTEXT,
                    BatchTransactionResourceProvider.getInstance(),
                    BatchTransactionClientRequestFactory.getInstance(),
                    ChPpq4Validator::new);

    private static final FhirTransactionConfiguration<FhirQueryAuditDataset> CH_PPQ_5_CONFIG =
            new FhirTransactionConfiguration<>(
                    "ch-ppq5",
                    "Mobile Privacy Policy Query",
                    true,
                    new ChPpq5AuditStrategy(false),
                    new ChPpq5AuditStrategy(true),
                    ChPpqmUtils.FHIR_CONTEXT,
                    new ChPpq5ResourceProvider(),
                    new ChPpq5ClientRequestFactory(),
                    ChPpq5Validator::new);

}
