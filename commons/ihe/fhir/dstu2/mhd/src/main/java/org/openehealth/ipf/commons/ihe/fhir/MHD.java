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
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67TransactionConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class MHD implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements FhirInteractionId {
        ITI_65(ITI_65_CONFIG),
        ITI_66(ITI_66_CONFIG),
        ITI_67(ITI_67_CONFIG),
        ITI_68(ITI_68_CONFIG);

        @Getter FhirTransactionConfiguration fhirTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }


    private static final Iti65TransactionConfiguration ITI_65_CONFIG = new Iti65TransactionConfiguration();
    private static final Iti66TransactionConfiguration ITI_66_CONFIG = new Iti66TransactionConfiguration();
    private static final Iti67TransactionConfiguration ITI_67_CONFIG = new Iti67TransactionConfiguration();
    private static final FhirTransactionConfiguration ITI_68_CONFIG = new FhirTransactionConfiguration(
            "mhd-iti68",
            "Retrieve Document",
            false,
            null,
            null,
            null,
            null,
            null,
            null);
}
