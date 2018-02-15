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
package org.openehealth.ipf.commons.ihe.hl7v2;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.FeedAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.iti30.Iti30AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.iti31.Iti31AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pam.PamTransactions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PAM implements IntegrationProfile {

    public enum Interactions implements Hl7v2InteractionId<FeedAuditDataset> {
        ITI_30 {
            @Override
            public void init(List<? extends HL7v2TransactionOption> options) {
                init("pam-iti30",
                        "Patient Identity Management",
                        false,
                        new Iti30AuditStrategy(false),
                        new Iti30AuditStrategy(true),
                        PamTransactions.ITI30,
                        options);
            }
        },
        ITI_31 {
            @Override
            public void init(List<? extends HL7v2TransactionOption> options) {
                init("pam-iti31",
                        "Patient Encounter Management",
                        false,
                        new Iti31AuditStrategy(false),
                        new Iti31AuditStrategy(true),
                        PamTransactions.ITI31,
                        options);
            }
        };

        @Getter private Hl7v2TransactionConfiguration<FeedAuditDataset> hl7v2TransactionConfiguration;
        @Getter private NakFactory<FeedAuditDataset> nakFactory;

        protected void init(
                String name,
                String description,
                boolean isQuery,
                AuditStrategy<FeedAuditDataset> clientAuditStrategy,
                AuditStrategy<FeedAuditDataset> serverAuditStrategy,
                PamTransactions pamTransactions,
                List<? extends HL7v2TransactionOption> options)
        {
            this.hl7v2TransactionConfiguration = new Hl7v2TransactionConfiguration<>(
                    name,
                    description,
                    isQuery,
                    clientAuditStrategy,
                    serverAuditStrategy,
                    new Version[]{Version.V25},
                    "PIM adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[]{"ADT"},
                    new String[]{HL7v2TransactionOption.concatAllToString(options)},
                    new String[]{"ACK"},
                    new String[]{"*"},
                    new boolean[]{true},
                    new boolean[]{false},
                    HapiContextFactory.createHapiContext(pamTransactions));

            this.nakFactory = new NakFactory<>(this.hl7v2TransactionConfiguration);
        }
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }
}
