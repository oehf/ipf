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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.InteractionProfile;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti64.Iti64AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti64.Iti64ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti64.Iti64ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XPID implements InteractionProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId {

        ITI_64("xpid-iti64",
                "XAD-PID Change Management",
                false,
                ITI64_CONFIGURATION,
                ITI64_NAK_FACTORY) {
            @Override
            public AuditStrategy<Iti64AuditDataset> getClientAuditStrategy() {
                return Iti64ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<Iti64AuditDataset> getServerAuditStrategy() {
                return Iti64ServerAuditStrategy.getInstance();
            }
        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter private NakFactory nakFactory;

    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Hl7v2TransactionConfiguration ITI64_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
                    new Version[] {Version.V25},
                    "XPID adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[] {"ADT"},
                    new String[] {"A43"},
                    new String[] {"ACK"},
                    new String[] {"*"},
                    new boolean[] {true},
                    new boolean[] {false},
                    HapiContextFactory.createHapiContext(PixPdqTransactions.ITI64));

    private static final NakFactory ITI64_NAK_FACTORY = new NakFactory(ITI64_CONFIGURATION);
}
