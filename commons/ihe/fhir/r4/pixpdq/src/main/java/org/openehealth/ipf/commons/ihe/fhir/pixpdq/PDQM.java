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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78TransactionConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class PDQM implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements FhirInteractionId<FhirQueryAuditDataset> {

        ITI_78(ITI_78_CONFIG),
        ITI_119(ITI_119_CONFIG);

        @Getter
        private final FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Iti78TransactionConfiguration ITI_78_CONFIG = new Iti78TransactionConfiguration();
    private static final Iti119TransactionConfiguration ITI_119_CONFIG = new Iti119TransactionConfiguration();
}
