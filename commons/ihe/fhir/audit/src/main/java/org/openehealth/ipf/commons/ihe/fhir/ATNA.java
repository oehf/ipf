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
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti81.Iti81ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti81.Iti81Configuration;
import org.openehealth.ipf.commons.ihe.fhir.iti81.Iti81ServerAuditStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class ATNA implements InteractionProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements FhirInteractionId {

        ITI_81("atna-iti81",
                "Retrieve ATNA Audit Event",
                true,
                ITI81_CONFIGURATION) {
            @Override
            public AuditStrategy<FhirQueryAuditDataset> getClientAuditStrategy() {
                return Iti81ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<FhirQueryAuditDataset> getServerAuditStrategy() {
                return Iti81ServerAuditStrategy.getInstance();
            }
        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter FhirTransactionConfiguration fhirTransactionConfiguration;
    }



    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final FhirTransactionConfiguration ITI81_CONFIGURATION = new Iti81Configuration();
}
