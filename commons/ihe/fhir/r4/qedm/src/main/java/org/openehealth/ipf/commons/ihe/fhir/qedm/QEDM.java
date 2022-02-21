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
package org.openehealth.ipf.commons.ihe.fhir.qedm;

import ca.uhn.fhir.context.FhirVersionEnum;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.fhir.*;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.pcc44.Pcc44ClientRequestFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class QEDM implements IntegrationProfile {


    public enum Interactions implements FhirInteractionId<FhirQueryAuditDataset> {

        PCC_44;

        @Getter
        private FhirTransactionConfiguration<FhirQueryAuditDataset> fhirTransactionConfiguration;

        @Override
        public void init(FhirTransactionOptionsProvider<FhirQueryAuditDataset, ? extends FhirTransactionOptions> optionsProvider,
                         List<? extends FhirTransactionOptions> options) {
            this.fhirTransactionConfiguration = new FhirTransactionConfiguration<>(
                    "qedm-pcc44",
                    "Mobile Query for Existing Data",
                    true,
                    optionsProvider.getAuditStrategy(false),
                    optionsProvider.getAuditStrategy(true),
                    FhirVersionEnum.R4,
                    FhirTransactionOptions.concatProviders(options),
                    new Pcc44ClientRequestFactory(),
                    FhirTransactionValidator.NO_VALIDATION);
        }
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        List<InteractionId> interactions = Arrays.asList(Interactions.values());
        return Collections.unmodifiableList(interactions);
    }

}
