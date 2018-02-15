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
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.iti64.Iti64AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.iti64.Iti64AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XPID implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId<Iti64AuditDataset> {
        ITI_64(ITI_64_CONFIGURATION, ITI_64_NAK_FACTORY);

        @Getter private Hl7v2TransactionConfiguration<Iti64AuditDataset> hl7v2TransactionConfiguration;
        @Getter private NakFactory<Iti64AuditDataset> nakFactory;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Hl7v2TransactionConfiguration<Iti64AuditDataset> ITI_64_CONFIGURATION =
            new Hl7v2TransactionConfiguration<>(
                    "xpid-iti64",
                    "XAD-PID Change Management",
                    false,
                    new Iti64AuditStrategy(false),
                    new Iti64AuditStrategy(true),
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
                    HapiContextFactory.createHapiContext(
                            CustomModelClassUtils.createFactory("xpid", "2.5"),
                            PixPdqTransactions.ITI64));

    private static final NakFactory ITI_64_NAK_FACTORY = new NakFactory(ITI_64_CONFIGURATION);
}
